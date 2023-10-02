package com.raisedeel.foodappmanager.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
  private List<Dish> menu;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
  private List<Subscription> subscriptions;

  @JsonIgnore
  public int getTotalOfRatings() {
    return (int) getSubscriptions().stream()
        .filter((sub) -> sub.getRating() > 0)
        .mapToInt(Subscription::getRating).count();
  }
}
