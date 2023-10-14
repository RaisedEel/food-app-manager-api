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

/**
 * Data Transfer Object (DTO) representing a restaurant's information. This class is designed to encapsulate restaurant data
 * for communication between client and server. It enforces data validation rules for the restaurant-related fields and
 * supports serialization/deserialization of restaurant information.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier of the restaurant.</li>
 *   <li><b>Name:</b> The name of the restaurant. <em>Must not be blank (Maximum: 60 characters).</em></li>
 *   <li><b>Type:</b> The type of the restaurant (e.g., Bar, Café, Seafood restaurant, etc.). <em>Must not be blank (Maximum: 60 characters).</em></li>
 *   <li><b>Description:</b> A description of the restaurant. <em>Must not be blank.</em></li>
 *   <li><b>Email:</b> The email address associated with the restaurant. <em>Must not be blank.</em></li>
 *   <li><b>Telephone:</b> The phone number for contacting the restaurant <em>Must have 10 digits.<em/>.</li>
 *   <li><b>Address:</b> The physical address of the restaurant. <em>Must not be blank.</em></li>
 *   <li><b>Rating:</b> The average rating of the restaurant on a 5-star scale. <em>Can only be read in a response.</em></li>
 *   <li><b>PhotoUrl:</b> The URL to a photo or image representing the restaurant.</li>
 *   <li><b>Menu:</b> A list of {@link DishDto} objects representing the restaurant's menu. <em>Can only be read in a response.</em></li>
 * </ul>
 *
 * @see com.raisedeel.foodappmanager.restaurant.model.Restaurant
 * @see DishDto
 */
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

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<DishDto> menu;
}
