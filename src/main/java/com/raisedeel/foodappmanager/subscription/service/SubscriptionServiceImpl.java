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

@AllArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  SubscriptionRepository subscriptionRepository;
  RestaurantRepository restaurantRepository;
  UserRepository userRepository;
  SubscriptionMapper subscriptionMapper;

  @Override
  public SubscriptionDto retrieveSubscription(Long userId, Long restaurantId) {
    return subscriptionMapper.subscriptionToDto(
        getSubscriptionByUserIdAndRestaurantId(userId, restaurantId)
    );
  }

  @Override
  public SubscriptionDto subscribeToRestaurant(Long userId, Long restaurantId) {
    User user = getUserById(userId);
    Restaurant restaurant = getRestaurantById(restaurantId);

    Subscription subscription = new Subscription();
    subscription.setUser(user);
    subscription.setRestaurant(restaurant);

    return subscriptionMapper.subscriptionToDto(subscriptionRepository.save(subscription));
  }

  @Override
  public void unsubscribeToRestaurant(Long userId, Long restaurantId) {
    updateRating(new SubscriptionDto(), userId, restaurantId);
    subscriptionRepository.deleteByUserIdAndRestaurantId(userId, restaurantId);
  }

  @Override
  public SubscriptionDto updateRating(SubscriptionDto subscriptionDto, Long userId, Long restaurantId) {
    Restaurant restaurant = getRestaurantById(restaurantId);
    Subscription subscription = getSubscriptionByUserIdAndRestaurantId(userId, restaurantId);

    restaurant.setRating(calculateRating(
        restaurant.getTotalOfRatings(),
        restaurant.getRating(),
        subscription.getRating(),
        subscriptionDto.getRating()
    ));

    restaurantRepository.save(restaurant);
    return subscriptionMapper.subscriptionToDto(subscriptionRepository.save(
        subscriptionMapper.updateSubscriptionFromDto(subscriptionDto, subscription)
    ));
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

  @SuppressWarnings("ReassignedVariable")
  private double calculateRating(int numberOfRatings, double totalRating, int oldUserRating, int newUserRating) {

    double newTotalRating = totalRating;

    if (oldUserRating > 0) {
      numberOfRatings--;
      if (numberOfRatings == 0) {
        newTotalRating = 0.0;
      } else {
        newTotalRating = (totalRating * (numberOfRatings + 1) - oldUserRating) / numberOfRatings;
      }
    }

    if (newUserRating <= 0) return newTotalRating;
    numberOfRatings++;
    newTotalRating = newTotalRating + ((newUserRating - newTotalRating) / numberOfRatings);

    return newTotalRating;
  }

}
