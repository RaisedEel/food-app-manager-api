package com.raisedeel.foodappmanager.restaurant.web;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

  RestaurantService restaurantService;

  @PostMapping()
  public ResponseEntity<RestaurantDto> createRestaurantHandler(@Valid @RequestBody RestaurantDto restaurantDto) {
    return new ResponseEntity<>(restaurantService.createRestaurant(restaurantDto), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RestaurantDto> retrieveRestaurantHandler(@PathVariable Long id) {
    return new ResponseEntity<>(restaurantService.retrieveRestaurant(id), HttpStatus.OK);
  }

  @GetMapping("/all")
  public ResponseEntity<List<RestaurantDto>> retrieveRestaurantsHandler() {
    return new ResponseEntity<>(restaurantService.retrieveRestaurants(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RestaurantDto> updateRestaurantHandler(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
    return new ResponseEntity<>(restaurantService.updateRestaurant(id, restaurantDto), HttpStatus.OK);
  }

  @DeleteMapping("/remove/{id}")
  public ResponseEntity<HttpStatus> deleteRestaurantHandler(@PathVariable Long id) {
    restaurantService.deleteRestaurant(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
