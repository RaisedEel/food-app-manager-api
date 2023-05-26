package com.raisedeel.foodappmanager.dish.repository;

import com.raisedeel.foodappmanager.dish.model.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {
  List<Dish> findAllByRestaurantId(Long restaurantId);
}
