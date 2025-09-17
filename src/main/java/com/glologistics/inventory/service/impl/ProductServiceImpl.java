package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.exception.ProductNotFoundException;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong productIdGenerator = new AtomicLong(1);

    @Override
    public Product addProduct(Product product) {
        product.setProductId(productIdGenerator.getAndIncrement());
        products.put(product.getProductId(), product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        if (!products.containsKey(product.getProductId())) {
            throw new ProductNotFoundException("Product not found with ID: " + product.getProductId());
        }
        products.put(product.getProductId(), product);
        return product;
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!products.containsKey(productId)) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        products.remove(productId);
    }

    @Override
    public Product getProduct(Long productId) {
        Product product = products.get(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        product.setQuantityInStock(quantity);
        products.put(productId, product);
    }
}