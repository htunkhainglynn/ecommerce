package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exceprion.ProductException;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductVariantService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ProductVariantRepository repo;
    @Autowired
    ProductRepository productRepo;
    @Autowired
    RedisTemplate<String, ProductVariantCache> redisTemplate;

    @Override
    public List<ProductVariantDto> getAllProductVariants() {
        return repo.findAll()
                .stream()
                .map(ProductVariantDto::new)
                .toList();
    }

    @Override
    public ProductVariantDto createProductVariant(ProductVariantDto productVariantDto) {
        Optional<Product> product = productRepo.findById(productVariantDto.getProduct_id());
        if (product.isPresent()) {
            ProductVariant productVariant = modelMapper.map(productVariantDto, ProductVariant.class);
            productVariant.setProduct(product.get());
            return modelMapper.map(repo.save(productVariant), ProductVariantDto.class);
        }
        throw new ProductException("Product not found");
    }

    @Override
    public void cacheProductVariant(ProductVariantCache productVariantCache) {
        log.info("Caching product variant with id: {}", productVariantCache.getId());
        String key = "ProductVariant:" + productVariantCache.getId();
        redisTemplate.opsForValue().set(key, productVariantCache);
    }

    @Override
    public List<ProductVariantCache> getAllProductVariantCache() {
        Set<String> keys = redisTemplate.keys("ProductVariant:*");
        return redisTemplate.opsForValue().multiGet(keys);
    }
}
