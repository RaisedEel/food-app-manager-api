package com.raisedeel.foodappmanager.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a user's information. This class is designed to encapsulate user data
 * for communication between client and server. It enforces data validation rules for the user-related fields and
 * supports serialization/deserialization of user information.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier of the user.</li>
 *   <li><b>Name:</b> The complete name of the user. <em>Must not be blank.</em></li>
 *   <li><b>Email:</b> The email of the user, which must be unique. <em>Must not be blank.</em></li>
 *   <li><b>Password:</b> Can only be written in a request and won't appear in a response. <em>Minimum: 5 characters.</em></li>
 *   <li><b>Address:</b> The physical address of the user. <em>Must not be blank.</em></li>
 *   <li><b>RestaurantOwned:</b> A {@link RestaurantDto} reference. <em>This field is only visible to owner users and won't
 *   appear to non-owner users.</em></li>
 * </ul>
 *
 * @see com.raisedeel.foodappmanager.user.model.User
 * @see RestaurantDto
 */
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
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private RestaurantDto restaurantOwned;
}
