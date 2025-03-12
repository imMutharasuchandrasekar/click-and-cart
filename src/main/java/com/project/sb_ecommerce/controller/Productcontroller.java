package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import com.project.sb_ecommerce.DTOs.Responses.ProductResponse;
import com.project.sb_ecommerce.service.Productservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class Productcontroller {

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
    public ResponseEntity<ProductResponse> getAllProducts()
    {
        ProductResponse response = productservice.getAllProducts();
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value= "public/categories/{categoryId}/products", method = RequestMethod.GET )
    public ResponseEntity<ProductResponse> getProductByCategory( @PathVariable Long categoryId )
    {
        ProductResponse response = productservice.getProudctsByCategory( categoryId );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value = "public/products/keyword/{keyword}", method = RequestMethod.GET )
    public ResponseEntity<ProductResponse> getProductByKeyword( @PathVariable String keyword )
    {
        ProductResponse response = productservice.searchByKeyword( keyword );
        return new ResponseEntity<>( response, HttpStatus.FOUND );
    }

    @GetMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage( @PathVariable Long productId,
                                                               @RequestParam(name="image") MultipartFile image ) throws IOException
    {
        ProductDTO updatedProduct = productservice.updateProductImage( productId, image );
        return new ResponseEntity<>( updatedProduct, HttpStatus.OK );
    }
}
