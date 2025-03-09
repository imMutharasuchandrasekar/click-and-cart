package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.ProductDTO;
import com.project.sb_ecommerce.DTOs.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface Productservice {
    ProductDTO addProduct( ProductDTO productDTO, long categoryId );

    ProductResponse getAllProducts();

    ProductResponse getProudctsByCategory(Long categoryId);

    ProductResponse searchByKeyword(String keyword);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
