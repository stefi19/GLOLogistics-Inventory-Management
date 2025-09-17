package com.glologistics.inventory.service;

import com.glologistics.inventory.model.Order;
import java.util.List;

public interface OrderService {
    Order generateOrder(Long customerId, Long productId, Integer quantity);
    void updateOrderStatus(Long orderId, Order.OrderStatus status);
    Order getOrder(Long orderId);
    List<Order> getAllOrders();
    void deleteOrder(Long orderId);
}