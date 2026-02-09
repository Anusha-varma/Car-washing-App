package org.example.exception;

/**
 * Exception thrown when duplicate email is registered
 */
public class DuplicateEmailException extends RuntimeException {
    private String email;

    public DuplicateEmailException(String email) {
        super("A customer with email '" + email + "' already exists");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
