package org.strategy;

import java.util.List;

/**
 * A discount strategy that applies a fixed dollar amount discount.
 * This strategy subtracts a specific amount from the total cart value.
 */
public class FixedAmountDiscount implements DiscountStrategy {
    private final double discountAmount;
    private final double minimumPurchase;
    
    /**
     * Creates a fixed amount discount strategy.
     * 
     * @param discountAmount the fixed amount to discount
     * @param minimumPurchase the minimum cart total required for this discount
     * @throws IllegalArgumentException if discountAmount or minimumPurchase is negative
     */
    public FixedAmountDiscount(double discountAmount, double minimumPurchase) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        if (minimumPurchase < 0) {
            throw new IllegalArgumentException("Minimum purchase amount cannot be negative");
        }
        
        this.discountAmount = discountAmount;
        this.minimumPurchase = minimumPurchase;
    }
    
    /**
     * Creates a fixed amount discount strategy with no minimum purchase requirement.
     * 
     * @param discountAmount the fixed amount to discount
     */
    public FixedAmountDiscount(double discountAmount) {
        this(discountAmount, 0.0);
    }
    
    @Override
    public double calculateDiscount(List<Product> products) {
        double totalAmount = products.stream()
                                    .mapToDouble(Product::getTotalPrice)
                                    .sum();
        
        // Only apply discount if minimum purchase is met
        if (totalAmount >= minimumPurchase) {
            // Don't let discount exceed the total amount
            return Math.min(discountAmount, totalAmount);
        }
        
        return 0.0;
    }
    
    @Override
    public String getStrategyName() {
        if (minimumPurchase > 0) {
            return String.format("$%.2f Off (Min $%.2f)", discountAmount, minimumPurchase);
        } else {
            return String.format("$%.2f Off", discountAmount);
        }
    }
    
    @Override
    public String getDescription() {
        if (minimumPurchase > 0) {
            return String.format("Save $%.2f on orders over $%.2f", discountAmount, minimumPurchase);
        } else {
            return String.format("Save $%.2f on your order", discountAmount);
        }
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public double getMinimumPurchase() {
        return minimumPurchase;
    }
}