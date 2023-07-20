package com.raisedeel.foodappmanager.subscription.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptionDto {
  private Long id;

  @Max(value = 5, message = "Rating cannot be higher than 5 stars")
  @Min(value = 0, message = "Rating cannot be lower than 0 stars")
  private int rating = 0;
}
