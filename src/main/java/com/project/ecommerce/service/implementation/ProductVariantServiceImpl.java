package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.ExpenseDto;
import com.project.ecommerce.dto.ProductVariantCache;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.entitiy.Expense;
import com.project.ecommerce.repo.BalanceRepo;
import com.project.ecommerce.repo.ExpenseRepo;
import com.project.ecommerce.service.BalanceService;
import com.project.ecommerce.vo.ProductVariantVo;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.ProductVariant;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.repo.ProductRepository;
import com.project.ecommerce.repo.ProductVariantRepository;
import com.project.ecommerce.service.ProductVariantService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ModelMapper modelMapper;

    private final ProductVariantRepository productVariantRepository;

    private final ProductRepository productRepo;

    private final ExpenseRepo expenseRepo;

    private final BalanceRepo balanceRepo;

    @Autowired
    public ProductVariantServiceImpl(ModelMapper modelMapper,
                                     ProductVariantRepository productVariantRepository,
                                     ProductRepository productRepo,
                                     ExpenseRepo expenseRepo,
                                     BalanceRepo balanceRepo) {
        this.modelMapper = modelMapper;
        this.productVariantRepository = productVariantRepository;
        this.productRepo = productRepo;
        this.expenseRepo = expenseRepo;
        this.balanceRepo = balanceRepo;
    }

    @Override
    public ProductVariantVo saveProductVariant(ProductVariantDto productVariantDto) {
        Product product = productRepo.getReferenceById(productVariantDto.getProduct_id());
        int oldQuantity = 0;

        // update case
        if (productVariantDto.getId() != 0) {
            ProductVariant originalProductVariant = productVariantRepository
                                                        .getReferenceById(productVariantDto.getId());

            // set expenses to updated expenses
            productVariantDto.setExpenses(originalProductVariant.getExpenses());
            oldQuantity = originalProductVariant.getQuantity();
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
    public Optional<ProductVariantVo> getProductVariantById(Integer id) {
        return productVariantRepository.findById(id)
                .map(ProductVariantVo::new);
    }

    @Override
    public void deleteProductVariant(Integer id) {
        productVariantRepository.deleteById(id);
    }

    @Override
    public Optional<String> getProductVariantImageUrl(Integer id) {
        return productVariantRepository.findImageUrlById(id);
    }

    @Override
    public void updateExpenseHistory(Integer id, ExpenseDto expenseDto) {
        double purchasePrice = expenseDto.getPurchasePrice();
        int quantity = expenseDto.getQuantity();
        double oldTotal = expenseRepo.getTotalById(id);
        double newTotal = purchasePrice * quantity;
        double difference = newTotal - oldTotal;
        expenseRepo.updateExpenseHistory(id, purchasePrice, quantity, newTotal);

        // update propagated changes in balance table
        // day propagation
        if (!expenseDto.getCreatedAt().equals(LocalDate.now())) {
            balanceRepo.updateExpenses(expenseDto.getCreatedAt(), difference);
        }

        // month propagation
        if (!expenseDto.getCreatedAt().getMonth().equals(LocalDate.now().getMonth())) {
            LocalDate endOfMonth = expenseDto.getCreatedAt().withDayOfMonth(expenseDto.getCreatedAt().lengthOfMonth());
            balanceRepo.updateExpenses(endOfMonth, difference);
        }

        // year propagation
        if (expenseDto.getCreatedAt().getYear() != LocalDate.now().getYear()) {
            LocalDate endOfYear = expenseDto.getCreatedAt().withDayOfYear(expenseDto.getCreatedAt().lengthOfYear());
            balanceRepo.updateExpenses(endOfYear, difference);
        }
    }

    @Override
    public Long getProductIdByPvId(Integer id) {
        return productVariantRepository.findProductIdById(id);
    }
}
