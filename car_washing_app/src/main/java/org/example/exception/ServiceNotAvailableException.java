package org.example.exception;

/**
 * Exception thrown when service is not available (area mismatch or cleaner unavailable)
 */
public class ServiceNotAvailableException extends RuntimeException {
    private int customerId;
    private int cleanerId;

    public ServiceNotAvailableException(int customerId, int cleanerId) {
        super("Service is not available for customer " + customerId + " with cleaner " + cleanerId);
        this.customerId = customerId;
        this.cleanerId = cleanerId;
    }

    public ServiceNotAvailableException(String message) {
        super(message);
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCleanerId() {
        return cleanerId;
    }
}
