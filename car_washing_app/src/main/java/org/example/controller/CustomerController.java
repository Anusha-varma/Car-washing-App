package org.example.controller;

import org.example.model.Customer;
import org.example.model.Area;
import org.example.repository.CustomerJpaRepository;
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

/**
 * REST Controller for Customer CRUD operations
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    @Autowired
    private CustomerJpaRepository customerRepository;

    /**
     * Get all customers
     */
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all registered customers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                return ResponseEntity.ok(customer.get());
            } else {
                throw new CustomerNotFoundException(id);
            }
        } catch (CustomerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get customers by area
     */
    @GetMapping("/area/{area}")
    @Operation(summary = "Get customers by area", description = "Retrieve all customers in a specific area")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved customers")
    public ResponseEntity<List<Customer>> getCustomersByArea(@PathVariable Area area) {
        List<Customer> customers = customerRepository.findByArea(area);
        return ResponseEntity.ok(customers);
    }

    /**
     * Create a new customer
     */
    @PostMapping
    @Operation(summary = "Register a new customer", description = "Create a new customer profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or duplicate email")
    })
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            if (customer.getName() == null || customer.getName().trim().isEmpty()) {
                throw new InvalidInputException("Customer name cannot be empty");
            }
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                throw new InvalidInputException("Customer email cannot be empty");
            }

            // Check for duplicate email
            Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
            if (existingCustomer.isPresent()) {
                throw new DuplicateEmailException(customer.getEmail());
            }

            Customer savedCustomer = customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (InvalidInputException | DuplicateEmailException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing customer
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update customer information", description = "Update an existing customer's details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customerDetails) {
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                Customer existingCustomer = customer.get();
                if (customerDetails.getName() != null) {
                    existingCustomer.setName(customerDetails.getName());
                }
                if (customerDetails.getEmail() != null) {
                    // Check if email is unique (excluding current customer)
                    Optional<Customer> emailExists = customerRepository.findByEmail(customerDetails.getEmail());
                    if (emailExists.isPresent() && emailExists.get().getId() != id) {
                        throw new DuplicateEmailException(customerDetails.getEmail());
                    }
                    existingCustomer.setEmail(customerDetails.getEmail());
                }
                if (customerDetails.getPhone() != null) {
                    existingCustomer.setPhone(customerDetails.getPhone());
                }
                if (customerDetails.getArea() != null) {
                    existingCustomer.setArea(customerDetails.getArea());
                }

                Customer updatedCustomer = customerRepository.save(existingCustomer);
                return ResponseEntity.ok(updatedCustomer);
            } else {
                throw new CustomerNotFoundException(id);
            }
        } catch (CustomerNotFoundException | DuplicateEmailException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete a customer
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Remove a customer from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                throw new CustomerNotFoundException(id);
            }
        } catch (CustomerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
