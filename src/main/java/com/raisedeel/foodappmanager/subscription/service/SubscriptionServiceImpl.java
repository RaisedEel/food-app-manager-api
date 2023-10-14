package com.raisedeel.foodappmanager.subscription.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionMapper;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.subscription.repository.SubscriptionRepository;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link SubscriptionService} interface for subscription management operations.
 * This class provides concrete implementations of methods for managing subscription data, including subscription creation,
 * retrieval, update, and deletion. It collaborates with various repositories and mappers to fulfill its functionality.
 *
 * @see SubscriptionService
 */
@AllArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  SubscriptionRepository subscriptionRepository;
  RestaurantRepository restaurantRepository;
  UserRepository userRepository;
  SubscriptionMapper subscriptionMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public SubscriptionDto retrieveSubscription(Long userId, Long restaurantId) {
    return subscriptionMapper.subscriptionToDto(
        getSubscriptionByUserIdAndRestaurantId(userId, restaurantId)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SubscriptionDto> retrieveSubscriptionsFromUser(Long userId) {
    return subscriptionRepository.findAllByUserId(userId)
        .stream()
        .map(subscriptionMapper::subscriptionToDto)
        .toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SubscriptionDto> retrieveSubscriptionsFromRestaurants(Long restaurantId) {
    return subscriptionRepository.findAllByRestaurantId(restaurantId)
        .stream()
        .map(subscriptionMapper::subscriptionToDto)
        .toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SubscriptionDto subscribeToRestaurant(Long userId, Long restaurantId) {
    User user = getUserById(userId);
    Restaurant restaurant = getRestaurantById(restaurantId);

    Subscription subscription = new Subscription();
    subscription.setUser(user);
    subscription.setRestaurant(restaurant);

    return subscriptionMapper.subscriptionToDto(subscriptionRepository.save(subscription));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unsubscribeToRestaurant(Long userId, Long restaurantId) {
    updateRating(new SubscriptionDto(), userId, restaurantId);
    subscriptionRepository.deleteByUserIdAndRestaurantId(userId, restaurantId);
  }

  /**
   * {@inheritDoc}
   * Subsequently, this operation updates the average rating in the associated {@link Restaurant} entity,
   * identified by the restaurantId parameter.
   */
  @Override
  public SubscriptionDto updateRating(SubscriptionDto subscriptionDto, Long userId, Long restaurantId) {
    Restaurant restaurant = getRestaurantById(restaurantId);
    Subscription subscription = getSubscriptionByUserIdAndRestaurantId(userId, restaurantId);

    SubscriptionDto updatedDto = subscriptionMapper.subscriptionToDto(subscriptionRepository.save(
        subscriptionMapper.updateSubscriptionFromDto(subscriptionDto, subscription)
    ));

    restaurant.setRating(
        subscriptionRepository.averageOfRatingsByRestaurantId(restaurantId).orElse(0.0)
    );
    restaurantRepository.save(restaurant);
    return updatedDto;
  }

  private Subscription getSubscriptionByUserIdAndRestaurantId(Long userId, Long restaurantId) {
    return subscriptionRepository.findByUserIdAndRestaurantId(userId, restaurantId)
        .orElseThrow(() -> new EntityNotFoundException("Subscription"));
  }

  private Restaurant getRestaurantById(Long restaurantId) {
    return restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new EntityNotFoundException("Restaurant"));
  }

  private User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User"));
  }

  /**
   * An alternative way of adding a rating to an average. This method is preferred if performance is
   * favored to precision.<br/>
   * <em>-Note:</em> The method could be adapted to exchange a rating in the average but could
   * lead to inaccurate averages when subtracting ratings.
   *
   * @param rating          the rating to add to the average.
   * @param numberOfRatings the last registered total of ratings.
   * @param totalRating     the last registered average of ratings.
   * @return the new average of ratings.
   */
  @Deprecated
  private double addRating(int rating, int numberOfRatings, double totalRating) {
    if (rating <= 0) return totalRating;
    return totalRating + (rating - totalRating) / (numberOfRatings + 1);
  }

}
