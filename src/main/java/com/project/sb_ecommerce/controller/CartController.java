package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.CartDTO;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController
{
    @Autowired
    CartService cartService;

    @PostMapping("/products/{productId}/quantity/{quantity}")
    public ResponseEntity<?> addProduct( @PathVariable Long productId, @PathVariable Integer quantity )
    {
        try
        {
            CartDTO cartDTO = cartService.addProductToCart( productId, quantity );
            return new ResponseEntity<>( cartDTO, HttpStatus.OK );
        }
        catch ( APIException apiException )
        {
            return new ResponseEntity<>( apiException.getMessage().toString(), HttpStatus.NOT_FOUND );
        }

    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts()
    {
       List<CartDTO> cartDTOList = cartService.getAllCarts();
       return new ResponseEntity<>( cartDTOList, HttpStatus.OK );
    }

    @GetMapping("user/cart")
    public ResponseEntity<?> getUserCart()
    {
        try
        {
            CartDTO userCartDTO = cartService.getLoggedInUserCart();
            return new ResponseEntity<>( userCartDTO, HttpStatus.OK );
        }
        catch ( APIException apiException )
        {
            return new ResponseEntity<>( apiException.getMessage().toString(), HttpStatus.NOT_FOUND );
        }
        catch ( Exception e )
        {
            return new ResponseEntity<>(  e.getMessage(), HttpStatus.BAD_GATEWAY );
        }
    }

    @PutMapping("/products/{productId}/quantity/{operation}")
    public ResponseEntity<?> updateProductQuantity( @PathVariable Long productId, @PathVariable String operation )
    {
        try
        {
            CartDTO updatedCart = cartService.updateProductQuantity( productId, operation );
            return new ResponseEntity<>( updatedCart, HttpStatus.ACCEPTED );
        }
        catch ( APIException apiException )
        {
            return new ResponseEntity<>( apiException.getMessage().toString(), HttpStatus.OK );
        }
        catch ( Exception e )
        {
            return new ResponseEntity<>(  e.getMessage(), HttpStatus.BAD_GATEWAY );
        }
    }

    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<?> deleteProductFromCart( @PathVariable Long cartId, @PathVariable Long productId )
    {
        String response = cartService.deleteProductFromCart( cartId, productId );
        return new ResponseEntity<>( response, HttpStatus.OK );
    }
}
