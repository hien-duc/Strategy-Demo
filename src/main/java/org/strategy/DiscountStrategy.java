package org.strategy;

import java.util.List;

/**
 * Strategy interface for different discount calculation algorithms.
 * This interface defines the contract that all concrete discount strategies must implement.
 * 
 * The Strategy pattern allows the discount calculation algorithm to vary independently
 * of the clients that use it (ShoppingCart in this case).
 */
public interface DiscountStrategy {
    
    /**
     * Calculates the discount amount for a list of products.
     * 
     * @param products the list of products to calculate discount for
     * @return the discount amount to be subtracted from the total
     */
    double calculateDiscount(List<Product> products);
    
    /**
     * Returns a descriptive name for this discount strategy.
     * This will be used in the UI to display the available discount options.
     * 
     * @return the name of the discount strategy
     */
    String getStrategyName();
    
    /**
     * Returns a description of how this discount strategy works.
     * This provides additional information for users about the discount calculation.
     * 
     * @return a description of the discount strategy
     */
    String getDescription();
}