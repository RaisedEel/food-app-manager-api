package com.raisedeel.foodappmanager.restaurant.web;

import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "Restaurant Controller", description = "Allow owners the creation and updating of restaurants. All users are allowed retrieving of data. Deletion of restaurants is restricted to the admin.")
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
