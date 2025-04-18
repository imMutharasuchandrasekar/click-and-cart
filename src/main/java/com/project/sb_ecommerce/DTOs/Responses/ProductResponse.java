package com.project.sb_ecommerce.DTOs.Responses;

import com.project.sb_ecommerce.DTOs.Requests.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse
{
    List<ProductDTO> contents = new ArrayList<>();
}
