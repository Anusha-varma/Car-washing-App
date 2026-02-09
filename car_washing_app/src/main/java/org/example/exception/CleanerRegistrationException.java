package org.example.exception;

/**
 * Exception thrown when cleaner registration fails
 */
public class CleanerRegistrationException extends RuntimeException {
    public CleanerRegistrationException(String message) {
        super(message);
    }

    public CleanerRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
