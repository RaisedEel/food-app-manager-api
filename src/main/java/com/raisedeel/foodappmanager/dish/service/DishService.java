package com.raisedeel.foodappmanager.dish.service;

import com.raisedeel.foodappmanager.dish.dto.DishDto;

import java.util.List;

public interface DishService {

  DishDto createDish(DishDto dishDto, Long restaurantId);

  DishDto retrieveDish(Long id);

  List<DishDto> retrieveDishesByRestaurant(Long restaurantId);

  DishDto updateDish(Long id, DishDto dishDto);

  void deleteDish(Long id);

}
