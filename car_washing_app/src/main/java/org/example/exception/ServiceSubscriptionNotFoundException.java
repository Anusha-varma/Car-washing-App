package org.example.exception;

/**
 * Exception thrown when a service subscription is not found
 */
public class ServiceSubscriptionNotFoundException extends RuntimeException {
    private int subscriptionId;

    public ServiceSubscriptionNotFoundException(int subscriptionId) {
        super("Service subscription with ID " + subscriptionId + " not found");
        this.subscriptionId = subscriptionId;
    }

    public ServiceSubscriptionNotFoundException(String message) {
        super(message);
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }
}
