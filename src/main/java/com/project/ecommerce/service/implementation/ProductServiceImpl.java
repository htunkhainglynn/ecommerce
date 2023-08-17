package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.repo.OrganizationRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ModelMapper mapper;
    @Autowired
    private ProductRepository repo;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    /*
    @Override
    public Page<ProductDto> getAllProducts(
            String name,
            String category,
            List<String> brands,
            String color,
            Integer minPrice,
            Integer maxPrice,
            List<String> productSizes,
            List<Double> ratings,
            Optional<Integer> page,
            Optional<Integer> size) {

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(5));  // custom rating

        Specification<Product> specification = (root, query, cb) ->
//                cb.like(cb.lower(root.get("code")), "%" + "#0");
                  cb.conjunction();

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

        if (StringUtils.hasLength(color)) {
            specification = specification.and((root, query, cb)
                    -> cb.like(cb.lower(root.get("color")), color.toLowerCase().concat("%")));
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
            double minRating = ratings.stream().min(Double::compareTo).get();
            double maxRating = ratings.stream().max(Double::compareTo).get();
            specification = specification.and((root, query, cb) ->{

                Subquery<Double> subquery = query.subquery(Double.class);  // select -> type: Double (single value)
                Root<Review> subRoot = subquery.from(Review.class); // from review r
                subquery.select(cb.avg(subRoot.get("rating").as(Double.class))); // complete select -> select avg(rating)
                subquery.where(cb.equal(subRoot.get("product"), root)); // where r.product_id = p.id(root is product)

                return cb.between(subquery.getSelection(), minRating, maxRating); // subquery.getSelection() gets avg
            });
        }

        return repo.findAll(specification, pageRequest)
                .map(product -> mapper.map(product, ProductDto.class));
    }
     */


    @Override
    public Page<ProductDto> getAllProducts(String keyword,
                                           boolean isAvailable,
                                           Optional<Integer> page,
                                           Optional<Integer> size) {

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(10));

        Specification<Product> specification = (root, query, cb) -> cb.conjunction();

        if (StringUtils.hasLength(keyword)) {
            specification = specification.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), keyword.toLowerCase().concat("%")),
                            cb.like(cb.lower(root.get("organization").get("vendor")), keyword.toLowerCase().concat("%")),
                            cb.like(cb.lower(root.get("organization").get("category")), keyword.toLowerCase().concat("%"))
                    )
            );
        }

        if (isAvailable) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("available"), true)
            );
        }

        if(!isAvailable) {
            	specification = specification.and((root, query, cb) ->
                        cb.equal(root.get("available"), false)
                );
        }

        return repo.findAll(specification, pageRequest)
                .map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public Optional<ProductDto> getProductById(Long id) {
        Optional<Product> optionalProduct = repo.findById(id);
        return optionalProduct.map(product -> mapper.map(product, ProductDto.class));
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        organizationRepository.save(productDto.getOrganization());
        Product product = mapper.map(productDto, Product.class);
        product = repo.save(product);
        saveProductVariants(product, product.getProductVariants());
        return mapper.map(product, ProductDto.class);
    }

    private void saveProductVariants(Product product, List<ProductVariant> productVariants) {
    	productVariants.forEach(productVariant -> productVariant.setProduct(product));
    	productVariants.forEach(productVariantRepository::save);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

}
