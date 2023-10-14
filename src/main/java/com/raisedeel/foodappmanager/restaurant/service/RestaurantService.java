package com.raisedeel.foodappmanager.restaurant.service;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;

import java.util.List;

/**
 * A service interface for restaurant management operations.<br/>
 * This interface provides methods for managing restaurant data, including restaurant creation, retrieval, update,
 * and deletion.
 */
public interface RestaurantService {

  /**
   * Saves the restaurant data recovered from the {@link RestaurantDto} into the database.
   *
   * @param restaurantDto the {@link RestaurantDto} containing restaurant data to be saved.
   * @return the saved {@link RestaurantDto} with updated information.
   */
  RestaurantDto createRestaurant(RestaurantDto restaurantDto);

  /**
   * Retrieves the restaurant with the given id.
   *
   * @param id the ID of the restaurant to retrieve.
   * @return the {@link RestaurantDto} representing the retrieved restaurant.
   */
  RestaurantDto retrieveRestaurant(Long id);

  /**
   * Retrieves all restaurants from the database.
   *
   * @return a list of {@link RestaurantDto} representing all restaurants.
   */
  List<RestaurantDto> retrieveRestaurants();

  /**
   * Updates the restaurant with the given id using the data in the {@link RestaurantDto}.
   *
   * @param id            the ID of the restaurant to update.
   * @param restaurantDto the {@link RestaurantDto} containing updated restaurant data.
   * @return the updated {@link RestaurantDto}.
   */
  RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);

  /**
   * Deletes the restaurant with the given id from the database.
   *
   * @param id the ID of the restaurant to delete.
   */
  void deleteRestaurant(Long id);
}
