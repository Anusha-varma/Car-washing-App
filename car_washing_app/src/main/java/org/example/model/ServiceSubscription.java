package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Represents a service subscription for a customer
 */
@Entity
@Table(name = "service_subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int customerId;

    @Column(nullable = false)
    private int cleanerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType packageType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private boolean active;

    public ServiceSubscription(int customerId, int cleanerId, PackageType packageType,
                              Frequency frequency, LocalDate startDate, LocalDate endDate) {
        this.customerId = customerId;
        this.cleanerId = cleanerId;
        this.packageType = packageType;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
        this.totalPrice = calculatePrice();
    }


    /**
     * Calculate the total price based on frequency and package
     */
    public double calculatePrice() {
        double basePrice = packageType.getBasePrice();
        int daysDifference = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        int occurrences = Math.max(1, daysDifference / frequency.getDayInterval());
        return basePrice * occurrences;
    }

    @Override
    public String toString() {
        return String.format("ServiceSubscription{id=%d, customerId=%d, cleanerId=%d, package=%s, frequency=%s, " +
                "startDate=%s, endDate=%s, totalPrice=$%.2f, active=%s}",
                id, customerId, cleanerId, packageType.getName(), frequency.getDisplayName(),
                startDate, endDate, totalPrice, active);
    }
}
