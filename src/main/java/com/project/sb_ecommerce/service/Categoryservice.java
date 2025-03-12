package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.CategoryDTO;
import com.project.sb_ecommerce.DTOs.Responses.CategoryResponse;
import com.project.sb_ecommerce.model.Category;

public interface Categoryservice {

    CategoryDTO addCategory(CategoryDTO categorydto);

    CategoryResponse getAllCategories( Integer offset, Integer limit, String sortBy, String sortOrder );

    Category updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);
}
