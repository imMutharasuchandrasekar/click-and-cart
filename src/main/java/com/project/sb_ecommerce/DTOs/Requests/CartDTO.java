package com.project.sb_ecommerce.DTOs.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO
{
    private Double totalPrice = 0.0;
    private List<ProductDTO> products = new ArrayList<>();
}