package org.example.config;

import org.example.model.*;
import org.example.repository.CleanerJpaRepository;
import org.example.repository.CustomerJpaRepository;
import org.example.repository.ServiceSubscriptionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * Initialize sample data when application starts
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CleanerJpaRepository cleanerRepository;

    @Autowired
    private CustomerJpaRepository customerRepository;

    @Autowired
    private ServiceSubscriptionJpaRepository subscriptionRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========== INITIALIZING SAMPLE DATA ==========\n");

        // Register Cleaners
        Cleaner cleaner1 = new Cleaner("John Smith", 5, 2500, Area.DOWNTOWN);
        Cleaner cleaner2 = new Cleaner("Mary Johnson", 8, 3000, Area.DOWNTOWN);
        Cleaner cleaner3 = new Cleaner("Bob Wilson", 3, 2000, Area.UPTOWN);
        Cleaner cleaner4 = new Cleaner("Alice Brown", 6, 2800, Area.UPTOWN);
        Cleaner cleaner5 = new Cleaner("Charlie Davis", 4, 2200, Area.SUBURBS);
        Cleaner cleaner6 = new Cleaner("Eve Martinez", 7, 2900, Area.SUBURBS);

        cleanerRepository.save(cleaner1);
        cleanerRepository.save(cleaner2);
        cleanerRepository.save(cleaner3);
        cleanerRepository.save(cleaner4);
        cleanerRepository.save(cleaner5);
        cleanerRepository.save(cleaner6);

        System.out.println("✓ 6 Cleaners registered");

        // Register Customers
        Customer customer1 = new Customer("Client A", "clienta@email.com", "555-0001", Area.DOWNTOWN);
        Customer customer2 = new Customer("Client B", "clientb@email.com", "555-0002", Area.DOWNTOWN);
        Customer customer3 = new Customer("Client C", "clientc@email.com", "555-0003", Area.UPTOWN);
        Customer customer4 = new Customer("Client D", "clientd@email.com", "555-0004", Area.SUBURBS);
        Customer customer5 = new Customer("Client E", "cliente@email.com", "555-0005", Area.SUBURBS);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        customerRepository.save(customer5);

        System.out.println("✓ 5 Customers registered");

        // Create sample subscriptions
        ServiceSubscription sub1 = new ServiceSubscription(1, 1, PackageType.FULL_CLEAN,
            Frequency.WEEKLY, LocalDate.of(2026, 2, 9), LocalDate.of(2026, 5, 9));
        ServiceSubscription sub2 = new ServiceSubscription(1, 2, PackageType.OUTER_CLEAN_WATERING,
            Frequency.MONTHLY, LocalDate.of(2026, 2, 9), LocalDate.of(2026, 12, 9));
        ServiceSubscription sub3 = new ServiceSubscription(3, 3, PackageType.OUTER_CLEAN,
            Frequency.WEEKLY, LocalDate.of(2026, 2, 9), LocalDate.of(2026, 4, 9));

        subscriptionRepository.save(sub1);
        subscriptionRepository.save(sub2);
        subscriptionRepository.save(sub3);

        System.out.println("✓ 3 Sample Subscriptions created");

        System.out.println("\n========== SAMPLE DATA INITIALIZED ==========");
        System.out.println("✓ Access Swagger UI at: http://localhost:8080/swagger-ui.html");
        System.out.println("✓ API Docs at: http://localhost:8080/api-docs");
        System.out.println("✓ H2 Console at: http://localhost:8080/h2-console\n");
    }
}
