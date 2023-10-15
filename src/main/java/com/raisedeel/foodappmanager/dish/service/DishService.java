package com.raisedeel.foodappmanager.dish.service;

import com.raisedeel.foodappmanager.dish.dto.DishDto;

import java.util.List;

/**
 * A service interface for dish management operations.<br/>
 * This interface provides methods for managing dish data, including dish creation, retrieval, update,
 * and deletion.
 */
public interface DishService {

  /**
   * Saves the dish data provided by the {@link DishDto} into the database,
   * associating it with a specific restaurant.
   *
   * @param dishDto      The {@link DishDto} containing dish data to be saved.
   * @param restaurantId The ID of the restaurant to which the dish will be linked.
   * @return The saved {@link DishDto} with updated information.
   */
  DishDto createDish(DishDto dishDto, Long restaurantId);

  /**
   * Retrieves the dish with the given id.
   *
   * @param id the ID of the dish to retrieve.
   * @return the {@link DishDto} representing the retrieved dish.
   */
  DishDto retrieveDish(Long id);

  /**
   * Retrieves the dishes associated with a specific restaurant ID.
   *
   * @param restaurantId the ID of the restaurant's dishes.
   * @return a list of {@link DishDto} representing all dishes.
   */
  List<DishDto> retrieveDishesByRestaurant(Long restaurantId);

  /**
   * Updates the dish with the given id using the data in the {@link DishDto}.
   *
   * @param id      the ID of the dish to update.
   * @param dishDto the {@link DishDto} containing updated dish data.
   * @return the updated {@link DishDto}.
   */
  DishDto updateDish(Long id, DishDto dishDto);

  /**
   * Deletes the dish with the given id from the database.
   *
   * @param id the ID of the dish to delete.
   */
  void deleteDish(Long id);

}
