package org.strategy;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Modern login and registration interface for the E-Commerce application.
 * Features professional styling, smooth animations, and user-friendly design.
 */
public class LoginRegisterApp extends Application {
    
    private UserService userService;
    private Stage primaryStage;
    private boolean isLoginMode = true;
    
    // UI Components
    private VBox authCard;
    private VBox formContainer;
    private Label titleLabel;
    private Label subtitleLabel;
    private TextField usernameField;
    private TextField emailField;
    private TextField firstNameField;
    private TextField lastNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button submitButton;
    private Button switchModeButton;
    private Label statusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = UserService.getInstance();
        
        primaryStage.setTitle("ECommerce Pro - Sign In");
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        
        // Create main layout
        createUI();
        
        // Load CSS
        Scene scene = new Scene(createMainLayout(), 400, 600);
        
        try {
            String css = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Play entrance animation
        playEntranceAnimation();
    }
    
    private VBox createMainLayout() {
        VBox root = new VBox();
        root.setStyle("-fx-background: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        
        // Create authentication card
        authCard = new VBox(25);
        authCard.getStyleClass().add("auth-card");
        authCard.setMaxWidth(350);
        authCard.setAlignment(Pos.CENTER);
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Form section
        formContainer = createFormSection();
        
        // Button section
        VBox buttonSection = createButtonSection();
        
        // Status section
        statusLabel = new Label();
        statusLabel.setVisible(false);
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        
        // Demo info section
        VBox demoInfo = createDemoInfo();
        
        authCard.getChildren().addAll(
            headerSection,
            formContainer,
            buttonSection,
            statusLabel,
            demoInfo
        );
        
        root.getChildren().add(authCard);
        return root;
    }
    
    private void createUI() {
        // Initialize form fields
        usernameField = new TextField();
        usernameField.getStyleClass().add("modern-text-field");
        usernameField.setPromptText("Username");
        
        emailField = new TextField();
        emailField.getStyleClass().add("modern-text-field");
        emailField.setPromptText("Email");
        
        firstNameField = new TextField();
        firstNameField.getStyleClass().add("modern-text-field");
        firstNameField.setPromptText("First Name");
        
        lastNameField = new TextField();
        lastNameField.getStyleClass().add("modern-text-field");
        lastNameField.setPromptText("Last Name");
        
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("modern-text-field");
        passwordField.setPromptText("Password");
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.getStyleClass().add("modern-text-field");
        confirmPasswordField.setPromptText("Confirm Password");
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        titleLabel = new Label("Welcome Back");
        titleLabel.getStyleClass().add("auth-title");
        
        subtitleLabel = new Label("Sign in to your account");
        subtitleLabel.getStyleClass().add("auth-subtitle");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private VBox createFormSection() {
        VBox form = new VBox(12);
        form.setAlignment(Pos.CENTER);
        
        // Username field (always visible)
        form.getChildren().add(usernameField);
        
        // Email field (register only)
        VBox emailContainer = new VBox();
        emailContainer.getChildren().add(emailField);
        emailContainer.setVisible(false);
        emailContainer.setManaged(false);
        form.getChildren().add(emailContainer);
        
        // Name fields (register only)
        HBox nameContainer = new HBox(8);
        nameContainer.getChildren().addAll(firstNameField, lastNameField);
        nameContainer.setVisible(false);
        nameContainer.setManaged(false);
        form.getChildren().add(nameContainer);
        
        // Password field (always visible)
        form.getChildren().add(passwordField);
        
        // Confirm password field (register only)
        VBox confirmContainer = new VBox();
        confirmContainer.getChildren().add(confirmPasswordField);
        confirmContainer.setVisible(false);
        confirmContainer.setManaged(false);
        form.getChildren().add(confirmContainer);
        
        return form;
    }
    
    private VBox createButtonSection() {
        VBox buttons = new VBox(12);
        buttons.setAlignment(Pos.CENTER);
        
        submitButton = new Button("Sign In");
        submitButton.getStyleClass().addAll("btn", "btn-primary", "btn-large");
        submitButton.setPrefWidth(300);
        submitButton.setOnAction(e -> handleSubmit());
        
        switchModeButton = new Button("Don't have an account? Sign up");
        switchModeButton.getStyleClass().addAll("btn", "btn-secondary");
        switchModeButton.setOnAction(e -> switchMode());
        
        buttons.getChildren().addAll(submitButton, switchModeButton);
        return buttons;
    }
    
    private VBox createDemoInfo() {
        VBox demoInfo = new VBox(10);
        demoInfo.setAlignment(Pos.CENTER);
        
        Label demoTitle = new Label("Demo Accounts");
        demoTitle.getStyleClass().addAll("label-secondary");
        demoTitle.setStyle("-fx-font-weight: bold;");
        
        VBox accountsList = new VBox(5);
        accountsList.setAlignment(Pos.CENTER);
        
        Label adminAccount = new Label("👤 Admin: admin / admin123");
        adminAccount.getStyleClass().add("label-secondary");
        adminAccount.setStyle("-fx-font-size: 11px;");
        
        Label customerAccount = new Label("👤 Customer: john_doe / password123");
        customerAccount.getStyleClass().add("label-secondary");
        customerAccount.setStyle("-fx-font-size: 11px;");
        
        Label managerAccount = new Label("👤 Manager: manager / manager123");
        managerAccount.getStyleClass().add("label-secondary");
        managerAccount.setStyle("-fx-font-size: 11px;");
        
        accountsList.getChildren().addAll(adminAccount, customerAccount, managerAccount);
        demoInfo.getChildren().addAll(demoTitle, accountsList);
        
        return demoInfo;
    }
    
    private void handleSubmit() {
        clearStatus();
        
        if (isLoginMode) {
            handleLogin();
        } else {
            handleRegistration();
        }
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields", "error");
            return;
        }
        
        // Add loading state
        submitButton.setDisable(true);
        submitButton.setText("Signing In...");
        
        // Simulate network delay for better UX
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(800), e -> {
            UserService.LoginResult result = userService.login(username, password);
            
            if (result.isSuccess()) {
                showStatus("Login successful! Welcome back, " + result.getUser().getDisplayName(), "success");
                
                // Transition to main application after a brief delay
                Timeline transition = new Timeline(new KeyFrame(Duration.millis(1500), event -> {
                    openMainApplication(result.getUser());
                }));
                transition.play();
                
            } else {
                showStatus(result.getMessage(), "error");
                submitButton.setDisable(false);
                submitButton.setText("Sign In");
            }
        }));
        delay.play();
    }
    
    private void handleRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all required fields", "error");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatus("Passwords do not match", "error");
            return;
        }
        
        // Add loading state
        submitButton.setDisable(true);
        submitButton.setText("Creating Account...");
        
        // Simulate network delay
        Timeline delay = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            UserService.RegistrationResult result = userService.registerUser(
                username, email, password, firstName, lastName
            );
            
            if (result.isSuccess()) {
                showStatus("Account created successfully! You can now sign in.", "success");
                
                // Switch to login mode after brief delay
                Timeline switchDelay = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
                    switchToLoginMode();
                    usernameField.setText(username); // Pre-fill username
                }));
                switchDelay.play();
                
            } else {
                showStatus(result.getMessage(), "error");
                submitButton.setDisable(false);
                submitButton.setText("Create Account");
            }
        }));
        delay.play();
    }
    
    private void switchMode() {
        if (isLoginMode) {
            switchToRegisterMode();
        } else {
            switchToLoginMode();
        }
    }
    
    private void switchToLoginMode() {
        isLoginMode = true;
        
        // Update header
        titleLabel.setText("Welcome Back");
        subtitleLabel.setText("Sign in to your account");
        
        // Update form visibility with animation
        playFormTransition(() -> {
            emailField.getParent().setVisible(false);
            emailField.getParent().setManaged(false);
            firstNameField.getParent().setVisible(false);
            firstNameField.getParent().setManaged(false);
            confirmPasswordField.getParent().setVisible(false);
            confirmPasswordField.getParent().setManaged(false);
        });
        
        // Update buttons
        submitButton.setText("Sign In");
        submitButton.setDisable(false);
        switchModeButton.setText("Don't have an account? Sign up");
        
        // Clear fields
        clearFields();
    }
    
    private void switchToRegisterMode() {
        isLoginMode = false;
        
        // Update header
        titleLabel.setText("Create Account");
        subtitleLabel.setText("Join us today and start shopping");
        
        // Update form visibility with animation
        playFormTransition(() -> {
            emailField.getParent().setVisible(true);
            emailField.getParent().setManaged(true);
            firstNameField.getParent().setVisible(true);
            firstNameField.getParent().setManaged(true);
            confirmPasswordField.getParent().setVisible(true);
            confirmPasswordField.getParent().setManaged(true);
        });
        
        // Update buttons
        submitButton.setText("Create Account");
        submitButton.setDisable(false);
        switchModeButton.setText("Already have an account? Sign in");
        
        // Clear fields
        clearFields();
    }
    
    private void playFormTransition(Runnable changeVisibility) {
        // Fade out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), formContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        fadeOut.setOnFinished(e -> {
            changeVisibility.run();
            
            // Fade in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), formContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        
        fadeOut.play();
    }
    
    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        firstNameField.clear();
        lastNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        clearStatus();
    }
    
    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().clear();
        statusLabel.getStyleClass().addAll("alert", "alert-" + type);
        statusLabel.setVisible(true);
        
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), statusLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private void clearStatus() {
        statusLabel.setVisible(false);
        statusLabel.setText("");
    }
    
    private void playEntranceAnimation() {
        // Initial state
        authCard.setOpacity(0);
        authCard.setTranslateY(30);
        
        // Entrance animation
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(0),
                new KeyValue(authCard.opacityProperty(), 0),
                new KeyValue(authCard.translateYProperty(), 30)
            ),
            new KeyFrame(Duration.millis(600),
                new KeyValue(authCard.opacityProperty(), 1, Interpolator.EASE_OUT),
                new KeyValue(authCard.translateYProperty(), 0, Interpolator.EASE_OUT)
            )
        );
        
        timeline.setDelay(Duration.millis(200));
        timeline.play();
    }
    
    private void openMainApplication(User user) {
        try {
            // Create and show the main application
            ECommerceDiscountApp mainApp = new ECommerceDiscountApp();
            Stage mainStage = new Stage();
            mainApp.start(mainStage);
            
            // Close login window
            primaryStage.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error opening main application: " + e.getMessage(), "error");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}