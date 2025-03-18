package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import com.project.sb_ecommerce.DTOs.Responses.PaginatedProductResponse;
import com.project.sb_ecommerce.DTOs.Responses.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface Productservice {
    ProductDTO addProduct( ProductDTO productDTO, long categoryId );

    PaginatedProductResponse getAllProducts( Integer offset, Integer limit, String sortBy, String sortOrder );

    PaginatedProductResponse getProductsByCategory( Long categoryId, Integer offset, Integer limit, String sortBy, String sortOrder );

    PaginatedProductResponse searchByKeyword( String keyword, Integer offset, Integer limit, String sortBy, String sortOrder );

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    String deleteProduct( Long productId );

    ProductResponse updateProduct(Long productId, ProductDTO dto);
}
