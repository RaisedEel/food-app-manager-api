package com.raisedeel.foodappmanager.restaurant.service;

import com.raisedeel.foodappmanager.exception.exceptions.EntityNotFoundException;
import com.raisedeel.foodappmanager.exception.exceptions.InvalidOperationException;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantMapper;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.restaurant.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link RestaurantService} interface for restaurant management operations.
 * This class provides concrete implementations of methods for managing restaurant data, including restaurant creation,
 * retrieval, update, and deletion. It collaborates with various repositories and mappers to fulfill its functionality.
 *
 * @see RestaurantService
 */
@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

  RestaurantRepository restaurantRepository;
  RestaurantMapper restaurantMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
    Restaurant restaurant = restaurantMapper.dtoToRestaurant(restaurantDto);
    return restaurantMapper.restaurantToDto(restaurantRepository.save(restaurant));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestaurantDto retrieveRestaurant(Long id) {
    return restaurantMapper.restaurantToDto(getRestaurantById(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RestaurantDto> retrieveRestaurants() {
    return ((List<Restaurant>) restaurantRepository.findAll())
        .stream()
        .map(restaurantMapper::restaurantToDto)
        .toList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
    Restaurant updatedRestaurant = restaurantMapper.updateRestaurantFromDto(
        restaurantDto,
        getRestaurantById(id)
    );

    return restaurantMapper.restaurantToDto(restaurantRepository.save(updatedRestaurant));
  }

  /**
   * {@inheritDoc}
   * The restaurant must be owner-less.
   */
  @Override
  public void deleteRestaurant(Long id) {
    Restaurant restaurant = getRestaurantById(id);

    if (restaurant.getOwner() != null) {
      throw new InvalidOperationException("This restaurant has an owner. " +
          "First demote the owner before attempting this operation");
    }

    restaurantRepository.delete(restaurant);
  }

  private Restaurant getRestaurantById(Long id) {
    return restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurant"));
  }

}
