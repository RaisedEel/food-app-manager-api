package com.raisedeel.foodappmanager.dish.repository;

import com.raisedeel.foodappmanager.dish.model.Dish;
import org.springframework.data.repository.CrudRepository;

public interface DishRepository extends CrudRepository<Dish, Long> {
}
