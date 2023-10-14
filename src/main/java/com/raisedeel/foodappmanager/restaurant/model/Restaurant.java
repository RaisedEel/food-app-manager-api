package com.raisedeel.foodappmanager.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * {@link Entity} class representing a restaurant in the application. This class is used to map restaurant-related data to the database and
 * represents various attributes of a restaurant, including its name, type, description, contact information, and owner.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier for this restaurant, automatically assigned by Spring JPA upon creation.</li>
 *   <li><b>Name:</b> The name of the restaurant.</li>
 *   <li><b>Type:</b> The type or category of the restaurant.</li>
 *   <li><b>Description:</b> A description of the restaurant.</li>
 *   <li><b>Email:</b> The email address associated with the restaurant.</li>
 *   <li><b>Telephone:</b> The phone number for contacting the restaurant.</li>
 *   <li><b>Address:</b> The physical address of the restaurant.</li>
 *   <li><b>Rating:</b> The overall rating of the restaurant based on user ratings.</li>
 *   <li><b>PhotoUrl:</b> The URL to a photo or image representing the restaurant.</li>
 *   <li><b>Owner:</b> The owner of the restaurant, linked to a {@link UserOwner} entity.</li>
 *   <li><b>Menu:</b> A list of {@link Dish} entities representing the restaurant's menu.</li>
 *   <li><b>Subscriptions:</b> A list of user subscriptions to the restaurant, linked to the {@link Subscription} entity.</li>
 * </ul>
 *
 * @see Entity
 * @see UserOwner
 * @see Dish
 * @see Subscription
 */
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
        .filter((sub) -> sub.getRating() > 0) // Only counts ratings greater than 0
        .mapToInt(Subscription::getRating).count();
  }
}
