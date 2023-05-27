package com.raisedeel.foodappmanager.subscription.repository;

import com.raisedeel.foodappmanager.subscription.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}
