package com.project.ecommerce.repo;

import com.project.ecommerce.entitiy.Brand;
import com.project.ecommerce.entitiy.Category;
import com.project.ecommerce.entitiy.Product;
import com.project.ecommerce.entitiy.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
