package com.raisedeel.foodappmanager.subscription.web;

import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import com.raisedeel.foodappmanager.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "Subscription Controller", description = "Allow users the creation, retrieving, updating and deletion of subscriptions on any preferred restaurant.")
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

  SubscriptionService subscriptionService;

  @GetMapping("/user/{userId}/restaurant/{restaurantId}")
  public ResponseEntity<SubscriptionDto> retrieveSubscriptionHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscription(userId, restaurantId), HttpStatus.OK);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<SubscriptionDto>> retrieveSubscriptionsFromUserHandler(
      @PathVariable Long userId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscriptionsFromUser(userId), HttpStatus.OK);
  }

  @GetMapping("/restaurant/{restaurantId}")
  public ResponseEntity<List<SubscriptionDto>> retrieveSubscriptionsFromRestaurantHandler(
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscriptionsFromRestaurants(restaurantId), HttpStatus.OK);
  }

  @PostMapping("/user/{userId}/restaurant/{restaurantId}")
  public ResponseEntity<SubscriptionDto> subscribeToRestaurantHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(
        subscriptionService.subscribeToRestaurant(userId, restaurantId),
        HttpStatus.CREATED
    );
  }

  @DeleteMapping("user/{userId}/restaurant/{restaurantId}")
  public ResponseEntity<HttpStatus> unsubscribeToRestaurantHandler(
      @PathVariable Long userId,
      @Valid @PathVariable Long restaurantId
  ) {
    subscriptionService.unsubscribeToRestaurant(userId, restaurantId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("user/{userId}/restaurant/{restaurantId}")
  public ResponseEntity<SubscriptionDto> updateRatingHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId,
      @Valid @RequestBody SubscriptionDto subscriptionDto
  ) {
    return new ResponseEntity<>(
        subscriptionService.updateRating(subscriptionDto, userId, restaurantId),
        HttpStatus.OK
    );
  }

}
