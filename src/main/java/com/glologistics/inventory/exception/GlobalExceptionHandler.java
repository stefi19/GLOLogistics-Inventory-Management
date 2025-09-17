package com.glologistics.inventory.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.util.ErrorHandler;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements ErrorHandler {

    @Override
    public void handleError(@NonNull Throwable throwable) {
        String errorMessage;
        
        if (throwable instanceof ProductNotFoundException) {
            errorMessage = "Product not found: " + throwable.getMessage();
        } else if (throwable instanceof InsufficientStockException) {
            errorMessage = "Insufficient stock: " + throwable.getMessage();
        } else if (throwable instanceof OrderNotFoundException) {
            errorMessage = "Order not found: " + throwable.getMessage();
        } else if (throwable instanceof IllegalArgumentException) {
            errorMessage = "Invalid input: " + throwable.getMessage();
        } else if (throwable instanceof IllegalStateException) {
            errorMessage = "Invalid operation: " + throwable.getMessage();
        } else {
            errorMessage = "An unexpected error occurred: " + throwable.getMessage();
        }
        
        System.err.println(errorMessage);
    }
}