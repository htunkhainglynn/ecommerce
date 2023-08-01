package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ModelMapper mapper;
    @Autowired
    private ProductRepository repo;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = repo.findAll();
        return products.stream().map(product -> mapper.map(product, ProductDto.class)).toList();
    }

    @Override
    public Optional<ProductDto> getProductById(Long id) {
        Optional<Product> optionalProduct = repo.findById(id);
        Optional<ProductDto> productDto = optionalProduct.map(product -> mapper.map(product, ProductDto.class));
        return productDto;
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        return mapper.map(repo.save(product), ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
