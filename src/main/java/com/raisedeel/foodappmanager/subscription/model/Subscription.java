package com.raisedeel.foodappmanager.subscription.model;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link Entity} class representing a subscription in the application. This class is used to map subscription-related
 * data to the database and represents various attributes of a subscription, including its rating, user, and restaurant.
 * It also enforces uniqueness, ensuring that each user and restaurant pair is unique and not repeated, preventing users
 * from rating the same restaurant more than once.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier for this restaurant, automatically assigned by Spring JPA upon creation.</li>
 *   <li><b>Rating:</b> The user's rating for the restaurant.</li>
 *   <li><b>UserId:</b> A {@link User} entity representing the rating user.</li>
 *   <li><b>Restaurant</b> A {@link Restaurant} entity representing the rated restaurant.</li>
 * </ul>
 *
 * @see Entity
 * @see User
 * @see Restaurant
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"})})
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int rating;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private Restaurant restaurant;
}
