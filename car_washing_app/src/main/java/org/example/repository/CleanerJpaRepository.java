package org.example.repository;

import org.example.model.Cleaner;
import org.example.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA Repository for Cleaner entity
 */
@Repository
public interface CleanerJpaRepository extends JpaRepository<Cleaner, Integer> {
    List<Cleaner> findByArea(Area area);
    List<Cleaner> findByAreaAndAvailableTrue(Area area);
    List<Cleaner> findByExperienceGreaterThanEqual(int experience);
    List<Cleaner> findByAvailableTrue();
}
