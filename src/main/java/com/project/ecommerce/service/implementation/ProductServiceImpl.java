package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.repo.OrganizationRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;

    private final ProductRepository repo;

    private final OrganizationRepository organizationRepository;

    private final ProductVariantRepository productVariantRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper mapper,
                              ProductRepository repo,
                              OrganizationRepository organizationRepository,
                              ProductVariantRepository productVariantRepository) {
        this.mapper = mapper;
        this.repo = repo;
        this.organizationRepository = organizationRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
        saveProductVariants(repo.getReferenceById(product.getId()), product.getProductVariants());
        return mapper.map(product, ProductDto.class);
    }

    private void saveProductVariants(Product product, List<ProductVariant> productVariants) {
    	productVariants.forEach(productVariant -> productVariant.setProduct(product));
        productVariantRepository.saveAll(productVariants);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

}
