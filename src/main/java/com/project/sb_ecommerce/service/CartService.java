package com.project.sb_ecommerce.service;

import com.project.sb_ecommerce.DTOs.Requests.CartDTO;
import com.project.sb_ecommerce.DTOs.Responses.CartResponse;

import java.util.List;

public interface CartService
{
    CartDTO addProductToCart( Long productId, Integer quantitiy );

    CartResponse getAllCarts(Integer offset, Integer limit, String sortBy, String sortOrder );

    CartDTO getLoggedInUserCart();

    CartDTO updateProductQuantity( Long productId, String operation );

    String deleteProductFromCart( Long cartId, Long productId );
}
