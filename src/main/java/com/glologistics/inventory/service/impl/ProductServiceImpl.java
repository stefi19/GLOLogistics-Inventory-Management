package com.glologistics.inventory.service.impl;

import com.glologistics.inventory.exception.ProductNotFoundException;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.repository.ProductRepository;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getProductId())) {
            throw new ProductNotFoundException("Product not found with ID: " + product.getProductId());
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        product.setQuantityInStock(quantity);
        productRepository.save(product);
    }
}