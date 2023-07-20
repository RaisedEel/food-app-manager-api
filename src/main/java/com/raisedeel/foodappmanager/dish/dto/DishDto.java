package com.raisedeel.foodappmanager.dish.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
