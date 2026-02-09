package org.example.repository;

import org.example.model.ServiceSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA Repository for ServiceSubscription entity
 */
@Repository
public interface ServiceSubscriptionJpaRepository extends JpaRepository<ServiceSubscription, Integer> {
    List<ServiceSubscription> findByCustomerId(int customerId);
    List<ServiceSubscription> findByCleanerId(int cleanerId);
    List<ServiceSubscription> findByActiveTrue();
    List<ServiceSubscription> findByCustomerIdAndActiveTrue(int customerId);
}
