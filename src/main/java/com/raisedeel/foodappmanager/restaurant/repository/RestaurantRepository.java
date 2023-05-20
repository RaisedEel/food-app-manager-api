package com.raisedeel.foodappmanager.restaurant.repository;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
}
