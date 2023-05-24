package com.raisedeel.foodappmanager.dish.service;

import com.raisedeel.foodappmanager.dish.dto.DishDto;

public interface DishService {

  DishDto createDish(DishDto dishDto);

  DishDto retrieveDish(Long id);

  DishDto updateDish(Long id, DishDto dishDto);

  void deleteDish(Long id);

}
