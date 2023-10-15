package com.raisedeel.foodappmanager.dish.model;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import jakarta.persistence.*;
import lombok.*;

/**
 * {@link Entity} class representing a dish in the application. This class is used to map dish-related
 * data to the database and represents various attributes of a dish, including its name, price, category, description
 * and its association with a restaurant.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>ID:</b> The unique identifier of the dish, automatically assigned by Spring JPA upon creation.</li>
 *   <li><b>Name:</b> The name of the dish.</li>
 *   <li><b>Price:</b> The price of the dish.</li>
 *   <li><b>Category:</b> The type or category of the dish.</li>
 *   <li><b>Description:</b> A description of the dish.</li>
 *   <li><b>PhotoUrl:</b> The URL to a photo or image representing the dish.</li>
 *   <li><b>Restaurant:</b> The restaurant to which the dish is linked, associated with a {@link Restaurant} entity.</li>
 * </ul>
 *
 * @see Entity
 * @see Restaurant
 */
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
  private String photoUrl;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private Restaurant restaurant;
}
