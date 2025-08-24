package org.strategy;

import java.util.List;

/**
 * A discount strategy that applies no discount at all.
 * This is the default strategy and serves as a null object pattern implementation.
 */
public class NoDiscount implements DiscountStrategy {
    
    @Override
    public double calculateDiscount(List<Product> products) {
        // No discount applied
        return 0.0;
    }
    
    @Override
    public String getStrategyName() {
        return "No Discount";
    }
    
    @Override
    public String getDescription() {
        return "No discount is applied to the total amount.";
    }
}