package com.glologistics.inventory.repository;

import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}