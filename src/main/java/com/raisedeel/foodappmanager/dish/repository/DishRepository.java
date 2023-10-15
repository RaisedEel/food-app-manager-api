package com.raisedeel.foodappmanager.dish.repository;

import com.raisedeel.foodappmanager.dish.model.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This Spring Data JPA repository interface is dedicated to the management of {@link Dish} entities.
 * It extends the {@link CrudRepository} interface, which provides a set of generic CRUD operations for data access.
 * This repository is designed to work with {@link Dish} objects, using their unique {@link Long} identifiers to perform CRUD operations.
 * <p/>
 * It allows you to:
 * <ul>
 *   <li>Retrieve dish entities by their IDs.</li>
 *   <li>Save new dish entities or update existing ones.</li>
 *   <li>Delete dish entities by their IDs.</li>
 *   <li>Query and filter dish entities using Spring Data JPA repository query methods.</li>
 * </ul>
 *
 * @see CrudRepository
 * @see Dish
 */
public interface DishRepository extends CrudRepository<Dish, Long> {

  /**
   * Get a list of dish entities associated with a specific restaurant.
   *
   * @param restaurantId The ID of the restaurant.
   * @return A list of matching dish entities.
   */
  List<Dish> findAllByRestaurantId(Long restaurantId);
}
