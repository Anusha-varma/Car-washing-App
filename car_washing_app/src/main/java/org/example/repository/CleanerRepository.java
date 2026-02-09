package org.example.repository;

import org.example.model.Area;
import org.example.model.Cleaner;
import java.util.*;

/**
 * Repository for managing Cleaner entities
 */
public class CleanerRepository {
    private Map<Integer, Cleaner> cleaners = new HashMap<>();
    private int nextId = 1;

    public Cleaner save(Cleaner cleaner) {
        if (cleaner.getId() == 0) {
            cleaner.setId(nextId++);
        }
        cleaners.put(cleaner.getId(), cleaner);
        return cleaner;
    }

    public Cleaner findById(int id) {
        return cleaners.get(id);
    }

    public List<Cleaner> findAll() {
        return new ArrayList<>(cleaners.values());
    }

    public List<Cleaner> findByArea(Area area) {
        return cleaners.values().stream()
                .filter(c -> c.getArea() == area)
                .toList();
    }

    public List<Cleaner> findByAreaAndAvailable(Area area) {
        return cleaners.values().stream()
                .filter(c -> c.getArea() == area && c.isAvailable())
                .toList();
    }

    public List<Cleaner> findByExperience(int minYears) {
        return cleaners.values().stream()
                .filter(c -> c.getExperience() >= minYears)
                .toList();
    }

    public boolean update(Cleaner cleaner) {
        if (cleaners.containsKey(cleaner.getId())) {
            cleaners.put(cleaner.getId(), cleaner);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        return cleaners.remove(id) != null;
    }

    public void clear() {
        cleaners.clear();
        nextId = 1;
    }
}
