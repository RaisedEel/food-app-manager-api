package com.raisedeel.foodappmanager.dish.web;

import com.raisedeel.foodappmanager.dish.dto.DishDto;
import com.raisedeel.foodappmanager.dish.service.DishService;
import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Tag(name = "4.- Dish Controller", description = "Allow the owners creation, updating and deletion of dishes on their specific restaurant. All users are allowed retrieving of data.")
@RestController
@RequestMapping("/dish")
public class DishController {

  DishService dishService;

  @Operation(summary = "Create a dish", description = "Creates the dish on the restaurant with the given id using the data sent. Requires the owner of the restaurant.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful creation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful creation. Some fields/parameters were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not the owner of the restaurant",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<DishDto> createDishHandler(
      @PathVariable Long restaurantId,
      @Valid @RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.createDish(dishDto, restaurantId), HttpStatus.CREATED);
  }

  @Operation(summary = "Get a dish", description = "Get the dish with the given id. No token is required.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "404", description = "The dish could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<DishDto> retrieveDishHandler(@PathVariable Long id) {
    return new ResponseEntity<>(dishService.retrieveDish(id), HttpStatus.OK);
  }

  @Operation(summary = "Get all dishes from a restaurant", description = "Get all the dishes from the restaurant with the given id. No token is required.")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping(value = "restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<List<DishDto>> retrieveDishesByRestaurantHandler(@PathVariable Long restaurantId) {
    return new ResponseEntity<>(dishService.retrieveDishesByRestaurant(restaurantId), HttpStatus.OK);
  }

  @Operation(summary = "Update a dish", description = "Updates the past data of the dish using the data sent. Requires the owner of the restaurant.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some fields/parameters were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not the owner of the restaurant",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The dish could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<DishDto> updateDishHandler(
      @PathVariable Long id,
      @Valid @RequestBody DishDto dishDto) {
    return new ResponseEntity<>(dishService.updateDish(id, dishDto), HttpStatus.OK);
  }

  @Operation(summary = "Delete a dish", description = "Deletes the dish with the given id. Requires the owner of the restaurant.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "The parameter was invalid", content = @Content),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not the owner of the restaurant",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The dish could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<HttpStatus> deleteDishHandler(@PathVariable Long id) {
    dishService.deleteDish(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
