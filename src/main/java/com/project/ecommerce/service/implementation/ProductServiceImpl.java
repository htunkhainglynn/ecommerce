package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.*;
import com.project.ecommerce.entitiy.Brand;
import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.Size;
import com.project.ecommerce.paginated.response.PaginatedResponse;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ModelMapper mapper;
    @Autowired
    private ProductRepository repo;

    @Override
    public Page<ProductDto> getAllProducts(
            String name,
            String category,
            List<String> brands,
            Integer minPrice,
            Integer maxPrice,
            List<String> productSizes,
            List<Integer> ratings,
            Optional<Integer> page,
            Optional<Integer> size) {

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(5),
                Sort.by("price").descending());  // custom rating

        Specification<Product> specification = (root, query, cb) -> {
            return cb.conjunction();  // select * from product;
        };

        if(StringUtils.hasLength(name)) {
            specification = specification.and((root, query, cb)
                    -> cb.like(cb.lower(root.get("name")), name.toLowerCase().concat("%")));
        }

        if(StringUtils.hasLength(category)) {
            specification = specification.and((root, query, cb)
                    -> cb.like(cb.lower(root.get("category").get("name")),
                                category.toLowerCase().concat("%")));
        }

        if(null != brands) {
            specification = specification.and((root, query, cb)
                    -> root.get("brand").get("name").in(brands));
        }

        if (minPrice != null) {
            specification = specification.and((root, query, cb)
                    -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            specification = specification.and((root, query, cb)
                    -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (null != productSizes) {
            specification = specification.and((root, query, cb)
                    -> root.get("sizes").in(productSizes));
        }

        if (null != ratings) {
            specification = specification.and((root, query, cb)
                    -> root.get("rating").in(ratings));
        }

        return repo.findAll(specification, pageRequest)
                .map(product -> mapper.map(product, ProductDto.class));
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
