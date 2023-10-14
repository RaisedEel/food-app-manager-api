package com.raisedeel.foodappmanager.restaurant.repository;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

/**
 * This Spring Data JPA repository interface is dedicated to the management of {@link Restaurant} entities.
 * It extends the {@link CrudRepository} interface, which provides a set of generic CRUD operations for data access.
 * This repository is designed to work with {@link Restaurant} objects, using their unique {@link Long} identifiers to perform CRUD operations.
 * <p/>
 * It allows you to:
 * <ul>
 *   <li>Retrieve restaurant entities by their IDs.</li>
 *   <li>Save new restaurant entities or update existing ones.</li>
 *   <li>Delete restaurant entities by their IDs.</li>
 *   <li>Query and filter restaurant entities using Spring Data JPA repository query methods.</li>
 * </ul>
 *
 * @see CrudRepository
 * @see Restaurant
 */
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
}
