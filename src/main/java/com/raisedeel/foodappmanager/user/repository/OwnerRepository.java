package com.raisedeel.foodappmanager.user.repository;

import com.raisedeel.foodappmanager.user.model.UserOwner;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends CrudRepository<UserOwner, Long> {
}
