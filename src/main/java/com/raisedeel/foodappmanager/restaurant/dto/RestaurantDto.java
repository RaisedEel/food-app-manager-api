package com.raisedeel.foodappmanager.restaurant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
}
