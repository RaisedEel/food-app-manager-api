package com.raisedeel.foodappmanager.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;
  private String name;
  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private String address;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private RestaurantDto restaurantOwned;
  private List<SubscriptionDto> subscriptions;
}
