package com.project.sb_ecommerce.DTOs;

import com.project.sb_ecommerce.model.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private long productId;
    private String productName;
    private String productImage;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
