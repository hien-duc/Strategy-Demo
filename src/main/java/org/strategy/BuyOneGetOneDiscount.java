package org.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A discount strategy that implements Buy One Get One (BOGO) offers.
 * This strategy applies discounts when customers buy multiple quantities of the same product.
 */
public class BuyOneGetOneDiscount implements DiscountStrategy {
    private final String targetCategory;
    private final double discountPercentage;
    
    /**
     * Creates a BOGO discount strategy for a specific category.
     * 
     * @param targetCategory the product category this discount applies to (null for all categories)
     * @param discountPercentage the discount percentage for the free/discounted item (0-100)
     * @throws IllegalArgumentException if discountPercentage is not between 0 and 100
     */
    public BuyOneGetOneDiscount(String targetCategory, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        this.targetCategory = targetCategory;
        this.discountPercentage = discountPercentage;
    }
    
    /**
     * Creates a BOGO free discount strategy (100% discount on second item) for all categories.
     */
    public BuyOneGetOneDiscount() {
        this(null, 100.0);
    }
    
    /**
     * Creates a BOGO free discount strategy (100% discount on second item) for a specific category.
     * 
     * @param targetCategory the product category this discount applies to
     */
    public BuyOneGetOneDiscount(String targetCategory) {
        this(targetCategory, 100.0);
    }
    
    @Override
    public double calculateDiscount(List<Product> products) {
        double totalDiscount = 0.0;
        
        // Group products by name to handle quantities
        Map<String, List<Product>> productGroups = products.stream()
            .filter(p -> targetCategory == null || targetCategory.equals(p.getCategory()))
            .collect(Collectors.groupingBy(Product::getName));
        
        for (List<Product> group : productGroups.values()) {
            if (!group.isEmpty()) {
                Product product = group.get(0);
                int totalQuantity = group.stream().mapToInt(Product::getQuantity).sum();
                
                // Calculate how many free/discounted items we get
                int discountedItems = totalQuantity / 2;
                
                // Calculate discount amount
                double itemDiscount = product.getPrice() * (discountPercentage / 100.0);
                totalDiscount += discountedItems * itemDiscount;
            }
        }
        
        return totalDiscount;
    }
    
    @Override
    public String getStrategyName() {
        String categoryText = (targetCategory != null) ? " (" + targetCategory + ")" : "";
        
        if (discountPercentage == 100.0) {
            return "Buy One Get One Free" + categoryText;
        } else if (discountPercentage == 50.0) {
            return "Buy One Get One 50% Off" + categoryText;
        } else {
            return String.format("Buy One Get One %.0f%% Off%s", discountPercentage, categoryText);
        }
    }
    
    @Override
    public String getDescription() {
        String categoryText = (targetCategory != null) ? " in " + targetCategory + " category" : "";
        
        if (discountPercentage == 100.0) {
            return "Buy one item and get another one free" + categoryText;
        } else {
            return String.format("Buy one item and get %.0f%% off the second item%s", 
                               discountPercentage, categoryText);
        }
    }
    
    public String getTargetCategory() {
        return targetCategory;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
}