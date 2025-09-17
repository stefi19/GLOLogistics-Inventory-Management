package com.glologistics.inventory.service;

import com.glologistics.inventory.model.Product;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long productId);
    Product getProduct(Long productId);
    List<Product> getAllProducts();
    void updateStock(Long productId, Integer quantity);
}