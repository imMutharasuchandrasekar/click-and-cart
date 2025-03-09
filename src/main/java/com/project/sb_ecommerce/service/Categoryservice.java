package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.CategoryDTO;
import com.project.sb_ecommerce.DTOs.CategoryResponse;
import com.project.sb_ecommerce.model.Category;

import java.util.List;

public interface Categoryservice {

    CategoryDTO addCategory(CategoryDTO categorydto);

    CategoryResponse getAllCategories( Integer offset, Integer limit, String sortBy, String sortOrder );

    Category updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);
}
