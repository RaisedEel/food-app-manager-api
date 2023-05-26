package com.raisedeel.foodappmanager.dish.service;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.dish.dto.DishMapper;
import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.dish.repository.DishRepository;
import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DishServiceImpl implements DishService {

  DishRepository dishRepository;
  RestaurantRepository restaurantRepository;
  DishMapper dishMapper;

  @Override
  public DishDto createDish(DishDto dishDto, Long restaurantId) {
    Dish dish = dishMapper.dtoToDish(dishDto);
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new EntityNotFoundException("Restaurant"));
    
    dish.setRestaurant(restaurant);
    return dishMapper.dishToDto(dishRepository.save(dish));
  }

  @Override
  public DishDto retrieveDish(Long id) {
    return dishMapper.dishToDto(getDishById(id));
  }

  @Override
  public List<DishDto> retrieveDishesByRestaurant(Long restaurantId) {
    return dishRepository.findAllByRestaurantId(restaurantId)
        .stream().map(dishMapper::dishToDto).toList();
  }

  @Override
  public DishDto updateDish(Long id, DishDto dishDto) {
    Dish updatedDish = dishMapper.updateDishFromDto(dishDto, getDishById(id));
    return dishMapper.dishToDto(dishRepository.save(updatedDish));
  }

  @Override
  public void deleteDish(Long id) {
    dishRepository.deleteById(id);
  }

  private Dish getDishById(Long id) {
    return dishRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Dish"));
  }

}
