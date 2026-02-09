package org.example.exception;

/**
 * Exception thrown when customer registration fails
 */
public class CustomerRegistrationException extends RuntimeException {
    public CustomerRegistrationException(String message) {
        super(message);
    }

    public CustomerRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
