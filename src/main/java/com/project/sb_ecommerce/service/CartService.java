package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.CartDTO;

import java.util.List;

public interface CartService
{
    CartDTO addProductToCart( Long productId, Integer quantitiy );

    List<CartDTO> getAllCarts();

    CartDTO getLoggedInUserCart();

    CartDTO updateProductQuantity( Long productId, String operation );

    String deleteProductFromCart(Long cartId, Long productId);
}
