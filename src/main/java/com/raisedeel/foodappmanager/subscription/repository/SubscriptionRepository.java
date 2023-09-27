package com.raisedeel.foodappmanager.subscription.repository;

import com.raisedeel.foodappmanager.subscription.model.Subscription;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
  Optional<Subscription> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

  List<Subscription> findAllByUserId(Long userId);

  List<Subscription> findAllByRestaurantId(Long restaurantId);

  @Transactional
  @Modifying
  @Query("delete from Subscription s where s.user.id = :userId and s.restaurant.id = :restaurantId")
  void deleteByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
