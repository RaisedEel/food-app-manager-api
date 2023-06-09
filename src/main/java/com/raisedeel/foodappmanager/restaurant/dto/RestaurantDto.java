package com.raisedeel.foodappmanager.restaurant.dto;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantDto {
  private Long id;
  private String name;
  private String type;
  private String description;
  private String email;
  private String telephone;
  private String address;
  private double rating;
  private String photoUrl;
  private Set<DishDto> menu;
  private List<SubscriptionDto> subscriptions;
}
