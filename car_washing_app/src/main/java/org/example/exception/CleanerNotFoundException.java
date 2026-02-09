package org.example.exception;

/**
 * Exception thrown when a cleaner is not found in the system
 */
public class CleanerNotFoundException extends RuntimeException {
    private int cleanerId;

    public CleanerNotFoundException(int cleanerId) {
        super("Cleaner with ID " + cleanerId + " not found");
        this.cleanerId = cleanerId;
    }

    public CleanerNotFoundException(String message) {
        super(message);
    }

    public int getCleanerId() {
        return cleanerId;
    }
}
