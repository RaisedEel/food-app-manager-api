package com.raisedeel.foodappmanager.restaurant.model;

import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;
  private String description;
  private String email;
  private String telephone;
  private String address;
  private double rating;
  private String photoUrl;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(mappedBy = "restaurantOwned")
  private UserOwner owner;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
  private Set<Dish> menu;
}
