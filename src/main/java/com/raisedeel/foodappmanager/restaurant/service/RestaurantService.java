package com.raisedeel.foodappmanager.restaurant.service;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {

  RestaurantDto createRestaurant(RestaurantDto restaurantDto);

  RestaurantDto retrieveRestaurant(Long id);

  List<RestaurantDto> retrieveRestaurants();

  RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);

  void deleteRestaurant(Long id);
}
