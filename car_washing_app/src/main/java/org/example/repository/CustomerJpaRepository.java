package org.example.repository;

import org.example.model.Customer;
import org.example.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Customer entity
 */
@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByArea(Area area);
    Optional<Customer> findByEmail(String email);
}
