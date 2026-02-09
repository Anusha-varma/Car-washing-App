package org.example.service;

import org.example.model.Area;
import org.example.model.Cleaner;
import org.example.model.Customer;
import org.example.repository.CleanerRepository;
import org.example.repository.CustomerRepository;
import org.example.exception.*;
import java.util.List;

/**
 * Service for checking service availability based on area matching
 */
public class ServiceAvailabilityService {
    private final CleanerRepository cleanerRepository;
    private final CustomerRepository customerRepository;

    public ServiceAvailabilityService(CleanerRepository cleanerRepository, CustomerRepository customerRepository) {
        this.cleanerRepository = cleanerRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Check if a service is available for a customer
     * Service is available only if cleaner and customer are in the same area
     */
    public boolean isServiceAvailable(int customerId, int cleanerId) {
        Customer customer = customerRepository.findById(customerId);
        Cleaner cleaner = cleanerRepository.findById(cleanerId);

        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        if (cleaner == null) {
            throw new CleanerNotFoundException(cleanerId);
        }

        return isServiceAvailable(customer, cleaner);
    }

    /**
     * Check if a service is available for customer and cleaner
     */
    public boolean isServiceAvailable(Customer customer, Cleaner cleaner) {
        if (customer == null) {
            throw new CustomerNotFoundException("Customer cannot be null");
        }
        if (cleaner == null) {
            throw new CleanerNotFoundException("Cleaner cannot be null");
        }

        if (customer.getArea() != cleaner.getArea()) {
            throw new AreaMismatchException(
                    customer.getArea().getDisplayName(),
                    cleaner.getArea().getDisplayName()
            );
        }

        if (!cleaner.isAvailable()) {
            throw new CleanerUnavailableException(cleaner.getId());
        }

        return true;
    }

    /**
     * Get available cleaners for a customer in the same area
     */
    public List<Cleaner> getAvailableCleanersForCustomer(Customer customer) {
        return cleanerRepository.findByAreaAndAvailable(customer.getArea());
    }

    /**
     * Get available cleaners for a customer ID in the same area
     */
    public List<Cleaner> getAvailableCleanersForCustomer(int customerId) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            return List.of();
        }
        return getAvailableCleanersForCustomer(customer);
    }

    /**
     * Get available cleaners in a specific area with minimum experience
     */
    public List<Cleaner> getAvailableCleanersByAreaAndExperience(Area area, int minYears) {
        return cleanerRepository.findByAreaAndAvailable(area).stream()
                .filter(c -> c.getExperience() >= minYears)
                .toList();
    }
}
