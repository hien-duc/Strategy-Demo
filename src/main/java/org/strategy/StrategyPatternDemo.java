package org.strategy;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstration class that tests all discount strategies and showcases
 * the Strategy Pattern implementation in the e-commerce discount system.
 * 
 * This class serves as a comprehensive test to validate that:
 * 1. All discount strategies work correctly
 * 2. The Strategy pattern allows dynamic algorithm switching
 * 3. Different scenarios produce expected results
 */
public class StrategyPatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("E-COMMERCE DISCOUNT SYSTEM - STRATEGY PATTERN DEMONSTRATION");
        System.out.println("=".repeat(80));
        
        // Create test scenarios
        runBasicStrategyDemo();
        runComprehensiveScenarios();
        runBOGODemo();
        runMinimumAmountDemo();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("STRATEGY PATTERN BENEFITS DEMONSTRATED:");
        System.out.println("✓ Runtime algorithm switching");
        System.out.println("✓ Easy addition of new discount strategies");
        System.out.println("✓ Clean separation of concerns");
        System.out.println("✓ Elimination of complex conditional statements");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Demonstrates basic strategy switching with the same cart contents
     */
    private static void runBasicStrategyDemo() {
        System.out.println("\n1. BASIC STRATEGY SWITCHING DEMO");
        System.out.println("-".repeat(50));
        
        // Create shopping cart with sample products
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Gaming Laptop", 999.99, "Electronics"));
        cart.addProduct(new Product("Wireless Mouse", 29.99, "Electronics"));
        cart.addProduct(new Product("Cotton T-Shirt", 24.99, "Clothing"));
        
        System.out.printf("Cart Contents: %.2f subtotal%n", cart.getSubtotal());
        
        // Test different strategies on the same cart
        List<DiscountStrategy> strategies = Arrays.asList(
            new NoDiscount(),
            new PercentageDiscount(10),
            new PercentageDiscount(15, 50),
            new FixedAmountDiscount(25),
            new BuyOneGetOneDiscount("Electronics")
        );
        
        for (DiscountStrategy strategy : strategies) {
            cart.setDiscountStrategy(strategy);
            double discount = cart.getDiscount();
            double total = cart.getTotal();
            
            System.out.printf("Strategy: %-25s | Discount: $%6.2f | Total: $%7.2f%n",
                            strategy.getStrategyName(), discount, total);
        }
    }
    
    /**
     * Tests various cart scenarios with different product combinations
     */
    private static void runComprehensiveScenarios() {
        System.out.println("\n2. COMPREHENSIVE SCENARIO TESTING");
        System.out.println("-".repeat(50));
        
        // Scenario 1: Small order
        testScenario("Small Order (<$50)", 
                    Arrays.asList(new Product("Book", 15.99, "Books")),
                    new PercentageDiscount(10, 50));
        
        // Scenario 2: Medium order
        testScenario("Medium Order ($50-$100)", 
                    Arrays.asList(
                        new Product("Headphones", 79.99, "Electronics"),
                        new Product("Phone Case", 19.99, "Electronics")
                    ),
                    new PercentageDiscount(10, 50));
        
        // Scenario 3: Large order
        testScenario("Large Order (>$100)", 
                    Arrays.asList(
                        new Product("Gaming Laptop", 999.99, "Electronics"),
                        new Product("External Monitor", 299.99, "Electronics")
                    ),
                    new PercentageDiscount(20, 100));
        
        // Scenario 4: Fixed discount larger than order
        testScenario("Fixed Discount > Order Total", 
                    Arrays.asList(new Product("Pen", 2.99, "Office")),
                    new FixedAmountDiscount(10));
    }
    
    /**
     * Demonstrates Buy One Get One strategies with different configurations
     */
    private static void runBOGODemo() {
        System.out.println("\n3. BUY ONE GET ONE (BOGO) STRATEGY DEMO");
        System.out.println("-".repeat(50));
        
        // BOGO scenario with multiple same items
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Gaming Mouse", 49.99, "Electronics", 3));
        cart.addProduct(new Product("T-Shirt", 19.99, "Clothing", 4));
        
        System.out.printf("Cart: 3x Gaming Mouse ($49.99) + 4x T-Shirt ($19.99) = $%.2f%n", 
                         cart.getSubtotal());
        
        // Test different BOGO strategies
        List<DiscountStrategy> bogoStrategies = Arrays.asList(
            new BuyOneGetOneDiscount(), // All categories, 100% off
            new BuyOneGetOneDiscount("Electronics"), // Electronics only, 100% off
            new BuyOneGetOneDiscount("Clothing", 50), // Clothing only, 50% off
            new BuyOneGetOneDiscount("Books") // Books only (no applicable items)
        );
        
        for (DiscountStrategy strategy : bogoStrategies) {
            cart.setDiscountStrategy(strategy);
            double discount = cart.getDiscount();
            double total = cart.getTotal();
            
            System.out.printf("%-30s | Discount: $%6.2f | Total: $%7.2f%n",
                            strategy.getStrategyName(), discount, total);
        }
    }
    
    /**
     * Tests minimum amount requirements for discounts
     */
    private static void runMinimumAmountDemo() {
        System.out.println("\n4. MINIMUM AMOUNT REQUIREMENT DEMO");
        System.out.println("-".repeat(50));
        
        // Test with amounts below and above minimum thresholds
        List<Product> products = Arrays.asList(
            new Product("Smartphone", 699.99, "Electronics"),
            new Product("Phone Case", 24.99, "Electronics")
        );
        
        ShoppingCart cart = new ShoppingCart();
        products.forEach(cart::addProduct);
        
        System.out.printf("Cart Total: $%.2f%n", cart.getSubtotal());
        
        // Test strategies with different minimum requirements
        List<DiscountStrategy> minAmountStrategies = Arrays.asList(
            new PercentageDiscount(15, 500), // Minimum $500 (cart qualifies)
            new PercentageDiscount(20, 800), // Minimum $800 (cart doesn't qualify)
            new FixedAmountDiscount(50, 600), // Minimum $600 (cart qualifies)
            new FixedAmountDiscount(100, 1000) // Minimum $1000 (cart doesn't qualify)
        );
        
        for (DiscountStrategy strategy : minAmountStrategies) {
            cart.setDiscountStrategy(strategy);
            double discount = cart.getDiscount();
            double total = cart.getTotal();
            boolean qualifies = discount > 0;
            
            System.out.printf("%-25s | Qualifies: %-5s | Discount: $%6.2f | Total: $%7.2f%n",
                            strategy.getStrategyName(), qualifies, discount, total);
        }
    }
    
    /**
     * Helper method to test a specific scenario
     */
    private static void testScenario(String scenarioName, List<Product> products, 
                                   DiscountStrategy strategy) {
        ShoppingCart cart = new ShoppingCart(strategy);
        products.forEach(cart::addProduct);
        
        double subtotal = cart.getSubtotal();
        double discount = cart.getDiscount();
        double total = cart.getTotal();
        
        System.out.printf("%-25s | Subtotal: $%7.2f | Discount: $%6.2f | Total: $%7.2f%n",
                         scenarioName, subtotal, discount, total);
    }
    
    /**
     * Demonstrates the extensibility of the Strategy pattern by showing
     * how easy it would be to add new discount strategies
     */
    public static void demonstrateExtensibility() {
        System.out.println("\n5. STRATEGY PATTERN EXTENSIBILITY");
        System.out.println("-".repeat(50));
        System.out.println("Adding new discount strategies is easy:");
        System.out.println("1. Create a new class implementing DiscountStrategy");
        System.out.println("2. Implement calculateDiscount() method");
        System.out.println("3. Add to the discount options in the GUI");
        System.out.println("4. No existing code needs modification!");
        
        // Example of how a new strategy could be added
        System.out.println("\nExample new strategies that could be easily added:");
        System.out.println("- SeasonalDiscount (higher discounts during holidays)");
        System.out.println("- LoyaltyDiscount (based on customer tier)");
        System.out.println("- VolumeDiscount (bulk purchase discounts)");
        System.out.println("- CategorySpecificDiscount (different rates per category)");
    }
}