package com.glologistics.inventory.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Override
    public String toString() {
        return String.format("Order{id=%d, customerId=%d, productId=%d, quantity=%d, total=%.2f, date=%s, status=%s}", 
            orderId, 
            customer != null ? customer.getCustomerId() : null,
            product != null ? product.getProductId() : null,
            quantity, 
            totalAmount, 
            orderDate, 
            status);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    public enum OrderStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}