package com.raisedeel.foodappmanager.subscription.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a subscription's information. This class is designed to encapsulate subscription data
 * for communication between client and server. It enforces data validation rules for the subscription-related fields and
 * supports serialization/deserialization of subscription information.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier of the subscription.</li>
 *   <li><b>Rating:</b> The user's rating for the restaurant. <em>The rating must be between 0 and 5 stars.</em></li>
 *   <li><b>UserId:</b> The ID of the user. <em>Can only be read in a response.</em></li>
 *   <li><b>RestaurantId:</b> The ID of the restaurant. <em>Can only be read in a response.</em></li>
 * </ul>
 *
 * @see com.raisedeel.foodappmanager.subscription.model.Subscription
 */
@Data
@NoArgsConstructor
public class SubscriptionDto {
  private Long id;

  @Max(value = 5, message = "Rating cannot be higher than 5 stars")
  @Min(value = 0, message = "Rating cannot be lower than 0 stars")
  private int rating = 0;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long userId;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long restaurantId;
}
