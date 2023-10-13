package com.raisedeel.foodappmanager.user.repository;

import com.raisedeel.foodappmanager.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This Spring Data JPA repository interface is dedicated to the management of {@link User} entities.
 * It extends the {@link CrudRepository} interface, which provides a set of generic CRUD operations for data access.
 * This repository is designed to work with {@link User} objects, using their unique {@link Long} identifiers to perform CRUD operations.
 * <p/>
 * It allows you to:
 * <ul>
 *   <li>Retrieve user entities by their IDs.</li>
 *   <li>Save new user entities or update existing ones.</li>
 *   <li>Delete user entities by their IDs.</li>
 *   <li>Query and filter user entities using Spring Data JPA repository query methods.</li>
 * </ul>
 *
 * @see CrudRepository
 * @see User
 */
public interface UserRepository extends CrudRepository<User, Long> {

  /**
   * Retrieves the user entity associated the given email (Emails are unique).
   *
   * @param email the email to search for.
   * @return an {@link Optional} containing the matching {@link User} if found, or an empty {@link Optional} if no such
   * entity exists.
   */
  Optional<User> findByEmail(String email);
}
