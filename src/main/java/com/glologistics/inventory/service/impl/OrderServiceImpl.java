package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.exception.InsufficientStockException;
import com.glologistics.inventory.exception.OrderNotFoundException;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.service.OrderService;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    @Autowired
    private ProductService productService;

    @Override
    public Order generateOrder(Long customerId, Long productId, Integer quantity) {
        Product product = productService.getProduct(productId);
        
        if (product.getQuantityInStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }

        Order order = new Order();
        order.setOrderId(orderIdGenerator.getAndIncrement());
        order.setCustomerId(customerId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalAmount(product.getProductPrice() * quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        orders.put(order.getOrderId(), order);
        
        // Update product stock
        productService.updateStock(productId, product.getQuantityInStock() - quantity);
        
        return order;
    }

    @Override
    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        
        if (status == Order.OrderStatus.REJECTED) {
            // Return the quantity back to stock
            Product product = productService.getProduct(order.getProductId());
            productService.updateStock(order.getProductId(), 
                product.getQuantityInStock() + order.getQuantity());
        }
        
        orders.put(orderId, order);
    }

    @Override
    public Order getOrder(Long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot delete order that is not in PENDING status");
        }
        
        // Return the quantity back to stock
        Product product = productService.getProduct(order.getProductId());
        productService.updateStock(order.getProductId(), 
            product.getQuantityInStock() + order.getQuantity());
            
        orders.remove(orderId);
    }
}