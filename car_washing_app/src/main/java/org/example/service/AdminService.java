package org.example.service;

import org.example.model.Area;
import org.example.model.Cleaner;
import org.example.model.Customer;
import org.example.repository.CleanerRepository;
import org.example.repository.CustomerRepository;
import org.example.exception.*;
import java.util.List;

/**
 * Service for administrative tasks - managing cleaners and customers
 */
public class AdminService {
    private final CleanerRepository cleanerRepository;
    private final CustomerRepository customerRepository;

    public AdminService(CleanerRepository cleanerRepository, CustomerRepository customerRepository) {
        this.cleanerRepository = cleanerRepository;
        this.customerRepository = customerRepository;
    }

    // ========== CLEANER MANAGEMENT ==========

    /**
     * Register a new cleaner
     */
    public Cleaner registerCleaner(String name, int experience, double salary, Area area) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new InvalidInputException("Cleaner name cannot be empty");
            }
            if (experience < 0) {
                throw new InvalidInputException("Experience cannot be negative");
            }
            if (salary < 0) {
                throw new InvalidInputException("Salary cannot be negative");
            }
            if (area == null) {
                throw new InvalidInputException("Area must be selected");
            }

            Cleaner cleaner = new Cleaner(name, experience, salary, area);
            Cleaner savedCleaner = cleanerRepository.save(cleaner);
            System.out.println("✓ Cleaner registered: " + savedCleaner);
            return savedCleaner;
        } catch (InvalidInputException e) {
            throw new CleanerRegistrationException("Failed to register cleaner: " + e.getMessage());
        }
    }

    /**
     * Get all cleaners
     */
    public List<Cleaner> getAllCleaners() {
        return cleanerRepository.findAll();
    }

    /**
     * Get cleaners in a specific area
     */
    public List<Cleaner> getCleanersByArea(Area area) {
        return cleanerRepository.findByArea(area);
    }

    /**
     * Get available cleaners in a specific area
     */
    public List<Cleaner> getAvailableCleanersByArea(Area area) {
        return cleanerRepository.findByAreaAndAvailable(area);
    }

    /**
     * Get cleaners with minimum experience
     */
    public List<Cleaner> getCleanersByExperience(int minYears) {
        return cleanerRepository.findByExperience(minYears);
    }

    /**
     * Update cleaner availability status
     */
    public boolean updateCleanerAvailability(int cleanerId, boolean available) {
        Cleaner cleaner = cleanerRepository.findById(cleanerId);
        if (cleaner == null) {
            throw new CleanerNotFoundException(cleanerId);
        }
        cleaner.setAvailable(available);
        return cleanerRepository.update(cleaner);
    }

    /**
     * Update cleaner salary
     */
    public boolean updateCleanerSalary(int cleanerId, double newSalary) {
        Cleaner cleaner = cleanerRepository.findById(cleanerId);
        if (cleaner == null) {
            throw new CleanerNotFoundException(cleanerId);
        }
        if (newSalary < 0) {
            throw new InvalidInputException("Salary cannot be negative");
        }
        cleaner.setSalary(newSalary);
        return cleanerRepository.update(cleaner);
    }

    /**
     * Remove a cleaner
     */
    public boolean removeCleaner(int cleanerId) {
        if (!cleanerRepository.delete(cleanerId)) {
            throw new CleanerNotFoundException(cleanerId);
        }
        return true;
    }

    // ========== CUSTOMER MANAGEMENT ==========

    /**
     * Register a new customer
     */
    public Customer registerCustomer(String name, String email, String phone, Area area) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new InvalidInputException("Customer name cannot be empty");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new InvalidInputException("Customer email cannot be empty");
            }
            if (area == null) {
                throw new InvalidInputException("Area must be selected");
            }

            // Check for duplicate email
            if (customerRepository.findByEmail(email) != null) {
                throw new DuplicateEmailException(email);
            }

            Customer customer = new Customer(name, email, phone, area);
            Customer savedCustomer = customerRepository.save(customer);
            System.out.println("✓ Customer registered: " + savedCustomer);
            return savedCustomer;
        } catch (InvalidInputException | DuplicateEmailException e) {
            throw new CustomerRegistrationException("Failed to register customer: " + e.getMessage());
        }
    }

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Get customers in a specific area
     */
    public List<Customer> getCustomersByArea(Area area) {
        return customerRepository.findByArea(area);
    }

    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        return customer;
    }

    /**
     * Get cleaner by ID
     */
    public Cleaner getCleanerById(int cleanerId) {
        Cleaner cleaner = cleanerRepository.findById(cleanerId);
        if (cleaner == null) {
            throw new CleanerNotFoundException(cleanerId);
        }
        return cleaner;
    }

    /**
     * Update customer info
     */
    public boolean updateCustomer(int customerId, String name, String email, String phone, Area area) {
        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setArea(area);
        return customerRepository.update(customer);
    }

    /**
     * Remove a customer
     */
    public boolean removeCustomer(int customerId) {
        if (!customerRepository.delete(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        return true;
    }

    /**
     * Print all cleaners info
     */
    public void printAllCleaners() {
        List<Cleaner> cleaners = getAllCleaners();
        if (cleaners.isEmpty()) {
            System.out.println("No cleaners registered.");
            return;
        }
        System.out.println("\n========== ALL CLEANERS ==========");
        for (Cleaner cleaner : cleaners) {
            System.out.println(cleaner);
        }
    }

    /**
     * Print all customers info
     */
    public void printAllCustomers() {
        List<Customer> customers = getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }
        System.out.println("\n========== ALL CUSTOMERS ==========");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
}
