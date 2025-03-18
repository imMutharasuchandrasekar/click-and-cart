package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import com.project.sb_ecommerce.DTOs.Responses.PaginatedProductResponse;
import com.project.sb_ecommerce.DTOs.Responses.ProductResponse;
import com.project.sb_ecommerce.configurations.Appconstants;
import com.project.sb_ecommerce.service.Productservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class Productcontroller
{
    @Autowired
    Productservice productservice;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO productDTO,
             @PathVariable long categoryId )
    {
        ProductDTO savedproduct = productservice.addProduct( productDTO, categoryId );
        return new ResponseEntity<>( savedproduct, HttpStatus.CREATED );
    }

    @GetMapping("public/products")
    public ResponseEntity<PaginatedProductResponse> getAllProducts(
            @RequestParam(name = "offset", defaultValue = Appconstants.OFFSET, required = false) Integer offset,
            @RequestParam(name = "limit", defaultValue = Appconstants.LIMIT, required = false) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Appconstants.DEFAULT_SORT_ORDER, required = false) String sortOrder )
    {
        PaginatedProductResponse response = productservice.getAllProducts( offset, limit, sortBy, sortOrder );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value= "public/categories/{categoryId}/products", method = RequestMethod.GET )
    public ResponseEntity<PaginatedProductResponse> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "offset", defaultValue = Appconstants.OFFSET, required = false) Integer offset,
            @RequestParam(name = "limit", defaultValue = Appconstants.LIMIT, required = false) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Appconstants.DEFAULT_SORT_ORDER, required = false) String sortOrder )
    {
        PaginatedProductResponse response = productservice.getProductsByCategory( categoryId, offset, limit, sortBy, sortOrder );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value = "public/products/keyword/{keyword}", method = RequestMethod.GET )
    public ResponseEntity<PaginatedProductResponse> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "offset", defaultValue = Appconstants.OFFSET, required = false) Integer offset,
            @RequestParam(name = "limit", defaultValue = Appconstants.LIMIT, required = false) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Appconstants.DEFAULT_SORT_ORDER, required = false) String sortOrder )
    {
        PaginatedProductResponse response = productservice.searchByKeyword( keyword, offset, limit, sortBy, sortOrder );
        return new ResponseEntity<>( response, HttpStatus.FOUND );
    }

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct( @RequestBody ProductDTO dto, @PathVariable Long productId )
    {
        ProductResponse response = productservice.updateProduct( productId, dto );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage( @PathVariable Long productId,
                                                               @RequestParam(name="image") MultipartFile image ) throws IOException
    {
        ProductDTO updatedProduct = productservice.updateProductImage( productId, image );
        return new ResponseEntity<>( updatedProduct, HttpStatus.OK );
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct( @PathVariable Long productId )
    {
        try{
            String response = productservice.deleteProduct( productId );
            return new ResponseEntity<>( response, HttpStatus.OK );
        } catch ( Exception e ) {
            return new ResponseEntity<>( e.getMessage(), HttpStatus.BAD_GATEWAY );
        }
    }
}
