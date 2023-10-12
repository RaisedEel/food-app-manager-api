package com.raisedeel.foodappmanager.restaurant.web;

import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import com.raisedeel.foodappmanager.restaurant.dto.RestaurantDto;
import com.raisedeel.foodappmanager.restaurant.service.RestaurantService;
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
@Tag(name = "Restaurant Controller", description = "Allow owners the creation and updating of restaurants. All users are allowed retrieving of data. Deletion of restaurants is restricted to the admin.")
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

  RestaurantService restaurantService;

  @Operation(summary = "Create a restaurant", description = "Creates the restaurant using the data sent but will be owner-less. To complete the creation process an administrator has to promote a user. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful creation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful creation. Some fields were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(produces = "application/json")
  public ResponseEntity<RestaurantDto> createRestaurantHandler(
      @Valid @RequestBody RestaurantDto restaurantDto) {
    return new ResponseEntity<>(restaurantService.createRestaurant(restaurantDto), HttpStatus.CREATED);
  }

  @Operation(summary = "Get a restaurant", description = "Get the restaurant with the given id. No token is required.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "404", description = "The restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<RestaurantDto> retrieveRestaurantHandler(@PathVariable Long id) {
    return new ResponseEntity<>(restaurantService.retrieveRestaurant(id), HttpStatus.OK);
  }

  @Operation(summary = "Get all restaurants", description = "Get all the restaurants. No token is required.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation")
  })
  @GetMapping(value = "/all", produces = "application/json")
  public ResponseEntity<List<RestaurantDto>> retrieveRestaurantsHandler() {
    return new ResponseEntity<>(restaurantService.retrieveRestaurants(), HttpStatus.OK);
  }

  @Operation(summary = "Update a restaurant", description = "Updates the past data of the restaurant using the data sent. Access restricted to Owner. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some fields/parameters were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not the owner of the restaurant",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<RestaurantDto> updateRestaurantHandler(
      @PathVariable Long id,
      @Valid @RequestBody RestaurantDto restaurantDto) {
    return new ResponseEntity<>(restaurantService.updateRestaurant(id, restaurantDto), HttpStatus.OK);
  }

  @Operation(summary = "Delete a restaurant", description = "Deletes the restaurant with the given id, if its owner-less. Requires the administrator.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. The parameter was invalid or the restaurant has an owner still",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not an administrator",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping(value = "/remove/{id}", produces = "application/json")
  public ResponseEntity<HttpStatus> deleteRestaurantHandler(@PathVariable Long id) {
    restaurantService.deleteRestaurant(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
