package com.raisedeel.foodappmanager.dish.model;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dish {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private double price;
  private String category;
  private String description;
  private String photoUlr;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private Restaurant restaurant;
}
