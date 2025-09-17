package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.model.Customer;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.repository.CustomerRepository;
import com.glologistics.inventory.repository.OrderRepository;
import com.glologistics.inventory.service.CustomerService;
import com.glologistics.inventory.service.OrderService;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Override
    public List<Product> viewAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    @Transactional
    public Order orderProduct(Long customerId, Long productId, Integer quantity) {
        // Verify customer exists
        getCustomer(customerId);
        return orderService.generateOrder(customerId, productId, quantity);
    }

    @Override
    public List<Order> viewAllOrders(Long customerId) {
        Customer customer = getCustomer(customerId);
        return orderRepository.findByCustomer(customer);
    }

    @Override
    @Transactional
    public void deleteOrder(Long customerId, Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (!order.getCustomer().getCustomerId().equals(customerId)) {
            throw new IllegalStateException("Order does not belong to customer: " + customerId);
        }
        orderService.deleteOrder(orderId);
    }

    @Override
    @Transactional
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }
}