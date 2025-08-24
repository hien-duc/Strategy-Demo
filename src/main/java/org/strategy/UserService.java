package org.strategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service class for managing user operations including registration, login,
 * and session management. In a real application, this would interact with
 * a database, but for this demo, it uses in-memory storage.
 */
public class UserService {
    private static UserService instance;
    private final Map<String, User> users;
    private final Map<String, String> userSalts;
    private User currentUser;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Password requirements
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    private UserService() {
        this.users = new HashMap<>();
        this.userSalts = new HashMap<>();
        this.currentUser = null;
        
        // Create some demo users
        createDemoUsers();
    }
    
    /**
     * Gets the singleton instance of UserService.
     * 
     * @return the UserService instance
     */
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    /**
     * Creates demo users for testing purposes.
     */
    private void createDemoUsers() {
        try {
            // Create admin user
            registerUser("admin", "admin@ecommerce.com", "admin123", "Admin", "User", User.UserRole.ADMIN);
            
            // Create regular customer
            registerUser("john_doe", "john@example.com", "password123", "John", "Doe", User.UserRole.CUSTOMER);
            
            // Create manager
            registerUser("manager", "manager@ecommerce.com", "manager123", "Store", "Manager", User.UserRole.MANAGER);
            
        } catch (Exception e) {
            System.err.println("Error creating demo users: " + e.getMessage());
        }
    }
    
    /**
     * Registers a new user with the specified information.
     * 
     * @param username the desired username
     * @param email the user's email address
     * @param password the user's password (plain text)
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param role the user's role
     * @return RegistrationResult indicating success or failure
     */
    public RegistrationResult registerUser(String username, String email, String password, 
                                         String firstName, String lastName, User.UserRole role) {
        // Validate input
        ValidationResult validation = validateRegistrationInput(username, email, password);
        if (!validation.isValid()) {
            return new RegistrationResult(false, validation.getErrorMessage(), null);
        }
        
        // Check if username already exists
        if (users.containsKey(username.toLowerCase())) {
            return new RegistrationResult(false, "Username already exists", null);
        }
        
        // Check if email already exists
        if (users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            return new RegistrationResult(false, "Email address already registered", null);
        }
        
        try {
            // Generate salt and hash password
            String salt = generateSalt();
            String passwordHash = hashPassword(password, salt);
            
            // Create user
            User newUser = new User(username, email, passwordHash, firstName, lastName);
            newUser.setRole(role);
            
            // Store user and salt
            users.put(username.toLowerCase(), newUser);
            userSalts.put(username.toLowerCase(), salt);
            
            return new RegistrationResult(true, "Registration successful", newUser.createSafeUserCopy());
            
        } catch (Exception e) {
            return new RegistrationResult(false, "Registration failed: " + e.getMessage(), null);
        }
    }
    
    /**
     * Registers a new customer user (convenience method).
     * 
     * @param username the desired username
     * @param email the user's email address
     * @param password the user's password
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return RegistrationResult indicating success or failure
     */
    public RegistrationResult registerUser(String username, String email, String password, 
                                         String firstName, String lastName) {
        return registerUser(username, email, password, firstName, lastName, User.UserRole.CUSTOMER);
    }
    
    /**
     * Attempts to log in a user with the provided credentials.
     * 
     * @param username the username
     * @param password the password (plain text)
     * @return LoginResult indicating success or failure
     */
    public LoginResult login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return new LoginResult(false, "Username cannot be empty", null);
        }
        
        if (password == null || password.isEmpty()) {
            return new LoginResult(false, "Password cannot be empty", null);
        }
        
        User user = users.get(username.toLowerCase());
        if (user == null) {
            return new LoginResult(false, "Invalid username or password", null);
        }
        
        if (!user.isActive()) {
            return new LoginResult(false, "Account is deactivated", null);
        }
        
        try {
            String salt = userSalts.get(username.toLowerCase());
            String providedPasswordHash = hashPassword(password, salt);
            
            if (user.getPasswordHash().equals(providedPasswordHash)) {
                user.updateLastLoginDate();
                this.currentUser = user;
                return new LoginResult(true, "Login successful", user.createSafeUserCopy());
            } else {
                return new LoginResult(false, "Invalid username or password", null);
            }
            
        } catch (Exception e) {
            return new LoginResult(false, "Login failed: " + e.getMessage(), null);
        }
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Gets the currently logged-in user.
     * 
     * @return the current user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser != null ? currentUser.createSafeUserCopy() : null;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Changes the password for the current user.
     * 
     * @param currentPassword the current password
     * @param newPassword the new password
     * @return true if password was changed successfully, false otherwise
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        // Validate current password
        LoginResult loginResult = login(currentUser.getUsername(), currentPassword);
        if (!loginResult.isSuccess()) {
            return false;
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        
        try {
            // Generate new salt and hash
            String salt = generateSalt();
            String passwordHash = hashPassword(newPassword, salt);
            
            // Update password
            currentUser.setPasswordHash(passwordHash);
            userSalts.put(currentUser.getUsername().toLowerCase(), salt);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Updates the current user's profile information.
     * 
     * @param email the new email address
     * @param firstName the new first name
     * @param lastName the new last name
     * @return true if update was successful, false otherwise
     */
    public boolean updateProfile(String email, String firstName, String lastName) {
        if (currentUser == null) {
            return false;
        }
        
        if (!isValidEmail(email)) {
            return false;
        }
        
        // Check if email is already used by another user
        Optional<User> existingUser = users.values().stream()
            .filter(user -> !user.getUsername().equals(currentUser.getUsername()))
            .filter(user -> user.getEmail().equalsIgnoreCase(email))
            .findFirst();
        
        if (existingUser.isPresent()) {
            return false; // Email already in use
        }
        
        currentUser.setEmail(email);
        currentUser.setFirstName(firstName != null ? firstName : "");
        currentUser.setLastName(lastName != null ? lastName : "");
        
        return true;
    }
    
    /**
     * Validates registration input.
     * 
     * @param username the username to validate
     * @param email the email to validate
     * @param password the password to validate
     * @return ValidationResult with validation status and error message
     */
    private ValidationResult validateRegistrationInput(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty");
        }
        
        if (username.length() < 3) {
            return new ValidationResult(false, "Username must be at least 3 characters long");
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return new ValidationResult(false, "Username can only contain letters, numbers, and underscores");
        }
        
        if (!isValidEmail(email)) {
            return new ValidationResult(false, "Invalid email address format");
        }
        
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return new ValidationResult(false, "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Validates email format.
     * 
     * @param email the email to validate
     * @return true if email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Generates a random salt for password hashing.
     * 
     * @return a base64-encoded salt string
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hashes a password with the provided salt using SHA-256.
     * 
     * @param password the password to hash
     * @param salt the salt to use
     * @return the hashed password
     * @throws NoSuchAlgorithmException if SHA-256 is not available
     */
    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Base64.getDecoder().decode(salt));
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
    
    /**
     * Result class for registration operations.
     */
    public static class RegistrationResult {
        private final boolean success;
        private final String message;
        private final User user;
        
        public RegistrationResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }
    
    /**
     * Result class for login operations.
     */
    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final User user;
        
        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }
    
    /**
     * Result class for validation operations.
     */
    private static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
}