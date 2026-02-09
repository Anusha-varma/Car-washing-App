package org.example.controller;

import org.example.model.ServiceSubscription;
import org.example.model.Customer;
import org.example.model.Cleaner;
import org.example.repository.ServiceSubscriptionJpaRepository;
import org.example.repository.CustomerJpaRepository;
import org.example.repository.CleanerJpaRepository;
import org.example.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 * REST Controller for ServiceSubscription CRUD operations
 */
@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription Management", description = "APIs for managing service subscriptions")
public class ServiceSubscriptionController {

    @Autowired
    private ServiceSubscriptionJpaRepository subscriptionRepository;

    @Autowired
    private CustomerJpaRepository customerRepository;

    @Autowired
    private CleanerJpaRepository cleanerRepository;

    /**
     * Get all subscriptions
     */
    @GetMapping
    @Operation(summary = "Get all subscriptions", description = "Retrieve all service subscriptions")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all subscriptions")
    public ResponseEntity<List<ServiceSubscription>> getAllSubscriptions() {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findAll();
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Get subscription by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID", description = "Retrieve a specific subscription by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subscription found"),
        @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<ServiceSubscription> getSubscriptionById(@PathVariable int id) {
        try {
            Optional<ServiceSubscription> subscription = subscriptionRepository.findById(id);
            if (subscription.isPresent()) {
                return ResponseEntity.ok(subscription.get());
            } else {
                throw new ServiceSubscriptionNotFoundException(id);
            }
        } catch (ServiceSubscriptionNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get subscriptions by customer ID
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get subscriptions by customer", description = "Retrieve all subscriptions for a customer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved subscriptions")
    public ResponseEntity<List<ServiceSubscription>> getSubscriptionsByCustomer(@PathVariable int customerId) {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Get active subscriptions by customer ID
     */
    @GetMapping("/customer/{customerId}/active")
    @Operation(summary = "Get active subscriptions by customer", description = "Retrieve all active subscriptions for a customer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active subscriptions")
    public ResponseEntity<List<ServiceSubscription>> getActiveSubscriptionsByCustomer(@PathVariable int customerId) {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findByCustomerIdAndActiveTrue(customerId);
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Get subscriptions by cleaner ID
     */
    @GetMapping("/cleaner/{cleanerId}")
    @Operation(summary = "Get subscriptions by cleaner", description = "Retrieve all subscriptions assigned to a cleaner")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved subscriptions")
    public ResponseEntity<List<ServiceSubscription>> getSubscriptionsByCleaner(@PathVariable int cleanerId) {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findByCleanerId(cleanerId);
        return ResponseEntity.ok(subscriptions);
    }

    /**
     * Create a new subscription
     */
    @PostMapping
    @Operation(summary = "Book a new service", description = "Create a new service subscription")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking details")
    })
    public ResponseEntity<ServiceSubscription> createSubscription(@RequestBody ServiceSubscription subscription) {
        try {
            // Validate customer exists
            Optional<Customer> customer = customerRepository.findById(subscription.getCustomerId());
            if (!customer.isPresent()) {
                throw new CustomerNotFoundException(subscription.getCustomerId());
            }

            // Validate cleaner exists
            Optional<Cleaner> cleaner = cleanerRepository.findById(subscription.getCleanerId());
            if (!cleaner.isPresent()) {
                throw new CleanerNotFoundException(subscription.getCleanerId());
            }

            Customer cust = customer.get();
            Cleaner cleanr = cleaner.get();

            // Check if cleaner is available
            if (!cleanr.isAvailable()) {
                throw new CleanerUnavailableException(subscription.getCleanerId());
            }

            // Check area match (CRITICAL)
            if (cust.getArea() != cleanr.getArea()) {
                throw new AreaMismatchException(
                    cust.getArea().getDisplayName(),
                    cleanr.getArea().getDisplayName()
                );
            }

            // Validate dates
            if (subscription.getStartDate() == null || subscription.getEndDate() == null) {
                throw new InvalidDateRangeException("Start and end dates cannot be null");
            }
            if (subscription.getStartDate().isAfter(subscription.getEndDate())) {
                throw new InvalidDateRangeException("Start date must be before end date");
            }

            // Calculate total price
            subscription.setTotalPrice(subscription.calculatePrice());
            subscription.setActive(true);

            ServiceSubscription savedSubscription = subscriptionRepository.save(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubscription);
        } catch (CustomerNotFoundException | CleanerNotFoundException | CleanerUnavailableException |
                 AreaMismatchException | InvalidDateRangeException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing subscription
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update subscription", description = "Update an existing service subscription")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subscription updated successfully"),
        @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<ServiceSubscription> updateSubscription(@PathVariable int id,
                                                                  @RequestBody ServiceSubscription subscriptionDetails) {
        try {
            Optional<ServiceSubscription> subscription = subscriptionRepository.findById(id);
            if (subscription.isPresent()) {
                ServiceSubscription existingSubscription = subscription.get();

                if (subscriptionDetails.getEndDate() != null) {
                    existingSubscription.setEndDate(subscriptionDetails.getEndDate());
                    existingSubscription.setTotalPrice(existingSubscription.calculatePrice());
                }

                existingSubscription.setActive(subscriptionDetails.isActive());

                ServiceSubscription updatedSubscription = subscriptionRepository.save(existingSubscription);
                return ResponseEntity.ok(updatedSubscription);
            } else {
                throw new ServiceSubscriptionNotFoundException(id);
            }
        } catch (ServiceSubscriptionNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Cancel a subscription
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel subscription", description = "Cancel an active service subscription")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subscription cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<ServiceSubscription> cancelSubscription(@PathVariable int id) {
        try {
            Optional<ServiceSubscription> subscription = subscriptionRepository.findById(id);
            if (subscription.isPresent()) {
                ServiceSubscription existingSubscription = subscription.get();
                if (!existingSubscription.isActive()) {
                    throw new InvalidBookingException("Subscription is already inactive");
                }
                existingSubscription.setActive(false);
                ServiceSubscription updatedSubscription = subscriptionRepository.save(existingSubscription);
                return ResponseEntity.ok(updatedSubscription);
            } else {
                throw new ServiceSubscriptionNotFoundException(id);
            }
        } catch (ServiceSubscriptionNotFoundException | InvalidBookingException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Delete a subscription
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subscription", description = "Remove a subscription from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Subscription deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    public ResponseEntity<Void> deleteSubscription(@PathVariable int id) {
        try {
            if (subscriptionRepository.existsById(id)) {
                subscriptionRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                throw new ServiceSubscriptionNotFoundException(id);
            }
        } catch (ServiceSubscriptionNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
