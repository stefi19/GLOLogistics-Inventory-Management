package com.glologistics.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    
    public enum OrderStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}