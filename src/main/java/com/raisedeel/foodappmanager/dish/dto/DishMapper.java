package com.raisedeel.foodappmanager.dish.dto;

import com.raisedeel.foodappmanager.dish.model.Dish;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring"
)
public interface DishMapper {
  DishDto dishToDto(Dish dish);

  Dish dtoToDish(DishDto dishDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Dish updateDishFromDto(DishDto dishDto, @MappingTarget Dish dish);
}
