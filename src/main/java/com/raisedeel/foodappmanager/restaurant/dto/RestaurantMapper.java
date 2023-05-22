package com.raisedeel.foodappmanager.restaurant.dto;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring"
)
public interface RestaurantMapper {

  RestaurantDto restaurantToDto(Restaurant restaurant);

  Restaurant dtoToRestaurant(RestaurantDto restaurantDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Restaurant updateRestaurantFromDto(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

}
