package com.glologistics.inventory.service;

import com.glologistics.inventory.model.Customer;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import java.util.List;

public interface CustomerService {
    List<Product> viewAllProducts();
    Order orderProduct(Long customerId, Long productId, Integer quantity);
    List<Order> viewAllOrders(Long customerId);
    void deleteOrder(Long customerId, Long orderId);
    Customer addCustomer(Customer customer);
    Customer getCustomer(Long customerId);
}