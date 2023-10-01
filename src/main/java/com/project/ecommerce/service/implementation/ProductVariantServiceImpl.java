package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.repo.ExpenseRepo;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductVariantService;
import com.project.ecommerce.vo.ProductVariantVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ModelMapper modelMapper;

    private final ProductVariantRepository productVariantRepository;

    private final ProductRepository productRepo;

    private final ExpenseRepo expenseRepo;

    @Autowired
    public ProductVariantServiceImpl(ModelMapper modelMapper,
                                     ProductVariantRepository productVariantRepository,
                                     ProductRepository productRepo,
                                     ExpenseRepo expenseRepo) {
        this.modelMapper = modelMapper;
        this.productVariantRepository = productVariantRepository;
        this.productRepo = productRepo;
        this.expenseRepo = expenseRepo;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductVariantVo saveProductVariant(ProductVariantDto productVariantDto) {
        Product product = productRepo.getReferenceById(productVariantDto.getProductId());
        int oldQuantity = 0;

        // update case
        if (productVariantDto.getId() != 0) {
            ProductVariant originalProductVariant = productVariantRepository
                                                        .getReferenceById(productVariantDto.getId());

            // set expenses to updated expenses
            productVariantDto.setExpenses(originalProductVariant.getExpenses());
            oldQuantity = originalProductVariant.getQuantity();
            productVariantDto.setUpdatedAt(LocalDate.now());
            productVariantDto.setCreatedAt(originalProductVariant.getCreatedAt());
        }


        // set product to available if it was previously unavailable
        if (!product.isAvailable()) {
            product.setAvailable(true);
            productRepo.save(product);
        }

        ProductVariant productVariant = modelMapper.map(productVariantDto, ProductVariant.class);

        if (productVariant.getQuantity() > 0) {
            productVariant.setInStock(true);
        }
        if (productVariant.getCreatedAt() == null) {
            productVariant.setCreatedAt(LocalDate.now());
        } else {
            productVariant.setUpdatedAt(LocalDate.now());
        }

        productVariant.setProduct(product);
        productVariant.setQuantity(productVariant.getQuantity() + oldQuantity);
        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);

        // save expense to expense table
        saveExpense(savedProductVariant, productVariantDto.getQuantity());
        return new ProductVariantVo(savedProductVariant);
    }

    private void saveExpense(ProductVariant savedProductVariant, Integer quantity) {
        expenseRepo.save(Expense.builder()
                .purchasePrice(savedProductVariant.getPurchasePrice())
                .quantity(quantity)
                .total(savedProductVariant.getPurchasePrice() * quantity)
                .createdAt(LocalDate.now())
                .productVariant(savedProductVariant)
                .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Optional<ProductVariantVo> getProductVariantById(Integer id) {
        return productVariantRepository.findById(id)
                .map(ProductVariantVo::new);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProductVariant(Integer id) {
        productVariantRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Optional<String> getProductVariantImageUrl(Integer id) {
        return productVariantRepository.findImageUrlById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Long getProductIdByPvId(Integer id) {
        return productVariantRepository.findProductIdById(id);
    }
}
