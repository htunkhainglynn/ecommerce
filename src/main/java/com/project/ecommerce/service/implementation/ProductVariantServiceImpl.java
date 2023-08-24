package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.dto.ProductVariantVo;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductVariantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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
    public List<ProductVariantVo> getAllProductVariants() {
        return repo.findAll()
                .stream()
                .map(ProductVariantVo::new)
                .toList();
    }

    @Override
    public ProductVariantVo saveProductVariant(ProductVariantDto productVariantDto) {
        Optional<Product> product = productRepo.findById(productVariantDto.getProduct_id());
        if (product.isPresent()) {
            ProductVariant productVariant = modelMapper.map(productVariantDto, ProductVariant.class);
            productVariant.setProduct(product.get());
            return new ProductVariantVo(repo.save(productVariant));
        }
        throw new ProductException("Product not found");
    }

    @Override
    public void cacheProductVariant(ProductVariantCache productVariantCache) {
        String key = "ProductVariant:" + productVariantCache.getProduct_id();
        redisTemplate.opsForValue().set(key, productVariantCache);
    }

    @Override
    public List<ProductVariantCache> getAllProductVariantCache(Integer id) {
        Set<String> keys = redisTemplate.keys("ProductVariant:" + id);
        assert keys != null;  // key might be null
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Optional<ProductVariantVo> getProductVariantById(Integer id) {
        return repo.findById(id)
                .map(ProductVariantVo::new);
    }
}
