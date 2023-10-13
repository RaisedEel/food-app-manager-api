package com.raisedeel.foodappmanager.user.repository;

import com.raisedeel.foodappmanager.user.model.UserOwner;
import org.springframework.data.repository.CrudRepository;

/**
 * This Spring Data JPA repository interface is dedicated to the management of {@link UserOwner} entities.
 * It extends the {@link CrudRepository} interface, which provides a set of generic CRUD operations for data access.
 * This repository is designed to work with {@link UserOwner} objects, using their unique {@link Long} identifiers to perform CRUD operations.
 * <p/>
 * It allows you to:
 * <ul>
 *   <li>Retrieve owner entities by their IDs.</li>
 *   <li>Save new owner entities or update existing ones.</li>
 *   <li>Delete owner entities by their IDs.</li>
 *   <li>Query and filter owner entities using Spring Data JPA repository query methods.</li>
 * </ul>
 *
 * @see CrudRepository
 * @see UserOwner
 */
public interface OwnerRepository extends CrudRepository<UserOwner, Long> {
}
