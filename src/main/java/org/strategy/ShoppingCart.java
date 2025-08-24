package org.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Shopping cart that uses the Strategy pattern to apply different discount strategies.
 * This is the Context class in the Strategy pattern that maintains a reference to a
 * DiscountStrategy object and delegates the discount calculation to it.
 */
public class ShoppingCart {
    private final List<Product> products;
    private DiscountStrategy discountStrategy;
    
    /**
     * Creates a new shopping cart with the specified discount strategy.
     * 
     * @param discountStrategy the initial discount strategy to use
     */
    public ShoppingCart(DiscountStrategy discountStrategy) {
        this.products = new ArrayList<>();
        this.discountStrategy = Objects.requireNonNull(discountStrategy, 
                                                       "Discount strategy cannot be null");
    }
    
    /**
     * Creates a new shopping cart with no discount strategy (NoDiscount).
     */
    public ShoppingCart() {
        this(new NoDiscount());
    }
    
    /**
     * Adds a product to the shopping cart.
     * 
     * @param product the product to add
     * @throws IllegalArgumentException if product is null
     */
    public void addProduct(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        products.add(product);
    }
    
    /**
     * Removes a product from the shopping cart.
     * 
     * @param product the product to remove
     * @return true if the product was removed, false if it wasn't in the cart
     */
    public boolean removeProduct(Product product) {
        return products.remove(product);
    }
    
    /**
     * Removes all products from the shopping cart.
     */
    public void clearCart() {
        products.clear();
    }
    
    /**
     * Changes the discount strategy at runtime.
     * This demonstrates the key feature of the Strategy pattern - 
     * the ability to change algorithms dynamically.
     * 
     * @param discountStrategy the new discount strategy to use
     * @throws IllegalArgumentException if discountStrategy is null
     */
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = Objects.requireNonNull(discountStrategy, 
                                                       "Discount strategy cannot be null");
    }
    
    /**
     * Gets the current discount strategy.
     * 
     * @return the current discount strategy
     */
    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }
    
    /**
     * Gets a copy of the products in the cart.
     * 
     * @return a new list containing all products in the cart
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
    
    /**
     * Calculates the subtotal (before discount) of all products in the cart.
     * 
     * @return the subtotal amount
     */
    public double getSubtotal() {
        return products.stream()
                      .mapToDouble(Product::getTotalPrice)
                      .sum();
    }
    
    /**
     * Calculates the discount amount using the current discount strategy.
     * 
     * @return the discount amount
     */
    public double getDiscount() {
        return discountStrategy.calculateDiscount(products);
    }
    
    /**
     * Calculates the total amount after applying the discount.
     * 
     * @return the final total after discount
     */
    public double getTotal() {
        double subtotal = getSubtotal();
        double discount = getDiscount();
        return Math.max(0, subtotal - discount); // Ensure total is never negative
    }
    
    /**
     * Calculates the tax amount based on the total after discount.
     * 
     * @param taxRate the tax rate as a percentage (e.g., 8.5 for 8.5%)
     * @return the tax amount
     */
    public double getTax(double taxRate) {
        return getTotal() * (taxRate / 100.0);
    }
    
    /**
     * Calculates the final total including tax.
     * 
     * @param taxRate the tax rate as a percentage
     * @return the final total including tax
     */
    public double getTotalWithTax(double taxRate) {
        return getTotal() + getTax(taxRate);
    }
    
    /**
     * Gets the number of items in the cart.
     * 
     * @return the total number of items (sum of all quantities)
     */
    public int getItemCount() {
        return products.stream()
                      .mapToInt(Product::getQuantity)
                      .sum();
    }
    
    /**
     * Checks if the cart is empty.
     * 
     * @return true if the cart has no products, false otherwise
     */
    public boolean isEmpty() {
        return products.isEmpty();
    }
    
    /**
     * Returns a detailed string representation of the shopping cart.
     * 
     * @return a formatted string showing all products, discounts, and totals
     */
    public String getCartSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Shopping Cart Summary ===\n");
        
        if (products.isEmpty()) {
            summary.append("Cart is empty\n");
            return summary.toString();
        }
        
        summary.append("Products:\n");
        for (Product product : products) {
            summary.append("  ").append(product.toString()).append("\n");
        }
        
        summary.append(String.format("\nSubtotal: $%.2f\n", getSubtotal()));
        summary.append(String.format("Discount Strategy: %s\n", discountStrategy.getStrategyName()));
        summary.append(String.format("Discount: -$%.2f\n", getDiscount()));
        summary.append(String.format("Total: $%.2f\n", getTotal()));
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return String.format("ShoppingCart[items=%d, subtotal=$%.2f, discount=$%.2f, total=$%.2f]",
                           getItemCount(), getSubtotal(), getDiscount(), getTotal());
    }
}