package com.raisedeel.foodappmanager.dish.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a dish's information. This class is designed to encapsulate dish data
 * for communication between client and server. It enforces data validation rules for the dish-related fields and
 * supports serialization/deserialization of dish information.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier of the dish.</li>
 *   <li><b>Name:</b> The name of the dish. <em>Must not be blank (Maximum: 40 characters).</em></li>
 *   <li><b>Price:</b> The price of the dish. <em>The value must be between 0 and 9999.</em></li>
 *   <li><b>Category:</b> The type or category of the dish (e.g., soups, salads, desserts, etc.). <em>Must not be blank
 *   (Maximum: 60 characters).</em></li>
 *   <li><b>Description:</b> A description of the dish. <em>Must not be blank.</em></li>
 *   <li><b>PhotoUrl:</b> The URL to a photo or image representing the dish.</li>
 * </ul>
 *
 * @see com.raisedeel.foodappmanager.dish.model.Dish
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
  private Long id;

  @NotBlank(message = "Dish name cannot be blank")
  @Size(max = 40, message = "Dish name cannot be larger than 40 characters")
  private String name;

  @Max(value = 9999, message = "Price cannot be higher than $9999")
  @Min(value = 0, message = "Price cannot be lower than $0")
  private BigDecimal price;

  @NotBlank(message = "Category cannot be blank")
  @Size(max = 60, message = "Category cannot be larger than 60 characters")
  private String category;

  @NotBlank(message = "Description cannot be blank")
  private String description;

  private String photoUrl = "";
}
