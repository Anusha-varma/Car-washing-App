package org.example.exception;

/**
 * Exception thrown when a customer is not found in the system
 */
public class CustomerNotFoundException extends RuntimeException {
    private int customerId;

    public CustomerNotFoundException(int customerId) {
        super("Customer with ID " + customerId + " not found");
        this.customerId = customerId;
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public int getCustomerId() {
        return customerId;
    }
}
