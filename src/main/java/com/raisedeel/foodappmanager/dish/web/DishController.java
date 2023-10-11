package com.raisedeel.foodappmanager.dish.web;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.dish.service.DishService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "Dish Controller", description = "Allow the owners creation, updating and deletion of dishes on their specific restaurant. All users are allowed retrieving of data.")
@RestController
@RequestMapping("/dish")
public class DishController {

  DishService dishService;

  @PostMapping("/restaurant/{restaurantId}")
  public ResponseEntity<DishDto> createDishHandler(@PathVariable Long restaurantId, @Valid @RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.createDish(dishDto, restaurantId), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DishDto> retrieveDishHandler(@PathVariable Long id) {
    return new ResponseEntity<>(dishService.retrieveDish(id), HttpStatus.OK);
  }

  @GetMapping("restaurant/{restaurantId}")
  public ResponseEntity<List<DishDto>> retrieveDishesByRestaurantHandler(@PathVariable Long restaurantId) {
    return new ResponseEntity<>(dishService.retrieveDishesByRestaurant(restaurantId), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DishDto> updateDishHandler(@PathVariable Long id, @Valid @RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.updateDish(id, dishDto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteDishHandler(@PathVariable Long id) {
    dishService.deleteDish(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
