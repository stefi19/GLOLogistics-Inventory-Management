package com.glologistics.inventory.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(length = 1000)
    private String productDescription;

    @Column(nullable = false)
    private Double productPrice;

    @Column(nullable = false)
    private Integer quantityInStock;
}