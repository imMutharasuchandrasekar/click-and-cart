package com.project.sb_ecommerce.repository;

import com.project.sb_ecommerce.model.Category;
import com.project.sb_ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductCategoryOrderByPriceAsc(Category category);

    Page<Product> findByProductNameLikeIgnoreCase( String keyword, Pageable Pageable );

    Page<Product> findByProductCategory(Category category, Pageable Pageable );
}
