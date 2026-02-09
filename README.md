
# Car Washing Service Management System

A simple Java-based application to manage a car washing service business with **customers, cleaners, service packages, and subscriptions**.

---

## Screenshots

### Swagger UI
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/a1b63bf0-c91a-44e7-9831-d79f540df74c" />
```
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/9b1ad7e6-d2c7-4b02-b98f-5d0dd565a990" />

```

### H2 Console
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d3f336c2-95fc-4cbd-9881-8d87232dd02a" />
```
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/4c7e4168-b709-4d60-93bc-0345a997e63b" />

```
---
## Features

* **Customer & Cleaner Management**

  * Register customers with area & contact details
  * Register cleaners with experience, salary & availability

* **Service Packages**

  * Full Clean – $150
  * Outer Clean – $75
  * Outer Clean + Watering – $100

* **Subscription Plans**

  * Weekly (7 days)
  * Monthly (30 days)
  * Yearly (365 days)

* **Area-Based Booking**

  * Services only allowed if **customer and cleaner are in the same area**
  * Areas: Downtown, Uptown, Suburbs, Industrial, Residential, Business District

* **Service Booking**

  * Book services with start & end dates
  * Automatic price calculation
  * Cleaner availability validation

* **Reports & Analytics**

  * Total revenue
  * Revenue by package & frequency
  * Cleaner performance
  * Area-wise service coverage

---

## Project Structure

```
src/main/java/org/example/
├── Main.java
├── model/
│   ├── Area.java
│   ├── Cleaner.java
│   ├── Customer.java
│   ├── Frequency.java
│   ├── PackageType.java
│   └── ServiceSubscription.java
├── repository/
│   ├── CleanerRepository.java
│   ├── CustomerRepository.java
│   └── ServiceSubscriptionRepository.java
└── service/
    ├── AdminService.java
    ├── BookingService.java
    ├── ServiceAvailabilityService.java
    └── ReportService.java
```

---

## Running the Application

### Prerequisites

* Java 21+
* Maven

### Compile

```bash
cd car_washing_app
javac -d target/classes src/main/java/org/example/**/*.java
```

### Run

```bash
java -cp target/classes org.example.Main
```

---

## Main Menu

* Register Cleaner
* Register Customer
* Book Service
* View Subscriptions
* Manage Cleaners
* View Reports
* Check Service Availability
* Demo Bookings
* Exit

---

## Sample Data

**Cleaners**

* Downtown, Uptown, Suburbs

**Customers**

* Downtown, Uptown, Suburbs

---

