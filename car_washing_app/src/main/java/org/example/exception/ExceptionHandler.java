package org.example.exception;

/**
 * Global exception handler utility for logging and displaying user-friendly error messages
 * NOTE: This file is kept for backward compatibility. Use AppExceptionHandler instead.
 */
@Deprecated
public class ExceptionHandler {

    public static void handleException(Exception e) {
        if (e instanceof CleanerNotFoundException) {
            System.out.println("ERROR: " + e.getMessage());
        } else if (e instanceof CustomerNotFoundException) {
            System.out.println("ERROR: " + e.getMessage());
        } else if (e instanceof ServiceSubscriptionNotFoundException) {
            System.out.println("ERROR: " + e.getMessage());
        } else if (e instanceof ServiceNotAvailableException) {
            System.out.println("ERROR: Service Unavailable - " + e.getMessage());
        } else if (e instanceof InvalidBookingException) {
            System.out.println("ERROR: Invalid Booking - " + e.getMessage());
        } else if (e instanceof CleanerRegistrationException) {
            System.out.println("ERROR: Cleaner Registration Failed - " + e.getMessage());
        } else if (e instanceof CustomerRegistrationException) {
            System.out.println("ERROR: Customer Registration Failed - " + e.getMessage());
        } else if (e instanceof DuplicateEmailException) {
            System.out.println("ERROR: Duplicate Email - " + e.getMessage());
        } else if (e instanceof InvalidInputException) {
            System.out.println("ERROR: Invalid Input - " + e.getMessage());
        } else if (e instanceof InvalidDateRangeException) {
            System.out.println("ERROR: Invalid Date Range - " + e.getMessage());
        } else if (e instanceof CleanerUnavailableException) {
            System.out.println("ERROR: Cleaner Unavailable - " + e.getMessage());
        } else if (e instanceof AreaMismatchException) {
            System.out.println("ERROR: Area Mismatch - " + e.getMessage());
        } else {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void logException(Exception e) {
        System.err.println("[EXCEPTION LOG] " + e.getClass().getSimpleName() + ": " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("[CAUSED BY] " + e.getCause().getMessage());
        }
    }
}
