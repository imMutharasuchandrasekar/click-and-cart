package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.CategoryDTO;
import com.project.sb_ecommerce.DTOs.Responses.CategoryResponse;
import com.project.sb_ecommerce.configurations.Appconstants;
import com.project.sb_ecommerce.model.Category;
import com.project.sb_ecommerce.service.Categoryservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Categorycontroller {

    @Autowired
    private Categoryservice categoryservice;

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categorydto )
    {
        return new ResponseEntity<>( categoryservice.addCategory( categorydto ), HttpStatus.CREATED );
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "offset", defaultValue = Appconstants.OFFSET, required = false) Integer offset,
            @RequestParam(name = "limit", defaultValue = Appconstants.LIMIT, required = false) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = Appconstants.DEFAULT_SORT_BY, required = false ) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Appconstants.DEFAULT_SORT_ORDER, required = false ) String sortOrder
    )
    {
        CategoryResponse response = categoryservice.getAllCategories( offset, limit, sortBy, sortOrder);
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category,
                                                   @PathVariable Long categoryId)
    {
        return new ResponseEntity<>( categoryservice.updateCategory(category, categoryId), HttpStatus.OK );
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory( @PathVariable Long categoryId )
    {
        String status = categoryservice.deleteCategory( categoryId );
        return new ResponseEntity<>( status, HttpStatus.OK );
    }
}
