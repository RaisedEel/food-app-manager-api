package com.raisedeel.foodappmanager.dish.web;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.dish.service.DishService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/dish")
public class DishController {

  DishService dishService;

  @PostMapping
  public ResponseEntity<DishDto> createDishHandler(@RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.createDish(dishDto), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DishDto> retrieveDishHandler(@PathVariable Long id) {
    return new ResponseEntity<>(dishService.retrieveDish(id), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DishDto> updateDishHandler(@PathVariable Long id, @RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.updateDish(id, dishDto), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteDishHandler(@PathVariable Long id) {
    dishService.deleteDish(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
