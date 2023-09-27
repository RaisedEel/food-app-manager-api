package com.raisedeel.foodappmanager.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raisedeel.foodappmanager.dish.dto.DishDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BigDecimal rating;

  private String photoUrl = "";
  private List<DishDto> menu;
}
