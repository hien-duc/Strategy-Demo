package org.strategy;

import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

/**
 * Enhanced JavaFX application demonstrating the Strategy pattern in an e-commerce discount system.
 * Features modern UI design, user authentication, and professional styling.
 * This application allows users to add products to a shopping cart and apply different
 * discount strategies to see how the Strategy pattern enables dynamic algorithm switching.
 */
public class ECommerceDiscountApp extends Application {
    
    private UserService userService;
    private User currentUser;
    private ShoppingCart shoppingCart;
    private TableView<Product> cartTable;
    private ObservableList<Product> cartItems;
    private ComboBox<DiscountStrategy> discountComboBox;
    
    // UI Components for totals
    private Label subtotalLabel;
    private Label discountLabel;
    private Label totalLabel;
    private Label strategyDescriptionLabel;
    private Label itemCountLabel;
    
    // User profile components
    private VBox userProfileSection;
    private Label userNameLabel;
    private Label userRoleLabel;
    private Label userAvatarLabel;
    
    // Predefined discount strategies
    private final List<DiscountStrategy> discountStrategies = Arrays.asList(
        new NoDiscount(),
        new PercentageDiscount(10, 50),  // 10% off orders over $50
        new PercentageDiscount(20, 100), // 20% off orders over $100
        new FixedAmountDiscount(5, 25),  // $5 off orders over $25
        new FixedAmountDiscount(15, 75), // $15 off orders over $75
        new BuyOneGetOneDiscount(),      // BOGO free for all categories
        new BuyOneGetOneDiscount("Electronics"), // BOGO free for electronics
        new BuyOneGetOneDiscount("Clothing", 50) // BOGO 50% off for clothing
    );
    
