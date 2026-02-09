# Car Washing Service Management System

A comprehensive Java application for managing a car washing service business with cleaners, customers, and service subscriptions.

## Features

### 1. **Customer & Cleaner Management**
- Register new customers with location (area) and contact info
- Register cleaners with experience level and salary information
- Track cleaner availability status

### 2. **Service Packages**
- **Full Clean**: Complete interior and exterior cleaning with waxing ($150)
- **Outer Clean**: Exterior washing only ($75)
- **Outer Clean + Watering**: Exterior cleaning with interior watering ($100)

### 3. **Subscription Frequencies**
- **Weekly**: Service every 7 days
- **Monthly**: Service every 30 days
- **Yearly**: Service every 365 days

### 4. **Area-Based Service Matching**
- Services are only available when cleaner and customer are in the same area
- 6 geographical areas: Downtown, Uptown, Suburbs, Industrial, Residential, Business District
- Automatic validation before booking

### 5. **Service Booking**
- Book services between cleaners and customers in the same area
- Specify start and end dates for subscriptions
- Automatic price calculation based on frequency and package

### 6. **Analytics & Reporting**
- Revenue reports (total and active)
- Revenue breakdown by package type
- Revenue breakdown by subscription frequency
- Cleaner performance reports
- Area coverage analysis
- Subscription details tracking

## Project Structure

```
src/main/java/org/example/
├── Main.java                          # Main application with interactive menu
├── model/
│   ├── Area.java                     # Enum for geographical areas
│   ├── Cleaner.java                  # Cleaner entity
│   ├── Customer.java                 # Customer entity
│   ├── Frequency.java                # Enum for subscription frequencies
│   ├── PackageType.java              # Enum for service packages
│   └── ServiceSubscription.java       # Service subscription entity
├── repository/
│   ├── CleanerRepository.java         # Cleaner data access
│   ├── CustomerRepository.java        # Customer data access
│   └── ServiceSubscriptionRepository.java  # Subscription data access
└── service/
    ├── AdminService.java             # Admin operations (register users)
    ├── BookingService.java           # Service booking operations
    ├── ServiceAvailabilityService.java # Area matching and availability
    └── ReportService.java            # Analytics and reporting
```

## Running the Application

### Prerequisites
- Java 21 or higher
- Maven

### Compilation
```bash
cd car_washing_app
javac -d target/classes src/main/java/org/example/**/*.java
```

### Execution
```bash
java -cp target/classes org.example.Main
```

## Main Menu Options

1. **Register Cleaner** - Add a new cleaner to the system
2. **Register Customer** - Add a new customer to the system
3. **Book Service** - Create a service subscription
4. **View Services & Subscriptions** - See all active subscriptions
5. **Manage Cleaners** - Update cleaner info and availability
6. **View Reports** - Analytics and performance metrics
7. **Check Service Availability** - Find available cleaners for a customer
8. **Demo Bookings** - Create sample bookings automatically
0. **Exit** - Close the application

## Sample Data

The application comes with pre-loaded sample data:

### Cleaners (by Area)
- **Downtown**: John Smith (5 yrs), Mary Johnson (8 yrs)
- **Uptown**: Bob Wilson (3 yrs), Alice Brown (6 yrs)
- **Suburbs**: Charlie Davis (4 yrs), Eve Martinez (7 yrs)

### Customers (by Area)
- **Downtown**: Client A, Client B
- **Uptown**: Client C
- **Suburbs**: Client D, Client E

## Key Classes Overview

### Area.java
Represents geographical service areas. Cleaners and customers must be in the same area.

### PackageType.java
Defines available cleaning packages with pricing and descriptions.

### Frequency.java
Defines subscription frequencies (weekly, monthly, yearly).

### Cleaner.java
Represents a service provider with:
- Name, ID, experience (years), monthly salary
- Assigned area
- Availability status

### Customer.java
Represents a customer with:
- Name, ID, email, phone number
- Service area
- Contact information

### ServiceSubscription.java
Represents an active service booking with:
- Customer and Cleaner IDs
- Package type and frequency
- Start and end dates
- Calculated total price based on frequency
- Active/inactive status

### ServiceAvailabilityService.java
Validates service availability based on:
- Area matching between cleaner and customer
- Cleaner availability status
- Returns list of available cleaners for a customer

### BookingService.java
Handles service booking operations:
- Validates area matching before booking
- Validates date ranges
- Calculates subscription pricing
- Manages subscription lifecycle

### AdminService.java
Administrative operations:
- Register/update/delete cleaners
- Register/update/delete customers
- Manage cleaner availability and salary
- Retrieve data by various filters

### ReportService.java
Analytics and reporting:
- Revenue analysis
- Performance metrics by cleaner
- Coverage analysis by area
- Subscription details

## Business Logic

### Service Booking Validation
1. Customer and Cleaner must exist
2. **CRITICAL**: Customer and Cleaner must be in the SAME area
3. Cleaner must be available (status = true)
4. Start date must be before end date

### Price Calculation
```
Total Price = Base Package Price × Number of Service Occurrences
Number of Occurrences = Days between dates / Frequency interval
```

Example:
- Package: Full Clean ($150)
- Frequency: Weekly (7 days)
- Duration: 3 months (90 days)
- Occurrences: 90 / 7 = 12 times
- Total Price: $150 × 12 = $1,800

## Example Workflow

1. **Register Cleaner**
   - Name: "John Smith"
   - Experience: 5 years
   - Salary: $2,500/month
   - Area: Downtown

2. **Register Customer**
   - Name: "Jane Doe"
   - Email: jane@example.com
   - Phone: 555-1234
   - Area: Downtown

3. **Book Service**
   - Select Customer: Jane Doe
   - Select Cleaner: John Smith (same area - Downtown)
   - Package: Full Clean ($150)
   - Frequency: Weekly
   - Start: 2026-02-09
   - End: 2026-05-09 (3 months)
   - Total: $150 × 12 weeks = $1,800

4. **View Reports**
   - See total revenue
   - Check cleaner assignments
   - Analyze area coverage

## Data Persistence

**Note**: This application uses in-memory repositories. Data is not persisted to disk and will be lost when the application terminates. For production use, integrate with a database (SQL/NoSQL).

## Future Enhancements

1. Database integration (JPA/Hibernate)
2. REST API endpoints (Spring Boot)
3. Web UI dashboard
4. Invoice generation
5. Payment processing
6. Email notifications
7. SMS reminders
8. Rating and review system
9. Cleaner skill/specialization tags
10. Dynamic pricing based on demand

## License

This project is provided as-is for educational purposes.
