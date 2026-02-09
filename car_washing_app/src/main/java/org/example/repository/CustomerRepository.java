package org.example.repository;

import org.example.model.Area;
import org.example.model.Customer;
import java.util.*;

/**
 * Repository for managing Customer entities
 */
public class CustomerRepository {
    private Map<Integer, Customer> customers = new HashMap<>();
    private int nextId = 1;

    public Customer save(Customer customer) {
        if (customer.getId() == 0) {
            customer.setId(nextId++);
        }
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer findById(int id) {
        return customers.get(id);
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    public List<Customer> findByArea(Area area) {
        return customers.values().stream()
                .filter(c -> c.getArea() == area)
                .toList();
    }

    public Customer findByEmail(String email) {
        return customers.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public boolean update(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            customers.put(customer.getId(), customer);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        return customers.remove(id) != null;
    }

    public void clear() {
        customers.clear();
        nextId = 1;
    }
}
