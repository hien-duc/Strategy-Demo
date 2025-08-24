package org.strategy;

import java.util.List;

/**
 * A discount strategy that applies a percentage discount to the total amount.
 * This strategy calculates a percentage off the total cart value.
 */
public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;
    private final double minimumAmount;
    
    /**
     * Creates a percentage discount strategy.
     * 
     * @param percentage the discount percentage (0-100)
     * @param minimumAmount the minimum cart total required for this discount
     * @throws IllegalArgumentException if percentage is not between 0 and 100, or minimumAmount is negative
     */
    public PercentageDiscount(double percentage, double minimumAmount) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        if (minimumAmount < 0) {
            throw new IllegalArgumentException("Minimum amount cannot be negative");
        }
        
        this.percentage = percentage;
        this.minimumAmount = minimumAmount;
    }
    
    /**
     * Creates a percentage discount strategy with no minimum amount requirement.
     * 
     * @param percentage the discount percentage (0-100)
     */
    public PercentageDiscount(double percentage) {
        this(percentage, 0.0);
    }
    
    @Override
    public double calculateDiscount(List<Product> products) {
        double totalAmount = products.stream()
                                    .mapToDouble(Product::getTotalPrice)
                                    .sum();
        
        // Only apply discount if minimum amount is met
        if (totalAmount >= minimumAmount) {
            return totalAmount * (percentage / 100.0);
        }
        
        return 0.0;
    }
    
    @Override
    public String getStrategyName() {
        if (minimumAmount > 0) {
            return String.format("%.0f%% Off (Min $%.2f)", percentage, minimumAmount);
        } else {
            return String.format("%.0f%% Off", percentage);
        }
    }
    
    @Override
    public String getDescription() {
        if (minimumAmount > 0) {
            return String.format("Get %.0f%% discount on orders over $%.2f", percentage, minimumAmount);
        } else {
            return String.format("Get %.0f%% discount on your entire order", percentage);
        }
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public double getMinimumAmount() {
        return minimumAmount;
    }
}