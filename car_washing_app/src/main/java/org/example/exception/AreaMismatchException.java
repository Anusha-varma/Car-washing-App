package org.example.exception;

/**
 * Exception thrown when area mismatch occurs between cleaner and customer
 */
public class AreaMismatchException extends RuntimeException {
    private String customerArea;
    private String cleanerArea;

    public AreaMismatchException(String customerArea, String cleanerArea) {
        super("Area mismatch: Customer is in " + customerArea + " but Cleaner is in " + cleanerArea);
        this.customerArea = customerArea;
        this.cleanerArea = cleanerArea;
    }

    public AreaMismatchException(String message) {
        super(message);
    }

    public String getCustomerArea() {
        return customerArea;
    }

    public String getCleanerArea() {
        return cleanerArea;
    }
}
