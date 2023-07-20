package com.raisedeel.foodappmanager.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;

  @NotBlank(message = "Name cannot be blank")
  private String name;

  @NotBlank(message = "Email address cannot be blank")
  @Email(message = "The email provided must be valid")
  private String email;

  @NotBlank(message = "Password cannot be blank")
  @Size(min = 5, message = "Password must be larger than 4 characters")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotBlank(message = "Address cannot be blank")
  private String address;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private RestaurantDto restaurantOwned;
  private List<SubscriptionDto> subscriptions;
}
