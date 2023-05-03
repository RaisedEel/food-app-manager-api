package com.raisedeel.foodappmanager.user.repository;

import com.raisedeel.foodappmanager.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
