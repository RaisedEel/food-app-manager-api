package com.raisedeel.foodappmanager.user.repository;

import com.raisedeel.foodappmanager.user.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @Query("SELECT a FROM User a WHERE TYPE(a) = UserOwner")
  List<User> findAllOwners();
}
