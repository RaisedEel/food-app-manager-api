package com.raisedeel.foodappmanager.subscription.web;

import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import com.raisedeel.foodappmanager.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "2.- Subscription Controller", description = "Allow users the creation, retrieving, updating and deletion of subscriptions on any preferred restaurant.")
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

  SubscriptionService subscriptionService;

  @Operation(summary = "Create a subscription on a restaurant", description = "Creates a subscription of the user with a rating of 0 on the restaurant. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful creation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful creation. Some fields/parameters were invalid or the subscription is a duplicate",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user or restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/user/{userId}/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<SubscriptionDto> subscribeToRestaurantHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(
        subscriptionService.subscribeToRestaurant(userId, restaurantId),
        HttpStatus.CREATED
    );
  }

  @Operation(summary = "Get a subscription", description = "Get the subscription from the user and restaurant given. No token is required.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "404", description = "The subscription could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "/user/{userId}/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<SubscriptionDto> retrieveSubscriptionHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscription(userId, restaurantId), HttpStatus.OK);
  }

  @Operation(summary = "Get all subscriptions from a user", description = "Get the subscriptions from the user with the given id. No token is required.")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping(value = "/user/{userId}", produces = "application/json")
  public ResponseEntity<List<SubscriptionDto>> retrieveSubscriptionsFromUserHandler(
      @PathVariable Long userId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscriptionsFromUser(userId), HttpStatus.OK);
  }

  @Operation(summary = "Get all subscriptions from a restaurant", description = "Get the subscriptions from the restaurant with the given id. No token is required.")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping(value = "/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<List<SubscriptionDto>> retrieveSubscriptionsFromRestaurantHandler(
      @PathVariable Long restaurantId) {
    return new ResponseEntity<>(subscriptionService.retrieveSubscriptionsFromRestaurants(restaurantId), HttpStatus.OK);
  }

  @Operation(summary = "Update the rating of a user", description = "Updates the rating of the user on the restaurant, then updates the average of ratings on the restaurant. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some fields/parameters were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The subscription or restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "user/{userId}/restaurant/{restaurantId}", produces = "application/json")
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

  @Operation(summary = "Delete the subscription of a user", description = "Deletes the subscription of the user, then updates the average of ratings on the restaurant. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some parameters were invalid", content = @Content),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The subscription or restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping(value = "user/{userId}/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<HttpStatus> unsubscribeToRestaurantHandler(
      @PathVariable Long userId,
      @Valid @PathVariable Long restaurantId
  ) {
    subscriptionService.unsubscribeToRestaurant(userId, restaurantId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
