package org.example.exception;

/**
 * Exception thrown when cleaner is not available for booking
 */
public class CleanerUnavailableException extends RuntimeException {
    private int cleanerId;

    public CleanerUnavailableException(int cleanerId) {
        super("Cleaner with ID " + cleanerId + " is not available");
        this.cleanerId = cleanerId;
    }

    public CleanerUnavailableException(String message) {
        super(message);
    }

    public int getCleanerId() {
        return cleanerId;
    }
}
