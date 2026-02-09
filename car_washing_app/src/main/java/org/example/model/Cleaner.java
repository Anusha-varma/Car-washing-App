package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a cleaner/service provider
 */
@Entity
@Table(name = "cleaners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cleaner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int experience; // in years

    @Column(nullable = false)
    private double salary; // monthly salary

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Area area;

    @Column(nullable = false)
    private boolean available;

    public Cleaner(String name, int experience, double salary, Area area) {
        this.name = name;
        this.experience = experience;
        this.salary = salary;
        this.area = area;
        this.available = true;
    }


    @Override
    public String toString() {
        return String.format("Cleaner{id=%d, name='%s', experience=%d years, salary=$%.2f, area=%s, available=%s}",
                id, name, experience, salary, area.getDisplayName(), available);
    }
}
