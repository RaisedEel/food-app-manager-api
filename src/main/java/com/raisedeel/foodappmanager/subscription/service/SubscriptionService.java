package com.raisedeel.foodappmanager.subscription.service;

import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;

import java.util.List;

/**
 * A service interface for subscription management operations.<br/>
 * This interface provides methods for managing subscription data, including subscription creation, retrieval, update,
 * and deletion.
 */
public interface SubscriptionService {

  /**
   * Retrieves a subscription with the given user and restaurant IDs.
   *
   * @param userId       the ID of the user.
   * @param restaurantId the ID of the restaurant.
   * @return the {@link SubscriptionDto} representing the retrieved subscription.
   */
  SubscriptionDto retrieveSubscription(Long userId, Long restaurantId);

  /**
   * Retrieves the subscriptions associated with a specific user ID.
   *
   * @param userId the ID of the user's subscription.
   * @return a list of {@link SubscriptionDto} representing all subscriptions.
   */
  List<SubscriptionDto> retrieveSubscriptionsFromUser(Long userId);

  /**
   * Retrieves the subscriptions associated with a specific restaurant ID.
   *
   * @param restaurantId the ID of the restaurant's subscription.
   * @return a list of {@link SubscriptionDto} representing all subscriptions.
   */
  List<SubscriptionDto> retrieveSubscriptionsFromRestaurants(Long restaurantId);

  /**
   * Saves a subscription with a rating of 0 associated with the user and restaurant IDs into the database.
   *
   * @param userId       the ID of the user subscribing.
   * @param restaurantId the ID of the restaurant to subscribe.
   * @return the saved {@link SubscriptionDto} with updated information.
   */
  SubscriptionDto subscribeToRestaurant(Long userId, Long restaurantId);

  /**
   * Deletes the subscription with the given user and restaurant IDs from the database.
   *
   * @param userId       the ID of the user.
   * @param restaurantId the ID of the restaurant.
   */
  void unsubscribeToRestaurant(Long userId, Long restaurantId);

  /**
   * Updates the subscription with the user and restaurant IDs using the data in the {@link SubscriptionDto}.
   *
   * @param userId          the ID of the user.
   * @param restaurantId    the ID of the restaurant.
   * @param subscriptionDto the {@link SubscriptionDto} containing updated rating data.
   * @return the updated {@link SubscriptionDto}.
   */
  SubscriptionDto updateRating(SubscriptionDto subscriptionDto, Long userId, Long restaurantId);

}
