package com.glologistics.inventory.ui;

import com.glologistics.inventory.model.Customer;
import com.glologistics.inventory.model.Order;
import com.glologistics.inventory.model.Product;
import com.glologistics.inventory.service.CustomerService;
import com.glologistics.inventory.service.OrderService;
import com.glologistics.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    private final Scanner scanner = new Scanner(System.in);
    private Customer currentCustomer;

    @Override
    public void run(String... args) {
        System.out.println("Welcome to GLOLogistics Inventory Management System");
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Login as Customer");
            System.out.println("2. Login as Admin");
            System.out.println("3. Register as New Customer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    customerLogin();
                    break;
                case 2:
                    adminMenu();
                    break;
                case 3:
                    registerCustomer();
                    break;
                case 4:
                    System.out.println("Thank you for using GLOLogistics IMS. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void customerLogin() {
        System.out.print("Enter customer ID: ");
        Long customerId = Long.parseLong(scanner.nextLine());
        try {
            currentCustomer = customerService.getCustomer(customerId);
            customerMenu();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registerCustomer() {
        System.out.println("\n=== Customer Registration ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer();
        customer.setCustomerName(name);
        customer.setContactNumber(contact);
        customer.setAddress(address);

        try {
            customer = customerService.addCustomer(customer);
            System.out.println("Registration successful! Your customer ID is: " + customer.getCustomerId());
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private void customerMenu() {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("Welcome, " + currentCustomer.getCustomerName());
            System.out.println("1. View All Products");
            System.out.println("2. Place Order");
            System.out.println("3. View My Orders");
            System.out.println("4. Cancel Order");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    displayAllProducts();
                    break;
                case 2:
                    placeOrder();
                    break;
                case 3:
                    viewCustomerOrders();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    currentCustomer = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Product");
            System.out.println("3. View All Products");
            System.out.println("4. View All Orders");
            System.out.println("5. Update Order Status");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    displayAllProducts();
                    break;
                case 4:
                    viewAllOrders();
                    break;
                case 5:
                    updateOrderStatus();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayAllProducts() {
        System.out.println("\n=== Available Products ===");
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
        System.out.println("-".repeat(50));
        for (Product product : products) {
            System.out.printf("%-5d %-20s $%-9.2f %-10d%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getQuantityInStock());
        }
    }

    private void addProduct() {
        System.out.println("\n=== Add New Product ===");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter initial stock quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        Product product = new Product();
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPrice(price);
        product.setQuantityInStock(quantity);

        try {
            productService.addProduct(product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        Long productId = Long.parseLong(scanner.nextLine());

        try {
            Product product = productService.getProduct(productId);
            System.out.println("Current stock: " + product.getQuantityInStock());
            System.out.print("Enter new stock quantity: ");
            int newQuantity = Integer.parseInt(scanner.nextLine());
            
            product.setQuantityInStock(newQuantity);
            productService.updateProduct(product);
            System.out.println("Product stock updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private void placeOrder() {
        displayAllProducts();
        System.out.print("\nEnter product ID to order: ");
        Long productId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        try {
            Order order = customerService.orderProduct(currentCustomer.getCustomerId(), productId, quantity);
            System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private void viewCustomerOrders() {
        System.out.println("\n=== My Orders ===");
        List<Order> orders = customerService.viewAllOrders(currentCustomer.getCustomerId());
        displayOrders(orders);
    }

    private void viewAllOrders() {
        System.out.println("\n=== All Orders ===");
        List<Order> orders = orderService.getAllOrders();
        displayOrders(orders);
    }

    private void displayOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.printf("%-5s %-10s %-10s %-8s %-10s %-12s%n",
                "ID", "Customer", "Product", "Quantity", "Total", "Status");
        System.out.println("-".repeat(60));
        
        for (Order order : orders) {
            System.out.printf("%-5d %-10d %-10d %-8d $%-9.2f %-12s%n",
                    order.getOrderId(),
                    order.getCustomer().getCustomerId(),
                    order.getProduct().getProductId(),
                    order.getQuantity(),
                    order.getTotalAmount(),
                    order.getStatus());
        }
    }

    private void cancelOrder() {
        viewCustomerOrders();
        System.out.print("\nEnter order ID to cancel: ");
        Long orderId = Long.parseLong(scanner.nextLine());

        try {
            customerService.deleteOrder(currentCustomer.getCustomerId(), orderId);
            System.out.println("Order cancelled successfully!");
        } catch (Exception e) {
            System.out.println("Error cancelling order: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        viewAllOrders();
        System.out.print("\nEnter order ID to update: ");
        Long orderId = Long.parseLong(scanner.nextLine());
        System.out.println("Select new status:");
        System.out.println("1. APPROVED");
        System.out.println("2. REJECTED");
        System.out.print("Enter choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        Order.OrderStatus newStatus;
        if (choice == 1) {
            newStatus = Order.OrderStatus.APPROVED;
        } else if (choice == 2) {
            newStatus = Order.OrderStatus.REJECTED;
        } else {
            System.out.println("Invalid choice!");
            return;
        }

        try {
            orderService.updateOrderStatus(orderId, newStatus);
            System.out.println("Order status updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
        }
    }
}