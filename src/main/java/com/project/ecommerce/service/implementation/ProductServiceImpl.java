package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductDto;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.repo.ExpenseRepo;
import com.project.ecommerce.repo.OrganizationRepository;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.vo.ProductVariantVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class  ProductServiceImpl implements ProductService {

    private final ModelMapper mapper;

    private final ProductRepository repo;

    private final ExpenseRepo expenseRepo;

    private final OrganizationRepository organizationRepository;

    private final ProductVariantRepository productVariantRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper mapper,
                              ExpenseRepo expenseRepo,
                              ProductRepository repo,
                              OrganizationRepository organizationRepository,
                              ProductVariantRepository productVariantRepository) {
        this.mapper = mapper;
        this.expenseRepo = expenseRepo;
        this.repo = repo;
        this.organizationRepository = organizationRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDto> getAllProducts(String keyword,
                                           Boolean isAvailable,
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

        if (isAvailable != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("available"), isAvailable)
            );
        }

        return repo.findAll(specification, pageRequest)
                .map(ProductDto::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductDto> getProductById(Long id) {
        Optional<Product> optionalProduct = repo.findById(id);
        return optionalProduct.map(ProductDto::new);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        organizationRepository.save(productDto.getOrganization());
        Product product;

        // update case
        if (productDto.getId() != null) {
            product = new Product(productDto);  // map productDto to product
            Product originalProduct = repo.getReferenceById(productDto.getId());

            // set original product's product variants and organization to the updated product
            product.setProductVariants(originalProduct.getProductVariants());
            product.setOrganization(originalProduct.getOrganization());
        } else {  // add case
            product = mapper.map(productDto, Product.class);
        }

        product.setAvailable(false);
        product = repo.save(product);
        if (!productDto.getProductVariants().isEmpty()) {
            saveProductVariants(repo.getReferenceById(product.getId()), product.getProductVariants());
        }
        return new ProductDto(product);
    }

    private void saveProductVariants(Product product, List<ProductVariant> productVariants) {
    	productVariants.forEach(productVariant -> {
            productVariant.setProduct(product);
            if (productVariant.getQuantity() > 0) {
                productVariant.setInStock(true);
            }
            productVariant.setCreatedAt(LocalDate.now());
        });
        List<ProductVariant> pvs = productVariantRepository.saveAll(productVariants);

        // add data to expense table
        pvs.forEach(pv -> expenseRepo.save( Expense.builder()
                .purchasePrice(pv.getPurchasePrice())
                .quantity(pv.getQuantity())
                .total(pv.getPurchasePrice() * pv.getQuantity())
                .createdAt(LocalDate.now())
                .productVariant(pv)
                .build()
        ));
        product.setProductVariants(pvs);
        product.setAvailable(true);
        repo.save(product);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    // to update quantity after an order is placed
    @PreAuthorize("hasAuthority('USER')")
    @Override
    public void updateProductVariantQuantity(Map<Integer, Integer> productQuantityMap) {
        productQuantityMap.forEach((key, value) -> {
            Optional<Integer> productVariant = productVariantRepository.findQuantityById(key);
            if (productVariant.isPresent()) {
                Integer quantity = productVariant.get();
                if (quantity < value) {
                    throw new ProductException("Product quantity is not enough!");
                } else if (quantity.equals(value)) {
                    productVariantRepository.updateInStockById(key, false);
                }
                quantity -= value;
                productVariantRepository.updateQuantityById(key, quantity);
            }
        });
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateProductAvailability(Long id) {
        Product product = repo.getReferenceById(id);
        product.setAvailable(!product.isAvailable());
    }

    @Override
    public Optional<List<ProductVariantVo>> getProductVariantById(Integer id) {
        List<ProductVariant> productVariants = repo.getProductVariantById(id);
        return Optional.of(productVariants.stream().map(ProductVariantVo::new).toList());
    }

}
