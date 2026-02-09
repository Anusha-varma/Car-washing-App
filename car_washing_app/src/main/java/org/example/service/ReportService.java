package org.example.service;

import org.example.model.*;
import org.example.repository.CleanerRepository;
import org.example.repository.ServiceSubscriptionRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for generating reports and analytics
 */
public class ReportService {
    private final ServiceSubscriptionRepository subscriptionRepository;
    private final CleanerRepository cleanerRepository;

    public ReportService(ServiceSubscriptionRepository subscriptionRepository,
                        CleanerRepository cleanerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.cleanerRepository = cleanerRepository;
    }

    /**
     * Print revenue report
     */
    public void printRevenueReport() {
        List<ServiceSubscription> allSubscriptions = subscriptionRepository.findAll();
        double totalRevenue = allSubscriptions.stream()
                .mapToDouble(ServiceSubscription::getTotalPrice)
                .sum();

        double activeRevenue = subscriptionRepository.findActiveSubscriptions().stream()
                .mapToDouble(ServiceSubscription::getTotalPrice)
                .sum();

        System.out.println("\n========== REVENUE REPORT ==========");
        System.out.println("Total Revenue (All): $" + String.format("%.2f", totalRevenue));
        System.out.println("Active Revenue: $" + String.format("%.2f", activeRevenue));
        System.out.println("Total Subscriptions: " + allSubscriptions.size());
        System.out.println("Active Subscriptions: " + subscriptionRepository.findActiveSubscriptions().size());
    }

    /**
     * Print revenue by package type
     */
    public void printRevenueByPackage() {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findActiveSubscriptions();
        Map<PackageType, Double> revenueByPackage = subscriptions.stream()
                .collect(Collectors.groupingBy(
                        ServiceSubscription::getPackageType,
                        Collectors.summingDouble(ServiceSubscription::getTotalPrice)
                ));

        System.out.println("\n========== REVENUE BY PACKAGE ==========");
        revenueByPackage.forEach((packageType, revenue) ->
                System.out.println(packageType.getName() + ": $" + String.format("%.2f", revenue))
        );
    }

    /**
     * Print revenue by frequency
     */
    public void printRevenueByFrequency() {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findActiveSubscriptions();
        Map<Frequency, Double> revenueByFrequency = subscriptions.stream()
                .collect(Collectors.groupingBy(
                        ServiceSubscription::getFrequency,
                        Collectors.summingDouble(ServiceSubscription::getTotalPrice)
                ));

        System.out.println("\n========== REVENUE BY FREQUENCY ==========");
        revenueByFrequency.forEach((frequency, revenue) ->
                System.out.println(frequency.getDisplayName() + ": $" + String.format("%.2f", revenue))
        );
    }

    /**
     * Print cleaner performance report
     */
    public void printCleanerPerformanceReport() {
        List<Cleaner> cleaners = cleanerRepository.findAll();
        System.out.println("\n========== CLEANER PERFORMANCE REPORT ==========");

        for (Cleaner cleaner : cleaners) {
            List<ServiceSubscription> assignments = subscriptionRepository.findByCleanerId(cleaner.getId());
            long activeAssignments = assignments.stream().filter(ServiceSubscription::isActive).count();
            double totalEarnings = assignments.stream().mapToDouble(ServiceSubscription::getTotalPrice).sum();

            System.out.println("\nCleaner: " + cleaner.getName());
            System.out.println("  Area: " + cleaner.getArea().getDisplayName());
            System.out.println("  Experience: " + cleaner.getExperience() + " years");
            System.out.println("  Salary: $" + String.format("%.2f", cleaner.getSalary()));
            System.out.println("  Total Assignments: " + assignments.size());
            System.out.println("  Active Assignments: " + activeAssignments);
            System.out.println("  Total Generated Revenue: $" + String.format("%.2f", totalEarnings));
            System.out.println("  Status: " + (cleaner.isAvailable() ? "Available" : "Unavailable"));
        }
    }

    /**
     * Print subscription details report
     */
    public void printSubscriptionDetailsReport() {
        List<ServiceSubscription> subscriptions = subscriptionRepository.findAll();
        if (subscriptions.isEmpty()) {
            System.out.println("\nNo subscriptions found.");
            return;
        }

        System.out.println("\n========== SUBSCRIPTION DETAILS REPORT ==========");
        for (ServiceSubscription sub : subscriptions) {
            System.out.println("\n" + sub);
        }
    }

    /**
     * Print area coverage report
     */
    public void printAreaCoverageReport() {
        Map<Area, List<Cleaner>> cleanersByArea = cleanerRepository.findAll().stream()
                .collect(Collectors.groupingBy(Cleaner::getArea));

        System.out.println("\n========== AREA COVERAGE REPORT ==========");
        for (Area area : Area.values()) {
            List<Cleaner> cleanersInArea = cleanersByArea.getOrDefault(area, List.of());
            long availableCount = cleanersInArea.stream().filter(Cleaner::isAvailable).count();

            System.out.println("\nArea: " + area.getDisplayName());
            System.out.println("  Total Cleaners: " + cleanersInArea.size());
            System.out.println("  Available Cleaners: " + availableCount);
            if (!cleanersInArea.isEmpty()) {
                System.out.println("  Cleaners: ");
                cleanersInArea.forEach(c -> System.out.println("    - " + c.getName() +
                        " (Experience: " + c.getExperience() + " years, Status: " +
                        (c.isAvailable() ? "Available" : "Unavailable") + ")"));
            }
        }
    }
}
