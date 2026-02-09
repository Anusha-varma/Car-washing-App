package org.example.service;

import org.example.model.*;
import org.example.repository.CleanerRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.ServiceSubscriptionRepository;
import org.example.exception.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing service bookings and subscriptions
 */
public class BookingService {
    private final ServiceSubscriptionRepository subscriptionRepository;
    private final CleanerRepository cleanerRepository;
    private final CustomerRepository customerRepository;
    private final ServiceAvailabilityService availabilityService;

    public BookingService(ServiceSubscriptionRepository subscriptionRepository,
                         CleanerRepository cleanerRepository,
                         CustomerRepository customerRepository,
                         ServiceAvailabilityService availabilityService) {
        this.subscriptionRepository = subscriptionRepository;
        this.cleanerRepository = cleanerRepository;
        this.customerRepository = customerRepository;
        this.availabilityService = availabilityService;
    }

    /**
     * Book a service for a customer
     * Validates area matching before booking
     */
    public ServiceSubscription bookService(int customerId, int cleanerId, PackageType packageType,
                                          Frequency frequency, LocalDate startDate, LocalDate endDate) {
        try {
            // Validate customer and cleaner exist
            Customer customer = customerRepository.findById(customerId);
            Cleaner cleaner = cleanerRepository.findById(cleanerId);

            if (customer == null) {
                throw new CustomerNotFoundException(customerId);
            }
            if (cleaner == null) {
                throw new CleanerNotFoundException(cleanerId);
            }

            // Check if cleaner is available
            if (!cleaner.isAvailable()) {
                throw new CleanerUnavailableException(cleanerId);
            }

            // Check if service is available (same area)
            if (customer.getArea() != cleaner.getArea()) {
                throw new AreaMismatchException(
                        customer.getArea().getDisplayName(),
                        cleaner.getArea().getDisplayName()
                );
            }

            // Validate dates
            if (startDate == null || endDate == null) {
                throw new InvalidDateRangeException("Start and end dates cannot be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new InvalidDateRangeException("Start date must be before end date");
            }

            // Create subscription
            ServiceSubscription subscription = new ServiceSubscription(
                    customerId, cleanerId, packageType, frequency, startDate, endDate
            );

            subscription.setTotalPrice(subscription.calculatePrice());

            // Save subscription
            ServiceSubscription savedSubscription = subscriptionRepository.save(subscription);

            System.out.println("✓ Service booked successfully!");
            System.out.println("  Subscription ID: " + savedSubscription.getId());
            System.out.println("  Package: " + packageType.getName());
            System.out.println("  Frequency: " + frequency.getDisplayName());
            System.out.println("  Period: " + startDate + " to " + endDate);
            System.out.println("  Total Price: $" + String.format("%.2f", savedSubscription.getTotalPrice()));

            return savedSubscription;
        } catch (CustomerNotFoundException | CleanerNotFoundException | CleanerUnavailableException |
                 AreaMismatchException | InvalidDateRangeException e) {
            throw new InvalidBookingException("Booking failed: " + e.getMessage());
        }
    }

    /**
     * Cancel an active subscription
     */
    public boolean cancelSubscription(int subscriptionId) {
        ServiceSubscription subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription == null) {
            throw new ServiceSubscriptionNotFoundException(subscriptionId);
        }
        if (!subscription.isActive()) {
            throw new InvalidBookingException("Subscription is already inactive");
        }
        subscription.setActive(false);
        return subscriptionRepository.update(subscription);
    }

    /**
     * Get all active subscriptions for a customer
     */
    public List<ServiceSubscription> getCustomerActiveSubscriptions(int customerId) {
        return subscriptionRepository.findByCustomerIdAndActive(customerId);
    }

    /**
     * Get all subscriptions for a customer
     */
    public List<ServiceSubscription> getCustomerSubscriptions(int customerId) {
        return subscriptionRepository.findByCustomerId(customerId);
    }

    /**
     * Get all subscriptions for a cleaner
     */
    public List<ServiceSubscription> getCleanerAssignments(int cleanerId) {
        return subscriptionRepository.findByCleanerId(cleanerId);
    }

    /**
     * Get all active subscriptions
     */
    public List<ServiceSubscription> getAllActiveSubscriptions() {
        return subscriptionRepository.findActiveSubscriptions();
    }
}
