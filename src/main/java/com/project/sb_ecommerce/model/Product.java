package com.project.sb_ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long productId;
    private String productName;
    private String productImage;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    // Unidirectional
    @ManyToOne
    @JoinColumn(name = "categoryid_fk")
    private Category productCategory;

    // Bidirectional
    // FK should reside on 'many' side (product) & should own the relationship.
    @ManyToOne
    @JoinColumn(name = "seller_id_userfk")
    private User user;
}
