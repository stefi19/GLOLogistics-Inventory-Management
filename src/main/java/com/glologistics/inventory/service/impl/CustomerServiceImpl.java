package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.model.Customer;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.service.CustomerService;
import com.glologistics.inventory.service.OrderService;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<Long, Customer> customers = new ConcurrentHashMap<>();
    private final AtomicLong customerIdGenerator = new AtomicLong(1);

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<Product> viewAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public Order orderProduct(Long customerId, Long productId, Integer quantity) {
        // Verify customer exists
        getCustomer(customerId);
        return orderService.generateOrder(customerId, productId, quantity);
    }

    @Override
    public List<Order> viewAllOrders(Long customerId) {
        // Verify customer exists
        getCustomer(customerId);
        return orderService.getAllOrders().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long customerId, Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (!order.getCustomerId().equals(customerId)) {
            throw new IllegalStateException("Order does not belong to customer: " + customerId);
        }
        orderService.deleteOrder(orderId);
    }

    @Override
    public Customer addCustomer(Customer customer) {
        customer.setCustomerId(customerIdGenerator.getAndIncrement());
        customers.put(customer.getCustomerId(), customer);
        return customer;
    }

    @Override
    public Customer getCustomer(Long customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }
        return customer;
    }
}