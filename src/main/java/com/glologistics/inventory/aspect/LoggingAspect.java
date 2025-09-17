package com.glologistics.inventory.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.glologistics.inventory.service.*.*(..))")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        logger.info("Executing {} with arguments: {}", 
            joinPoint.getSignature().getName(), 
            joinPoint.getArgs());
    }

    @AfterReturning(
        pointcut = "execution(* com.glologistics.inventory.service.*.*(..))",
        returning = "result")
    public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
        logger.info("{} executed successfully", 
            joinPoint.getSignature().getName());
    }

    @AfterThrowing(
        pointcut = "execution(* com.glologistics.inventory.service.*.*(..))",
        throwing = "error")
    public void logServiceError(JoinPoint joinPoint, Throwable error) {
        logger.error("Error in {}: {}", 
            joinPoint.getSignature().getName(), 
            error.getMessage());
    }

    // Specific logging for order-related operations
    @AfterReturning(
        pointcut = "execution(* com.glologistics.inventory.service.OrderService.generateOrder(..))",
        returning = "result")
    public void logOrderCreation(JoinPoint joinPoint, Object result) {
        logger.info("New order created: {}", result);
    }

    @AfterReturning(
        pointcut = "execution(* com.glologistics.inventory.service.OrderService.updateOrderStatus(..))")
    public void logOrderStatusUpdate(JoinPoint joinPoint) {
        logger.info("Order status updated - OrderId: {}, New Status: {}", 
            joinPoint.getArgs()[0], 
            joinPoint.getArgs()[1]);
    }

    // Specific logging for product inventory changes
    @AfterReturning(
        pointcut = "execution(* com.glologistics.inventory.service.ProductService.updateStock(..))")
    public void logStockUpdate(JoinPoint joinPoint) {
        logger.info("Product stock updated - ProductId: {}, New Quantity: {}", 
            joinPoint.getArgs()[0], 
            joinPoint.getArgs()[1]);
    }
}