package com.raisedeel.foodappmanager.subscription.repository;

import com.raisedeel.foodappmanager.subscription.model.Subscription;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * This Spring Data JPA repository interface is dedicated to the management of {@link Subscription} entities.
 * It extends the {@link CrudRepository} interface, which provides a set of generic CRUD operations for data access.
 * This repository is designed to work with {@link Subscription} objects, using their unique {@link Long} identifiers to perform CRUD operations.
 * <p/>
 * It allows you to:
 * <ul>
 *   <li>Retrieve subscription entities by their IDs.</li>
 *   <li>Save new subscription entities or update existing ones.</li>
 *   <li>Delete subscription entities by their IDs.</li>
 *   <li>Query and filter subscription entities using Spring Data JPA repository query methods,
 *   including operations such as calculating the average rating.</li>
 * </ul>
 *
 * @see CrudRepository
 * @see Subscription
 */
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

  /**
   * Find a subscription entity by the IDs of the user and restaurant.
   *
   * @param userId       The ID of the user.
   * @param restaurantId The ID of the restaurant.
   * @return An {@link Optional} containing the matching subscription, if found.
   */
  Optional<Subscription> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

  /**
   * Get a list of subscription entities associated with a specific user.
   *
   * @param userId The ID of the user.
   * @return A list of matching subscription entities.
   */
  List<Subscription> findAllByUserId(Long userId);

  /**
   * Get a list of subscription entities associated with a specific restaurant.
   *
   * @param restaurantId The ID of the restaurant.
   * @return A list of matching subscription entities.
   */
  List<Subscription> findAllByRestaurantId(Long restaurantId);

  /**
   * Calculate the average rating for a restaurant by its ID, excluding ratings with a value of 0.
   *
   * @param restaurantId The ID of the restaurant.
   * @return An {@link Optional} containing the average rating, if available.
   */
  @Query("select AVG(nullif(s.rating, 0)) from Subscription s where s.restaurant.id = :restaurantId")
  Optional<Double> averageOfRatingsByRestaurantId(Long restaurantId);

  /**
   * Delete a subscription entity by the IDs of the user and restaurant.
   *
   * @param userId       The ID of the user.
   * @param restaurantId The ID of the restaurant.
   */
  @Transactional
  @Modifying
  @Query("delete from Subscription s where s.user.id = :userId and s.restaurant.id = :restaurantId")
  void deleteByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
