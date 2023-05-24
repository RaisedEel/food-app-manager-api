package com.raisedeel.foodappmanager.dish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
  private Long id;
  private String name;
  private double price;
  private String category;
  private String description;
}
