package com.raisedeel.foodappmanager.subscription.service;

import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

  SubscriptionDto retrieveSubscription(Long userId, Long restaurantId);

  List<SubscriptionDto> retrieveSubscriptionsFromUser(Long userId);

  List<SubscriptionDto> retrieveSubscriptionsFromRestaurants(Long restaurantId);

  SubscriptionDto subscribeToRestaurant(Long userId, Long restaurantId);

  void unsubscribeToRestaurant(Long userId, Long restaurantId);

  SubscriptionDto updateRating(SubscriptionDto subscriptionDto, Long userId, Long restaurantId);

}
