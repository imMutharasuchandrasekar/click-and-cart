package com.project.sb_ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    // Bidirection and owner of relation.
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Bidirection and owner of relation.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private double productPrice;

    public CartItem(Cart cartobj, Product prod, Integer quantity, double productPrice )
    {
        cart = cartobj;
        product = prod;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }
}