    @Override
    public void start(Stage primaryStage) {
        this.userService = UserService.getInstance();
        this.currentUser = userService.getCurrentUser();
        
        if (currentUser == null) {
            // If no user is logged in, show login screen
            showLoginScreen();
            return;
        }
        
        shoppingCart = new ShoppingCart();
        cartItems = FXCollections.observableArrayList();
        
        primaryStage.setTitle("E-Commerce Discount Application - Welcome " + currentUser.getDisplayName());
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
        primaryStage.setWidth(1400);
        primaryStage.setHeight(900);
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.getStyleClass().add("application-root");
        
        // Top: Header with user profile and navigation
        VBox topSection = createHeaderSection();
        root.setTop(topSection);
        
        // Center: Main content area with scroll support
        ScrollPane mainScrollPane = new ScrollPane();
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.getStyleClass().add("scroll-pane");
        
        HBox centerContent = new HBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setMinHeight(Region.USE_PREF_SIZE);
        
        // Left: Product addition section
        VBox leftSection = createProductAdditionSection();
        leftSection.setPrefWidth(350);
        leftSection.setMinWidth(320);
        leftSection.setMaxWidth(380);
        
        // Center: Shopping cart section
        VBox centerSection = createCartSection();
        HBox.setHgrow(centerSection, Priority.ALWAYS);
        centerSection.setMinWidth(400);
        
        // Right: Discount strategy and totals section
        VBox rightSection = createDiscountSection();
        rightSection.setPrefWidth(320);
        rightSection.setMinWidth(300);
        rightSection.setMaxWidth(350);
        
        centerContent.getChildren().addAll(leftSection, centerSection, rightSection);
        mainScrollPane.setContent(centerContent);
        root.setCenter(mainScrollPane);
        
        // Create scene and apply styling
        Scene scene = new Scene(root, 1400, 900);
        
        // Load CSS
        try {
            String css = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Initialize with default values
        updateTotals();
        
        // Add entrance animation
        playEntranceAnimation(root);
    }
    
    private void showLoginScreen() {
        try {
            LoginRegisterApp loginApp = new LoginRegisterApp();
            Stage loginStage = new Stage();
            loginApp.start(loginStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox();
        header.getStyleClass().add("user-profile");
        
        HBox headerContent = new HBox(20);
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(16, 20, 16, 20));
        
        // App title and branding
        VBox brandingSection = new VBox(5);
        Label appTitle = new Label("ECommerce Pro");
        appTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label appSubtitle = new Label("Strategy Pattern Demonstration");
        appSubtitle.getStyleClass().add("label-secondary");
        brandingSection.getChildren().addAll(appTitle, appSubtitle);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User profile section
        userProfileSection = createUserProfileSection();
        
        headerContent.getChildren().addAll(brandingSection, spacer, userProfileSection);
        header.getChildren().add(headerContent);
        
        return header;
    }
    
    private VBox createUserProfileSection() {
        VBox profileSection = new VBox(8);
        profileSection.setAlignment(Pos.CENTER_RIGHT);
        
        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        
        // User details
        VBox userDetails = new VBox(2);
        userDetails.setAlignment(Pos.CENTER_RIGHT);
        
        userNameLabel = new Label(currentUser.getDisplayName());
        userNameLabel.getStyleClass().add("username");
        
        userRoleLabel = new Label(currentUser.getRole().getDisplayName());
        userRoleLabel.getStyleClass().add("user-role");
        
        userDetails.getChildren().addAll(userNameLabel, userRoleLabel);
        
        // User avatar
        userAvatarLabel = new Label(currentUser.getInitials());
        userAvatarLabel.getStyleClass().add("user-avatar");
        
        userInfo.getChildren().addAll(userDetails, userAvatarLabel);
        
        // Action buttons
        HBox actionButtons = new HBox(8);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button profileButton = new Button("Profile");
        profileButton.getStyleClass().addAll("btn", "btn-secondary", "btn-small");
        profileButton.setOnAction(e -> showProfileDialog());
        
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().addAll("btn", "btn-danger", "btn-small");
        logoutButton.setOnAction(e -> handleLogout());
        
        actionButtons.getChildren().addAll(profileButton, logoutButton);
        
        profileSection.getChildren().addAll(userInfo, actionButtons);
        
        return profileSection;
    }
    
    
    private void showProfileDialog() {
        Alert profileDialog = new Alert(Alert.AlertType.INFORMATION);
        profileDialog.setTitle("User Profile");
        profileDialog.setHeaderText("Profile Information");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        content.getChildren().addAll(
            new Label("Username: " + currentUser.getUsername()),
            new Label("Email: " + currentUser.getEmail()),
            new Label("Full Name: " + currentUser.getFullName()),
            new Label("Role: " + currentUser.getRole().getDisplayName()),
            new Label("Member Since: " + currentUser.getRegistrationDate().toLocalDate())
        );
        
        profileDialog.getDialogPane().setContent(content);
        profileDialog.showAndWait();
    }
    
    private void handleLogout() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Logout");
        confirmDialog.setHeaderText("Are you sure you want to logout?");
        confirmDialog.setContentText("You will need to sign in again to access the application.");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userService.logout();
                
                // Close current window
                Stage currentStage = (Stage) userProfileSection.getScene().getWindow();
                currentStage.close();
                
                // Show login screen
                showLoginScreen();
            }
        });
    }
    
    private VBox createProductAdditionSection() {
        VBox section = new VBox(20);
        section.getStyleClass().addAll("card");
        section.setPrefWidth(350);
        section.setMaxHeight(Double.MAX_VALUE);
        
        Label sectionTitle = new Label("Add Products");
        sectionTitle.getStyleClass().add("card-header");
        
        // Create scrollable content area
        VBox cardBody = new VBox(15);
        cardBody.getStyleClass().add("card-body");
        
        // Create scroll pane for the card body
        ScrollPane scrollPane = new ScrollPane(cardBody);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(600); // Set a preferred height to enable scrolling
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        
        // Product form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(12);
        
        // Name field
        Label nameLabel = new Label("Product Name");
        nameLabel.getStyleClass().add("label-secondary");
        TextField nameField = new TextField();
        nameField.getStyleClass().add("modern-text-field");
        nameField.setPromptText("Enter product name");
        
        // Price field
        Label priceLabel = new Label("Price ($)");
        priceLabel.getStyleClass().add("label-secondary");
        TextField priceField = new TextField();
        priceField.getStyleClass().add("modern-text-field");
        priceField.setPromptText("0.00");
        
        // Category field
        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("label-secondary");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getStyleClass().add("combo-box");
        categoryComboBox.getItems().addAll("Electronics", "Clothing", "Books", "Home & Garden", "Sports", "Beauty");
        categoryComboBox.setPromptText("Select category");
        categoryComboBox.setPrefWidth(Double.MAX_VALUE);
        
        // Quantity field
        Label quantityLabel = new Label("Quantity");
        quantityLabel.getStyleClass().add("label-secondary");
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 99, 1);
        quantitySpinner.getStyleClass().add("spinner");
        quantitySpinner.setPrefWidth(Double.MAX_VALUE);
        
        formGrid.add(nameLabel, 0, 0, 2, 1);
        formGrid.add(nameField, 0, 1, 2, 1);
        formGrid.add(priceLabel, 0, 2);
        formGrid.add(categoryLabel, 1, 2);
        formGrid.add(priceField, 0, 3);
        formGrid.add(categoryComboBox, 1, 3);
        formGrid.add(quantityLabel, 0, 4);
        formGrid.add(quantitySpinner, 1, 4);
        
        // Make columns grow equally
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        formGrid.getColumnConstraints().addAll(col1, col2);
        
        Button addButton = new Button("Add to Cart");
        addButton.getStyleClass().addAll("btn", "btn-primary");
        addButton.setPrefWidth(Double.MAX_VALUE);
        
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String priceText = priceField.getText().trim();
                String category = categoryComboBox.getValue();
                int quantity = quantitySpinner.getValue();
                
                if (name.isEmpty() || priceText.isEmpty() || category == null) {
                    showAlert("Validation Error", "Please fill in all fields", Alert.AlertType.WARNING);
                    return;
                }
                
                double price = Double.parseDouble(priceText);
                if (price < 0) {
                    showAlert("Validation Error", "Price cannot be negative", Alert.AlertType.WARNING);
                    return;
                }
                
                Product product = new Product(name, price, category, quantity);
                
                shoppingCart.addProduct(product);
                cartItems.add(product);
                updateTotals();
                
                // Clear form with animation
                clearFormWithAnimation(nameField, priceField, categoryComboBox, quantitySpinner);
                
                // Show success message
                showTemporaryMessage("Product added successfully!", "success");
                
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid price", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "Error adding product: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
        
        // Quick add section with modern cards
        VBox quickAddSection = createQuickAddSection();
        
        // Cart management
        VBox cartManagement = new VBox(10);
        Label cartManagementTitle = new Label("Cart Management");
        cartManagementTitle.getStyleClass().add("section-subtitle");
        
        itemCountLabel = new Label("0 items in cart");
        itemCountLabel.getStyleClass().add("label-secondary");
        
        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.getStyleClass().addAll("btn", "btn-danger");
        clearCartButton.setPrefWidth(Double.MAX_VALUE);
        clearCartButton.setOnAction(e -> {
            if (shoppingCart.isEmpty()) {
                showAlert("Information", "Cart is already empty", Alert.AlertType.INFORMATION);
                return;
            }
            
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Clear Cart");
            confirmAlert.setHeaderText("Are you sure?");
            confirmAlert.setContentText("This will remove all items from your cart.");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    shoppingCart.clearCart();
                    cartItems.clear();
                    updateTotals();
                    showTemporaryMessage("Cart cleared successfully!", "info");
                }
            });
        });
        
        cartManagement.getChildren().addAll(cartManagementTitle, itemCountLabel, clearCartButton);
        
        cardBody.getChildren().addAll(formGrid, addButton, 
                                     new Separator(), quickAddSection, 
                                     new Separator(), cartManagement);
        
        section.getChildren().addAll(sectionTitle, scrollPane);
        return section;
    }
    
    
    private VBox createQuickAddSection() {
        VBox quickAddSection = new VBox(10);
        Label quickAddLabel = new Label("Quick Add Demo Products");
        quickAddLabel.getStyleClass().add("section-subtitle");
        
        // Create a container for better organization
        VBox productCards = new VBox(8);
        productCards.setMaxHeight(300); // Limit height to enable internal scrolling if needed
        
        // Expanded demo products with variety
        Product[] demoProducts = {
            new Product("Gaming Laptop", 999.99, "Electronics"),
            new Product("Cotton T-Shirt", 24.99, "Clothing"),
            new Product("Java Programming Book", 14.99, "Books"),
            new Product("Wireless Mouse", 29.99, "Electronics"),
            new Product("Coffee Mug", 12.99, "Home & Garden"),
            new Product("Bluetooth Headphones", 89.99, "Electronics"),
            new Product("Denim Jeans", 49.99, "Clothing"),
            new Product("Fitness Tracker", 149.99, "Electronics"),
            new Product("Cooking Book", 19.99, "Books"),
            new Product("Plant Pot", 15.99, "Home & Garden"),
            new Product("Running Shoes", 79.99, "Sports"),
            new Product("Face Cream", 34.99, "Beauty")
        };
        
        for (Product product : demoProducts) {
            Button productButton = createQuickAddButton(product);
            productCards.getChildren().add(productButton);
        }
        
        quickAddSection.getChildren().addAll(quickAddLabel, productCards);
        return quickAddSection;
    }
    
    private Button createQuickAddButton(Product product) {
        Button button = new Button();
        button.getStyleClass().addAll("btn", "btn-secondary", "hoverable");
        button.setPrefWidth(Double.MAX_VALUE);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(50); // Ensure consistent height
        
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(5));
        
        // Product info section
        VBox productInfo = new VBox(2);
        productInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setWrapText(true);
        
        Label detailsLabel = new Label(String.format("%s - $%.2f", product.getCategory(), product.getPrice()));
        detailsLabel.setStyle("-fx-font-size: 10px;");
        detailsLabel.getStyleClass().add("label-secondary");
        
        productInfo.getChildren().addAll(nameLabel, detailsLabel);
        
        // Add icon or visual indicator
        Label addIcon = new Label("+");
        addIcon.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3498db;");
        addIcon.setMinWidth(25);
        addIcon.setAlignment(Pos.CENTER);
        
        // Spacer to push the + icon to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        content.getChildren().addAll(productInfo, spacer, addIcon);
        button.setGraphic(content);
        
        button.setOnAction(e -> {
            shoppingCart.addProduct(product);
            cartItems.add(product);
            updateTotals();
            
            // Enhanced visual feedback with color change
            String originalStyle = button.getStyle();
            button.setStyle(originalStyle + "-fx-background-color: #2ecc71;");
            addIcon.setText("✓");
            addIcon.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            Timeline resetButton = new Timeline(new KeyFrame(
                Duration.millis(1000), 
                event -> {
                    button.setStyle(originalStyle);
                    addIcon.setText("+");
                    addIcon.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3498db;");
                }
            ));
            resetButton.play();
            
            showTemporaryMessage(product.getName() + " added to cart!", "success");
        });
        
        return button;
    }
    
    private void clearFormWithAnimation(TextField nameField, TextField priceField, 
                                       ComboBox<String> categoryComboBox, Spinner<Integer> quantitySpinner) {
        // Create fade out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.7);
        
        fadeOut.setOnFinished(e -> {
            nameField.clear();
            priceField.clear();
            categoryComboBox.setValue(null);
            quantitySpinner.getValueFactory().setValue(1);
            
            // Fade back in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150));
            fadeIn.setFromValue(0.7);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        
        fadeOut.play();
    }
    
    private void showTemporaryMessage(String message, String type) {
        // This could be implemented as a toast notification
        // For now, we'll use a simple console output
        System.out.println("[" + type.toUpperCase() + "] " + message);
    }
    
    private void playEntranceAnimation(BorderPane root) {
        // Set initial state
        root.setOpacity(0);
        root.setScaleX(0.95);
        root.setScaleY(0.95);
        
        // Create entrance animation
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(root.opacityProperty(), 0),
                new KeyValue(root.scaleXProperty(), 0.95),
                new KeyValue(root.scaleYProperty(), 0.95)
            ),
            new KeyFrame(Duration.millis(500),
                new KeyValue(root.opacityProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(root.scaleXProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(root.scaleYProperty(), 1, Interpolator.EASE_OUT)
            )
        );
        
        timeline.play();
    }
    
    private VBox createCartSection() {
        VBox section = new VBox(20);
        section.getStyleClass().add("card");
        
        Label sectionTitle = new Label("Shopping Cart");
        sectionTitle.getStyleClass().add("card-header");
        
        VBox cardBody = new VBox(15);
        cardBody.getStyleClass().add("card-body");
        
        // Create table with modern styling
        cartTable = new TableView<>();
        cartTable.setItems(cartItems);
        cartTable.getStyleClass().add("table-view");
        cartTable.setPrefHeight(350);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Enhanced table columns
        TableColumn<Product, String> nameColumn = new TableColumn<>("Product");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(120);
        nameColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(name);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });
        
        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setMinWidth(80);
        categoryColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(category);
                    setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
                }
            }
        });
        
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Unit Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(80);
        priceColumn.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
        
        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Qty");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(50);
        quantityColumn.setCellFactory(column -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer quantity, boolean empty) {
                super.updateItem(quantity, empty);
                if (empty || quantity == null) {
                    setText(null);
                } else {
                    setText(quantity.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
        
        TableColumn<Product, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalColumn.setMinWidth(80);
        totalColumn.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", total));
                    setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
                }
            }
        });
        
        // Enhanced action column with styled button
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setMinWidth(80);
        actionColumn.setCellFactory(column -> new TableCell<Product, Void>() {
            private final Button removeButton = new Button("Remove");
            
            {
                removeButton.getStyleClass().addAll("btn", "btn-danger", "btn-small");
                removeButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    
                    // Add confirmation for expensive items
                    if (product.getTotalPrice() > 100) {
                        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmAlert.setTitle("Remove Item");
                        confirmAlert.setHeaderText("Remove expensive item?");
                        confirmAlert.setContentText(String.format("Are you sure you want to remove %s ($%.2f)?", 
                                                                  product.getName(), product.getTotalPrice()));
                        
                        confirmAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                removeProduct(product);
                            }
                        });
                    } else {
                        removeProduct(product);
                    }
                });
            }
            
            private void removeProduct(Product product) {
                shoppingCart.removeProduct(product);
                cartItems.remove(product);
                updateTotals();
                showTemporaryMessage(product.getName() + " removed from cart", "info");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
        
        cartTable.getColumns().addAll(nameColumn, categoryColumn, priceColumn, quantityColumn, totalColumn, actionColumn);
        
        // Empty cart placeholder
        Label emptyCartLabel = new Label("Your cart is empty");
        emptyCartLabel.getStyleClass().add("label-secondary");
        emptyCartLabel.setStyle("-fx-font-size: 16px; -fx-padding: 40px;");
        cartTable.setPlaceholder(emptyCartLabel);
        
        cardBody.getChildren().add(cartTable);
        section.getChildren().addAll(sectionTitle, cardBody);
        return section;
    }
    
    private VBox createDiscountSection() {
        VBox section = new VBox(20);
        section.getStyleClass().add("card");
        section.setPrefWidth(320);
        
        Label sectionTitle = new Label("Discount Strategy");
        sectionTitle.getStyleClass().add("card-header");
        
        VBox cardBody = new VBox(20);
        cardBody.getStyleClass().add("card-body");
        
        // Strategy selection section
        VBox strategySection = new VBox(10);
        Label strategyLabel = new Label("Select Discount Strategy:");
        strategyLabel.getStyleClass().add("section-subtitle");
        
        discountComboBox = new ComboBox<>();
        discountComboBox.getStyleClass().add("combo-box");
        discountComboBox.getItems().addAll(discountStrategies);
        discountComboBox.setValue(discountStrategies.get(0)); // Default to NoDiscount
        discountComboBox.setPrefWidth(Double.MAX_VALUE);
        
        // Custom cell factory to display strategy names
        discountComboBox.setCellFactory(listView -> new ListCell<DiscountStrategy>() {
            @Override
            protected void updateItem(DiscountStrategy strategy, boolean empty) {
                super.updateItem(strategy, empty);
                if (empty || strategy == null) {
                    setText(null);
                } else {
                    setText(strategy.getStrategyName());
                }
            }
        });
        
        discountComboBox.setButtonCell(new ListCell<DiscountStrategy>() {
            @Override
            protected void updateItem(DiscountStrategy strategy, boolean empty) {
                super.updateItem(strategy, empty);
                if (empty || strategy == null) {
                    setText(null);
                } else {
                    setText(strategy.getStrategyName());
                }
            }
        });
        
        discountComboBox.setOnAction(e -> {
            DiscountStrategy selectedStrategy = discountComboBox.getValue();
            if (selectedStrategy != null) {
                shoppingCart.setDiscountStrategy(selectedStrategy);
                updateTotals();
                
                // Add visual feedback
                FadeTransition fade = new FadeTransition(Duration.millis(200), strategyDescriptionLabel);
                fade.setFromValue(0.5);
                fade.setToValue(1.0);
                fade.play();
            }
        });
        
        // Strategy description
        strategyDescriptionLabel = new Label();
        strategyDescriptionLabel.setWrapText(true);
        strategyDescriptionLabel.getStyleClass().addAll("label-secondary");
        strategyDescriptionLabel.setStyle("-fx-font-size: 12px; -fx-background-color: rgba(52, 152, 219, 0.1); " +
                                         "-fx-padding: 10px; -fx-background-radius: 5px;");
        strategyDescriptionLabel.setPrefWidth(Double.MAX_VALUE);
        
        strategySection.getChildren().addAll(strategyLabel, discountComboBox, strategyDescriptionLabel);
        
        // Totals section with enhanced styling
        VBox totalsSection = new VBox(15);
        totalsSection.getStyleClass().add("card");
        totalsSection.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef); " +
                              "-fx-border-color: #dee2e6; -fx-border-width: 1px; -fx-border-radius: 8px; " +
                              "-fx-background-radius: 8px; -fx-padding: 20px;");
        
        Label totalsTitle = new Label("Order Summary");
        totalsTitle.getStyleClass().add("section-subtitle");
        
        // Item count
        itemCountLabel = new Label("0 items");
        itemCountLabel.getStyleClass().add("label-secondary");
        
        // Subtotal
        HBox subtotalRow = new HBox();
        subtotalRow.setAlignment(Pos.CENTER_LEFT);
        Label subtotalLabelText = new Label("Subtotal:");
        Region subtotalSpacer = new Region();
        HBox.setHgrow(subtotalSpacer, Priority.ALWAYS);
        subtotalLabel = new Label("$0.00");
        subtotalLabel.setStyle("-fx-font-size: 14px;");
        subtotalRow.getChildren().addAll(subtotalLabelText, subtotalSpacer, subtotalLabel);
        
        // Discount row
        HBox discountRow = new HBox();
        discountRow.setAlignment(Pos.CENTER_LEFT);
        Label discountLabelText = new Label("Discount:");
        Region discountSpacer = new Region();
        HBox.setHgrow(discountSpacer, Priority.ALWAYS);
        discountLabel = new Label("-$0.00");
        discountLabel.getStyleClass().add("label-success");
        discountLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        discountRow.getChildren().addAll(discountLabelText, discountSpacer, discountLabel);
        
        // Total row with emphasis
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setStyle("-fx-padding: 10px 0 0 0; -fx-border-color: #dee2e6; -fx-border-width: 1px 0 0 0;");
        Label totalLabelText = new Label("Total:");
        totalLabelText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);
        totalLabel = new Label("$0.00");
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        totalRow.getChildren().addAll(totalLabelText, totalSpacer, totalLabel);
        
        // Savings indicator
        Label savingsLabel = new Label();
        savingsLabel.getStyleClass().add("label-success");
        savingsLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
        savingsLabel.setVisible(false);
        
        totalsSection.getChildren().addAll(
            totalsTitle, 
            itemCountLabel,
            new Separator(),
            subtotalRow, 
            discountRow, 
            totalRow,
            savingsLabel
        );
        
        // Checkout button (placeholder)
        Button checkoutButton = new Button("Proceed to Checkout");
        checkoutButton.getStyleClass().addAll("btn", "btn-success", "btn-large");
        checkoutButton.setPrefWidth(Double.MAX_VALUE);
        checkoutButton.setOnAction(e -> {
            if (shoppingCart.isEmpty()) {
                showAlert("Empty Cart", "Please add some items to your cart before checkout", Alert.AlertType.INFORMATION);
            } else {
                showCheckoutDialog();
            }
        });
        
        cardBody.getChildren().addAll(
            strategySection, 
            new Separator(), 
            totalsSection,
            checkoutButton
        );
        
        section.getChildren().addAll(sectionTitle, cardBody);
        return section;
    }
    
    
    private void showCheckoutDialog() {
        Alert checkoutAlert = new Alert(Alert.AlertType.INFORMATION);
        checkoutAlert.setTitle("Checkout");
        checkoutAlert.setHeaderText("Order Summary");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Order details
        content.getChildren().add(new Label("Customer: " + currentUser.getDisplayName()));
        content.getChildren().add(new Label("Items: " + shoppingCart.getItemCount()));
        content.getChildren().add(new Label("Subtotal: $" + String.format("%.2f", shoppingCart.getSubtotal())));
        content.getChildren().add(new Label("Discount: -$" + String.format("%.2f", shoppingCart.getDiscount())));
        content.getChildren().add(new Label("Strategy: " + shoppingCart.getDiscountStrategy().getStrategyName()));
        
        Label totalLabel = new Label("Total: $" + String.format("%.2f", shoppingCart.getTotal()));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        content.getChildren().add(totalLabel);
        
        content.getChildren().add(new Label("\nThis is a demonstration. No actual payment will be processed."));
        
        checkoutAlert.getDialogPane().setContent(content);
        checkoutAlert.showAndWait();
    }
    
    
    private void updateTotals() {
        double subtotal = shoppingCart.getSubtotal();
        double discount = shoppingCart.getDiscount();
        double total = shoppingCart.getTotal();
        int itemCount = shoppingCart.getItemCount();
        
        // Update labels with animation
        itemCountLabel.setText(itemCount + (itemCount == 1 ? " item" : " items"));
        subtotalLabel.setText(String.format("$%.2f", subtotal));
        discountLabel.setText(String.format("-$%.2f", discount));
        totalLabel.setText(String.format("$%.2f", total));
        
        // Update strategy description
        DiscountStrategy currentStrategy = shoppingCart.getDiscountStrategy();
        strategyDescriptionLabel.setText(currentStrategy.getDescription());
        
        // Add visual feedback for changes
        if (discount > 0) {
            // Highlight savings
            FadeTransition highlight = new FadeTransition(Duration.millis(300), discountLabel);
            highlight.setFromValue(0.5);
            highlight.setToValue(1.0);
            highlight.play();
        }
        
        // Animate total change
        FadeTransition totalAnimation = new FadeTransition(Duration.millis(200), totalLabel);
        totalAnimation.setFromValue(0.7);
        totalAnimation.setToValue(1.0);
        totalAnimation.play();
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply styling to alert
        DialogPane dialogPane = alert.getDialogPane();
        try {
            String css = getClass().getResource("/styles.css").toExternalForm();
            dialogPane.getStylesheets().add(css);
        } catch (Exception e) {
            // CSS loading failed, continue without styling
        }
        
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}