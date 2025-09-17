package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.exception.InsufficientStockException;
import com.glologistics.inventory.exception.OrderNotFoundException;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.model.Customer;
import com.glologistics.inventory.repository.OrderRepository;
import com.glologistics.inventory.repository.CustomerRepository;
import com.glologistics.inventory.service.OrderService;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public Order generateOrder(Long customerId, Long productId, Integer quantity) {
        Product product = productService.getProduct(productId);
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
        
        if (product.getQuantityInStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalAmount(product.getProductPrice() * quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        order = orderRepository.save(order);
        
        // Update product stock
        productService.updateStock(productId, product.getQuantityInStock() - quantity);
        
        return order;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        
        if (status == Order.OrderStatus.REJECTED) {
            // Return the quantity back to stock
            Product product = order.getProduct();
            productService.updateStock(product.getProductId(), 
                product.getQuantityInStock() + order.getQuantity());
        }
        
        orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot delete order that is not in PENDING status");
        }
        
        // Return the quantity back to stock
        Product product = order.getProduct();
        productService.updateStock(product.getProductId(), 
            product.getQuantityInStock() + order.getQuantity());
            
        orderRepository.delete(order);
    }
}