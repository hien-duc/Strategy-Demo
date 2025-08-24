package org.strategy;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User model class representing a user in the e-commerce system.
 * Contains user authentication information and profile data.
 */
public class User {
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
    private UserRole role;
    private boolean isActive;
    
    /**
     * Enum representing different user roles in the system
     */
    public enum UserRole {
        CUSTOMER("Customer"),
        ADMIN("Administrator"),
        MANAGER("Manager");
        
        private final String displayName;
        
        UserRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Creates a new User with the specified details.
     * 
     * @param username the unique username
     * @param email the user's email address
     * @param passwordHash the hashed password
     * @param firstName the user's first name
     * @param lastName the user's last name
     */
    public User(String username, String email, String passwordHash, 
                String firstName, String lastName) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash cannot be null");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
        this.registrationDate = LocalDateTime.now();
        this.role = UserRole.CUSTOMER; // Default role
        this.isActive = true;
    }
    
    /**
     * Creates a new User with minimal information (for quick registration).
     * 
     * @param username the unique username
     * @param email the user's email address
     * @param passwordHash the hashed password
     */
    public User(String username, String email, String passwordHash) {
        this(username, email, passwordHash, "", "");
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getFullName() {
        if (firstName.isEmpty() && lastName.isEmpty()) {
            return username;
        }
        return (firstName + " " + lastName).trim();
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Setters
    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash cannot be null");
    }
    
    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
    }
    
    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
    }
    
    public void setRole(UserRole role) {
        this.role = Objects.requireNonNull(role, "Role cannot be null");
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Updates the last login date to the current time.
     */
    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }
    
    /**
     * Checks if the user has administrative privileges.
     * 
     * @return true if the user is an admin or manager
     */
    public boolean hasAdminPrivileges() {
        return role == UserRole.ADMIN || role == UserRole.MANAGER;
    }
    
    /**
     * Gets the user's display name for the UI.
     * 
     * @return the full name if available, otherwise the username
     */
    public String getDisplayName() {
        String fullName = getFullName();
        return fullName.equals(username) ? username : fullName + " (" + username + ")";
    }
    
    /**
     * Gets the user's initials for avatar display.
     * 
     * @return the user's initials (first letter of first and last name)
     */
    public String getInitials() {
        if (!firstName.isEmpty() && !lastName.isEmpty()) {
            return (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();
        } else if (!firstName.isEmpty()) {
            return firstName.substring(0, 1).toUpperCase();
        } else if (!lastName.isEmpty()) {
            return lastName.substring(0, 1).toUpperCase();
        } else {
            return username.substring(0, Math.min(2, username.length())).toUpperCase();
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return Objects.equals(username, user.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    @Override
    public String toString() {
        return String.format("User{username='%s', email='%s', fullName='%s', role=%s, active=%s}",
                           username, email, getFullName(), role, isActive);
    }
    
    /**
     * Creates a copy of this user without sensitive information (password hash).
     * 
     * @return a new User object without password information
     */
    public User createSafeUserCopy() {
        User safeUser = new User(username, email, "", firstName, lastName);
        safeUser.role = this.role;
        safeUser.isActive = this.isActive;
        safeUser.registrationDate = this.registrationDate;
        safeUser.lastLoginDate = this.lastLoginDate;
        return safeUser;
    }
}