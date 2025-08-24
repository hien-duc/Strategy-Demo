package org.strategy;

import java.util.Objects;

/**
 * Represents a product in the e-commerce system.
 * This class encapsulates the basic properties of a product including name, price, and category.
 */
public class Product {
    private String name;
    private double price;
    private String category;
    private int quantity;
    
    /**
     * Creates a new Product with the specified details.
     * 
     * @param name the name of the product
     * @param price the price of the product (must be non-negative)
     * @param category the category the product belongs to
     * @param quantity the quantity of this product (must be positive)
     * @throws IllegalArgumentException if price is negative or quantity is not positive
     */
    public Product(String name, double price, String category, int quantity) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.price = price;
        this.category = Objects.requireNonNull(category, "Product category cannot be null");
        this.quantity = quantity;
    }
    
    /**
     * Creates a new Product with quantity 1.
     * 
     * @param name the name of the product
     * @param price the price of the product
     * @param category the category the product belongs to
     */
    public Product(String name, double price, String category) {
        this(name, price, category, 1);
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Calculates the total price for this product (price * quantity).
     * 
     * @return the total price for this product
     */
    public double getTotalPrice() {
        return price * quantity;
    }
    
    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
    }
    
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }
    
    public void setCategory(String category) {
        this.category = Objects.requireNonNull(category, "Product category cannot be null");
    }
    
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - $%.2f x %d = $%.2f", 
                           name, category, price, quantity, getTotalPrice());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Product product = (Product) obj;
        return Double.compare(product.price, price) == 0 &&
               quantity == product.quantity &&
               Objects.equals(name, product.name) &&
               Objects.equals(category, product.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price, category, quantity);
    }
}