package org.strategy;

import javafx.application.Application;

/**
 * Main class that demonstrates the Strategy Pattern in an E-Commerce Discount Application.
 * 
 * The Strategy Pattern is a behavioral design pattern that:
 * 1. Defines a family of algorithms (discount strategies)
 * 2. Encapsulates each algorithm in separate classes
 * 3. Makes them interchangeable at runtime
 * 
 * This application demonstrates:
 * - DiscountStrategy interface: Defines the contract for all discount algorithms
 * - Concrete strategies: NoDiscount, PercentageDiscount, FixedAmountDiscount, BuyOneGetOneDiscount
 * - Context class: ShoppingCart that uses the strategies
 * - Dynamic strategy switching: Users can change discount strategies at runtime
 * 
 * Real-world applications of Strategy Pattern:
 * - Payment processing (Credit Card, PayPal, Bank Transfer)
 * - Compression algorithms (ZIP, RAR, GZIP)
 * - Sorting algorithms (QuickSort, MergeSort, BubbleSort)
 * - Navigation systems (Fastest route, Shortest route, Scenic route)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting E-Commerce Discount Application...");
        System.out.println("This application demonstrates the Strategy Pattern with JavaFX GUI and user authentication.");
        System.out.println("\n=== Strategy Pattern Demo ===");
        System.out.println("The Strategy Pattern allows you to:");
        System.out.println("1. Define multiple discount algorithms");
        System.out.println("2. Switch between them at runtime");
        System.out.println("3. Add new strategies without modifying existing code");
        System.out.println("\n=== New Features ===");
        System.out.println("✓ User Authentication (Login/Register)");
        System.out.println("✓ Modern Professional UI Design");
        System.out.println("✓ Enhanced User Experience with Animations");
        System.out.println("✓ Responsive Design and Better Visual Feedback");
        System.out.println("\nDemo Accounts Available:");
        System.out.println("• Admin: admin / admin123");
        System.out.println("• Customer: john_doe / password123");
        System.out.println("• Manager: manager / manager123");
        System.out.println("\nLaunching Login Application...");
        
        // Launch the login/register application first
        Application.launch(LoginRegisterApp.class, args);
    }
}