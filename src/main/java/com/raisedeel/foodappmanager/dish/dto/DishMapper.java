package com.raisedeel.foodappmanager.dish.dto;

import com.raisedeel.foodappmanager.dish.model.Dish;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * A Mapper interface annotated with {@link Mapper} that converts between {@link Dish} and {@link DishDto}
 * objects using MapStruct. <br/>
 * This interface defines methods for mapping Dish objects to DishDto objects and vice versa.
 * It also provides a method for updating a Dish object from a DishDto while ignoring null values.
 *
 * @see Mapper
 * @see Dish
 * @see DishDto
 */
@Mapper(
    componentModel = "spring"
)
public interface DishMapper {

  /**
   * Maps a {@link Dish} object to a {@link DishDto} object.
   *
   * @param dish the {@link Dish} object to be mapped to a {@link DishDto}.
   * @return the resulting {@link DishDto}.
   */
  DishDto dishToDto(Dish dish);

  /**
   * Maps a {@link DishDto} object to a {@link Dish} object.
   *
   * @param dishDto the {@link DishDto} object to be mapped to a {@link Dish}.
   * @return the resulting {@link Dish} object.
   */
  Dish dtoToDish(DishDto dishDto);

  /**
   * Updates a {@link Dish} object from a {@link DishDto} while ignoring null values.
   * This method is useful for selectively updating Dish attributes.
   *
   * @param dishDto the {@link DishDto} containing updated information.
   * @param dish    the {@link Dish} object to be updated.
   * @return the updated {@link Dish} object.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Dish updateDishFromDto(DishDto dishDto, @MappingTarget Dish dish);
}
