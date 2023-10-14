package com.raisedeel.foodappmanager.restaurant.dto;

import com.raisedeel.foodappmanager.dish.dto.DishMapper;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import org.mapstruct.*;

/**
 * A Mapper interface annotated with {@link Mapper} that converts between {@link Restaurant} and {@link RestaurantDto}
 * objects using MapStruct. <br/>
 * This interface defines methods for mapping Restaurant objects to RestaurantDto objects and vice versa.
 * It also provides a method for updating a Restaurant object from a RestaurantDto while ignoring null values.
 * Additionally, this mapper utilizes {@link DishMapper} for conversions involving Dish and DishDto objects.
 *
 * @see Mapper
 * @see Restaurant
 * @see RestaurantDto
 * @see DishMapper
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = DishMapper.class
)
public interface RestaurantMapper {

  /**
   * Maps a {@link Restaurant} object to a {@link RestaurantDto} object.
   *
   * @param restaurant the {@link Restaurant} object to be mapped to a {@link RestaurantDto}.
   * @return the resulting {@link RestaurantDto}.
   */
  RestaurantDto restaurantToDto(Restaurant restaurant);

  /**
   * Maps a {@link RestaurantDto} object to a {@link Restaurant} object.
   *
   * @param restaurantDto the {@link RestaurantDto} object to be mapped to a {@link Restaurant}.
   * @return the resulting {@link Restaurant} object.
   */
  Restaurant dtoToRestaurant(RestaurantDto restaurantDto);

  /**
   * Updates a {@link Restaurant} object from a {@link RestaurantDto} while ignoring null values.
   * This method is useful for selectively updating Restaurant attributes.
   *
   * @param restaurantDto the {@link RestaurantDto} containing updated information.
   * @param restaurant    the {@link Restaurant} object to be updated.
   * @return the updated {@link Restaurant} object.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Restaurant updateRestaurantFromDto(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

}
