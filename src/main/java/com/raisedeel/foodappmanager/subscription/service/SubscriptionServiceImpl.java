package com.raisedeel.foodappmanager.subscription.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionMapper;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.subscription.repository.SubscriptionRepository;
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
    return null;
  }

  @Override
  public SubscriptionDto subscribeToRestaurant(Long userId, Long restaurantId) {
    return subscriptionMapper.subscriptionToDto(subscriptionRepository.save(new Subscription()));
  }

  @Override
  public void unsubscribeToRestaurant(Long userId, Long restaurantId) {

  }

  @Override
  public SubscriptionDto updateRating(SubscriptionDto subscriptionDto, Long userId, Long restaurantId) {
    Restaurant restaurant = getRestaurantById(restaurantId);
    restaurant.setRating(subscriptionDto.getRating());
    restaurantRepository.save(restaurant);

    Subscription subscription = subscriptionMapper.updateSubscriptionFromDto(subscriptionDto, new Subscription());

    return subscriptionMapper.subscriptionToDto(subscriptionRepository.save(subscription));
  }

  private Restaurant getRestaurantById(Long restaurantId) {
    return restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new EntityNotFoundException("Restaurant"));
  }

}
