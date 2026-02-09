package org.example.repository;

import org.example.model.ServiceSubscription;
import java.util.*;

/**
 * Repository for managing ServiceSubscription entities
 */
public class ServiceSubscriptionRepository {
    private Map<Integer, ServiceSubscription> subscriptions = new HashMap<>();
    private int nextId = 1;

    public ServiceSubscription save(ServiceSubscription subscription) {
        if (subscription.getId() == 0) {
            subscription.setId(nextId++);
        }
        subscriptions.put(subscription.getId(), subscription);
        return subscription;
    }

    public ServiceSubscription findById(int id) {
        return subscriptions.get(id);
    }

    public List<ServiceSubscription> findAll() {
        return new ArrayList<>(subscriptions.values());
    }

    public List<ServiceSubscription> findByCustomerId(int customerId) {
        return subscriptions.values().stream()
                .filter(s -> s.getCustomerId() == customerId)
                .toList();
    }

    public List<ServiceSubscription> findByCleanerId(int cleanerId) {
        return subscriptions.values().stream()
                .filter(s -> s.getCleanerId() == cleanerId)
                .toList();
    }

    public List<ServiceSubscription> findActiveSubscriptions() {
        return subscriptions.values().stream()
                .filter(ServiceSubscription::isActive)
                .toList();
    }

    public List<ServiceSubscription> findByCustomerIdAndActive(int customerId) {
        return subscriptions.values().stream()
                .filter(s -> s.getCustomerId() == customerId && s.isActive())
                .toList();
    }

    public boolean update(ServiceSubscription subscription) {
        if (subscriptions.containsKey(subscription.getId())) {
            subscriptions.put(subscription.getId(), subscription);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        return subscriptions.remove(id) != null;
    }

    public void clear() {
        subscriptions.clear();
        nextId = 1;
    }
}
