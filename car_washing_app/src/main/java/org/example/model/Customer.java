package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a customer who requests car washing services
 */
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Area area;

    public Customer(String name, String email, String phone, Area area) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.area = area;
    }

    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s', email='%s', phone='%s', area=%s}",
                id, name, email, phone, area.getDisplayName());
    }
}
