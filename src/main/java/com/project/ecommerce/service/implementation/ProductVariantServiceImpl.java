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


    private final ModelMapper modelMapper;

    private final ProductVariantRepository repo;

    private final ProductRepository productRepo;

    private final RedisTemplate<String, ProductVariantCache> redisTemplate;

    @Autowired
    public ProductVariantServiceImpl(ModelMapper modelMapper,
                                     ProductVariantRepository repo,
                                     ProductRepository productRepo,
                                     RedisTemplate<String, ProductVariantCache> redisTemplate) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.productRepo = productRepo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ProductVariantDto> getAllProductVariants() {
        return repo.findAll()
                .stream()
                .map(ProductVariantDto::new)
                .toList();
    }

    @Override
    public ProductVariantDto saveProductVariant(ProductVariantDto productVariantDto) {
        Optional<Product> product = productRepo.findById(productVariantDto.getProduct_id());
        log.info("Saving product variant with product id: {}", productVariantDto.getProduct_id());
        if (product.isPresent()) {
            ProductVariant productVariant = modelMapper.map(productVariantDto, ProductVariant.class);
            productVariant.setProduct(product.get());
            return new ProductVariantDto(repo.save(productVariant));
        }
        throw new ProductException("Product not found");
    }

    @Override
    public void cacheProductVariant(ProductVariantCache productVariantCache) {
        log.info("Caching product variant with id: {}", productVariantCache.getProduct_id());
        String key = "ProductVariant:" + productVariantCache.getProduct_id();
        redisTemplate.opsForValue().set(key, productVariantCache);
    }

    @Override
    public List<ProductVariantCache> getAllProductVariantCache(Integer id) {
        Set<String> keys = redisTemplate.keys("ProductVariant:" + id);
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Optional<ProductVariantDto> getProductVariantById(Integer id) {
        return repo.findById(id)
                .map(ProductVariantDto::new);
    }
}
