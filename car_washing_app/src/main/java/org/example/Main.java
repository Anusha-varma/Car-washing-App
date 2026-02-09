package org.example;

import org.example.model.*;
import org.example.repository.*;
import org.example.service.*;
import org.example.exception.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for Car Washing Service Management System
 * Features:
 * - Register cleaners and customers
 * - Book services with different packages (Full Clean, Outer Clean, Outer Clean + Watering)
 * - Support multiple frequencies (Weekly, Monthly, Yearly)
 * - Area-based service matching (cleaners and customers must be in same area)
 * - Cleaner management (experience, salary, availability)
 * - Comprehensive reporting and analytics
 */
public class Main {
    private static final CleanerRepository cleanerRepository = new CleanerRepository();
    private static final CustomerRepository customerRepository = new CustomerRepository();
    private static final ServiceSubscriptionRepository subscriptionRepository = new ServiceSubscriptionRepository();

    private static final AdminService adminService = new AdminService(cleanerRepository, customerRepository);
    private static final ServiceAvailabilityService availabilityService = new ServiceAvailabilityService(cleanerRepository, customerRepository);
    private static final BookingService bookingService = new BookingService(subscriptionRepository, cleanerRepository, customerRepository, availabilityService);
    private static final ReportService reportService = new ReportService(subscriptionRepository, cleanerRepository);

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   CAR WASHING SERVICE MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));

        // Initialize sample data
        initializeSampleData();

        // Run interactive menu
        runInteractiveMenu();
    }

    /**
     * Initialize sample data for demonstration
     */
    private static void initializeSampleData() {
        System.out.println("\n[Initializing sample data...]");

        // Register cleaners
        adminService.registerCleaner("John Smith", 5, 2500, Area.DOWNTOWN);
        adminService.registerCleaner("Mary Johnson", 8, 3000, Area.DOWNTOWN);
        adminService.registerCleaner("Bob Wilson", 3, 2000, Area.UPTOWN);
        adminService.registerCleaner("Alice Brown", 6, 2800, Area.UPTOWN);
        adminService.registerCleaner("Charlie Davis", 4, 2200, Area.SUBURBS);
        adminService.registerCleaner("Eve Martinez", 7, 2900, Area.SUBURBS);

        // Register customers
        adminService.registerCustomer("Client A", "clienta@email.com", "555-0001", Area.DOWNTOWN);
        adminService.registerCustomer("Client B", "clientb@email.com", "555-0002", Area.DOWNTOWN);
        adminService.registerCustomer("Client C", "clientc@email.com", "555-0003", Area.UPTOWN);
        adminService.registerCustomer("Client D", "clientd@email.com", "555-0004", Area.SUBURBS);
        adminService.registerCustomer("Client E", "cliente@email.com", "555-0005", Area.SUBURBS);

        System.out.println("[Sample data initialized successfully!]\n");
    }

    /**
     * Run the interactive menu
     */
    private static void runInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMainMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    menuRegisterCleaner(scanner);
                    break;
                case "2":
                    menuRegisterCustomer(scanner);
                    break;
                case "3":
                    menuBookService(scanner);
                    break;
                case "4":
                    menuViewServices();
                    break;
                case "5":
                    menuManageCleaners(scanner);
                    break;
                case "6":
                    menuReports();
                    break;
                case "7":
                    menuCheckAvailability(scanner);
                    break;
                case "8":
                    menuDemoBookings();
                    break;
                case "0":
                    running = false;
                    System.out.println("\nThank you for using Car Washing Service Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * Print main menu
     */
    private static void printMainMenu() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("-".repeat(60));
        System.out.println("1. Register Cleaner");
        System.out.println("2. Register Customer");
        System.out.println("3. Book Service");
        System.out.println("4. View Services & Subscriptions");
        System.out.println("5. Manage Cleaners");
        System.out.println("6. View Reports");
        System.out.println("7. Check Service Availability");
        System.out.println("8. Demo Bookings");
        System.out.println("0. Exit");
        System.out.println("-".repeat(60));
    }

    /**
     * Menu to register a new cleaner
     */
    private static void menuRegisterCleaner(Scanner scanner) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("REGISTER NEW CLEANER");
        System.out.println("-".repeat(60));

        System.out.print("Cleaner Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Experience (years): ");
        int experience = getIntInput(scanner);

        System.out.print("Salary (monthly): ");
        double salary = getDoubleInput(scanner);

        System.out.println("Select Area:");
        Area area = selectArea(scanner);

        try {
            adminService.registerCleaner(name, experience, salary, area);
        } catch (CleanerRegistrationException | CleanerNotFoundException | InvalidInputException e) {
            ExceptionHandler.handleException(e);
            ExceptionHandler.logException(e);
        }
    }

    /**
     * Menu to register a new customer
     */
    private static void menuRegisterCustomer(Scanner scanner) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("REGISTER NEW CUSTOMER");
        System.out.println("-".repeat(60));

        System.out.print("Customer Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        System.out.println("Select Area:");
        Area area = selectArea(scanner);

        try {
            adminService.registerCustomer(name, email, phone, area);
        } catch (CustomerRegistrationException | DuplicateEmailException | InvalidInputException e) {
            ExceptionHandler.handleException(e);
            ExceptionHandler.logException(e);
        }
    }

    /**
     * Menu to book a service
     */
    private static void menuBookService(Scanner scanner) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("BOOK SERVICE");
        System.out.println("-".repeat(60));

        adminService.printAllCustomers();
        System.out.print("\nEnter Customer ID: ");
        int customerId = getIntInput(scanner);

        Customer customer = adminService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("\nAvailable Cleaners for " + customer.getName() + " (" + customer.getArea().getDisplayName() + "):");
        List<Cleaner> availableCleaners = availabilityService.getAvailableCleanersForCustomer(customer);

        if (availableCleaners.isEmpty()) {
            System.out.println("No cleaners available in your area!");
            return;
        }

        for (Cleaner cleaner : availableCleaners) {
            System.out.println(cleaner);
        }

        System.out.print("\nEnter Cleaner ID: ");
        int cleanerId = getIntInput(scanner);

        System.out.println("\nSelect Package Type:");
        for (int i = 0; i < PackageType.values().length; i++) {
            PackageType pkg = PackageType.values()[i];
            System.out.println((i + 1) + ". " + pkg.getName() + " - $" + String.format("%.2f", pkg.getBasePrice()) +
                             " (" + pkg.getDescription() + ")");
        }
        System.out.print("Enter choice (1-" + PackageType.values().length + "): ");
        int pkgChoice = getIntInput(scanner) - 1;
        if (pkgChoice < 0 || pkgChoice >= PackageType.values().length) {
            System.out.println("Invalid choice!");
            return;
        }
        PackageType packageType = PackageType.values()[pkgChoice];

        System.out.println("\nSelect Frequency:");
        for (int i = 0; i < Frequency.values().length; i++) {
            Frequency freq = Frequency.values()[i];
            System.out.println((i + 1) + ". " + freq.getDisplayName());
        }
        System.out.print("Enter choice (1-" + Frequency.values().length + "): ");
        int freqChoice = getIntInput(scanner) - 1;
        if (freqChoice < 0 || freqChoice >= Frequency.values().length) {
            System.out.println("Invalid choice!");
            return;
        }
        Frequency frequency = Frequency.values()[freqChoice];

        System.out.print("Start Date (YYYY-MM-DD): ");
        LocalDate startDate = getDateInput(scanner);

        System.out.print("End Date (YYYY-MM-DD): ");
        LocalDate endDate = getDateInput(scanner);

        try {
            bookingService.bookService(customerId, cleanerId, packageType, frequency, startDate, endDate);
        } catch (CustomerNotFoundException | CleanerNotFoundException | AreaMismatchException |
                 CleanerUnavailableException | InvalidDateRangeException | InvalidBookingException e) {
            ExceptionHandler.handleException(e);
            ExceptionHandler.logException(e);
        }
    }

    /**
     * Menu to view services and subscriptions
     */
    private static void menuViewServices() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("ALL SUBSCRIPTIONS");
        System.out.println("-".repeat(60));
        reportService.printSubscriptionDetailsReport();
    }

    /**
     * Menu to manage cleaners
     */
    private static void menuManageCleaners(Scanner scanner) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("MANAGE CLEANERS");
        System.out.println("-".repeat(60));
        System.out.println("1. View All Cleaners");
        System.out.println("2. View Cleaners by Area");
        System.out.println("3. Update Cleaner Availability");
        System.out.println("4. Update Cleaner Salary");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                adminService.printAllCleaners();
                break;
            case "2":
                System.out.println("Select Area:");
                Area area = selectArea(scanner);
                List<Cleaner> cleaners = adminService.getCleanersByArea(area);
                System.out.println("\nCleaners in " + area.getDisplayName() + ":");
                cleaners.forEach(System.out::println);
                break;
            case "3":
                adminService.printAllCleaners();
                System.out.print("\nEnter Cleaner ID: ");
                int cleanerId = getIntInput(scanner);
                System.out.print("Available? (true/false): ");
                boolean available = Boolean.parseBoolean(scanner.nextLine().trim());
                if (adminService.updateCleanerAvailability(cleanerId, available)) {
                    System.out.println("✓ Cleaner availability updated!");
                } else {
                    System.out.println("Cleaner not found!");
                }
                break;
            case "4":
                adminService.printAllCleaners();
                System.out.print("\nEnter Cleaner ID: ");
                cleanerId = getIntInput(scanner);
                System.out.print("New Salary: ");
                double newSalary = getDoubleInput(scanner);
                if (adminService.updateCleanerSalary(cleanerId, newSalary)) {
                    System.out.println("✓ Cleaner salary updated!");
                } else {
                    System.out.println("Cleaner not found!");
                }
                break;
        }
    }

    /**
     * Menu to view reports
     */
    private static void menuReports() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("REPORTS & ANALYTICS");
        System.out.println("-".repeat(60));
        reportService.printRevenueReport();
        reportService.printRevenueByPackage();
        reportService.printRevenueByFrequency();
        reportService.printAreaCoverageReport();
        reportService.printCleanerPerformanceReport();
    }

    /**
     * Menu to check service availability
     */
    private static void menuCheckAvailability(Scanner scanner) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("CHECK SERVICE AVAILABILITY");
        System.out.println("-".repeat(60));

        adminService.printAllCustomers();
        System.out.print("\nEnter Customer ID: ");
        int customerId = getIntInput(scanner);

        Customer customer = adminService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        List<Cleaner> availableCleaners = availabilityService.getAvailableCleanersForCustomer(customer);
        System.out.println("\n✓ Service is available in area: " + customer.getArea().getDisplayName());
        System.out.println("Available cleaners for " + customer.getName() + ":");

        if (availableCleaners.isEmpty()) {
            System.out.println("No cleaners currently available in this area.");
        } else {
            availableCleaners.forEach(System.out::println);
        }
    }

    /**
     * Menu to create demo bookings
     */
    private static void menuDemoBookings() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("CREATING DEMO BOOKINGS...");
        System.out.println("-".repeat(60));

        try {
            // Book 1: Customer 1 with Cleaner 1 (both in Downtown)
            bookingService.bookService(1, 1, PackageType.FULL_CLEAN, Frequency.WEEKLY,
                                      LocalDate.of(2026, 2, 9), LocalDate.of(2026, 5, 9));

            // Book 2: Customer 1 with Cleaner 2 (both in Downtown)
            bookingService.bookService(1, 2, PackageType.OUTER_CLEAN_WATERING, Frequency.MONTHLY,
                                      LocalDate.of(2026, 2, 9), LocalDate.of(2026, 12, 9));

            // Book 3: Customer 3 with Cleaner 3 (both in Uptown)
            bookingService.bookService(3, 3, PackageType.OUTER_CLEAN, Frequency.WEEKLY,
                                      LocalDate.of(2026, 2, 9), LocalDate.of(2026, 4, 9));

            // Book 4: Customer 4 with Cleaner 5 (both in Suburbs)
            bookingService.bookService(4, 5, PackageType.FULL_CLEAN, Frequency.YEARLY,
                                      LocalDate.of(2026, 2, 9), LocalDate.of(2027, 2, 9));

            // Book 5: Customer 5 with Cleaner 6 (both in Suburbs)
            bookingService.bookService(5, 6, PackageType.OUTER_CLEAN_WATERING, Frequency.MONTHLY,
                                      LocalDate.of(2026, 2, 9), LocalDate.of(2026, 8, 9));

            System.out.println("\n✓ All demo bookings created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating bookings: " + e.getMessage());
        }
    }

    /**
     * Helper method to select an area
     */
    private static Area selectArea(Scanner scanner) {
        Area[] areas = Area.values();
        for (int i = 0; i < areas.length; i++) {
            System.out.println((i + 1) + ". " + areas[i].getDisplayName());
        }
        System.out.print("Enter choice (1-" + areas.length + "): ");
        int choice = getIntInput(scanner) - 1;
        if (choice >= 0 && choice < areas.length) {
            return areas[choice];
        }
        System.out.println("Invalid choice, using default area.");
        return Area.DOWNTOWN;
    }

    /**
     * Helper method to get integer input
     */
    private static int getIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return 0;
        }
    }

    /**
     * Helper method to get double input
     */
    private static double getDoubleInput(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return 0.0;
        }
    }

    /**
     * Helper method to get date input
     */
    private static LocalDate getDateInput(Scanner scanner) {
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return LocalDate.now();
        }
    }
}