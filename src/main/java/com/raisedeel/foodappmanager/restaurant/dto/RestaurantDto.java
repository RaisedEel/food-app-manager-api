package com.raisedeel.foodappmanager.restaurant.dto;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantDto {
  private Long id;

  @NotBlank(message = "Restaurant name cannot be blank")
  @Size(max = 60, message = "Restaurant name cannot be larger than 60 characters")
  private String name;

  @NotBlank(message = "Type cannot be blank")
  @Size(max = 60, message = "Type cannot be larger than 60 characters")
  private String type;

  @NotBlank(message = "Description cannot be blank")
  private String description;

  @NotBlank(message = "Restaurant email address cannot be blank")
  @Email(message = "The email provided must be valid")
  private String email;

  @NotBlank(message = "Telephone cannot be blank")
  @Size(max = 10, message = "Number cannot be larger than 10 digits")
  private String telephone;

  @NotBlank(message = "Restaurant address cannot be blank")
  private String address;

  @Max(value = 5, message = "Rating cannot be higher thant 5 stars")
  @Min(value = 0, message = "Rating cannot be lower thant 0 stars")
  private BigDecimal rating = new BigDecimal(0);

  private String photoUrl = "";
  private Set<DishDto> menu;
  private List<SubscriptionDto> subscriptions;
}
