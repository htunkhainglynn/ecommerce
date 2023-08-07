package com.project.ecommerce.service.implementation;

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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductVariantRepository repo;

    @Autowired
    ProductRepository productRepo;

    @Override
    public ProductVariantDto createProductVariant(ProductVariantDto productVariantDto) {
        Optional<Product> product = productRepo.findById(productVariantDto.getProduct_id());
        log.info("product id", productVariantDto.getProduct_id());
        if (product.isPresent()) {
            ProductVariant productVariant = modelMapper.map(productVariantDto, ProductVariant.class);
            productVariant.setProduct(product.get());
            return modelMapper.map(repo.save(productVariant), ProductVariantDto.class);
        }
        throw new ProductException("Product not found");
    }
}
