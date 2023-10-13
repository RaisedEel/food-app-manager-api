package com.raisedeel.foodappmanager.user.web;

import com.raisedeel.foodappmanager.exception.model.ErrorResponse;
import com.raisedeel.foodappmanager.user.dto.UserDto;
import com.raisedeel.foodappmanager.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
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
@Tag(name = "1.- User Controller", description = "Handle registering, authentication and modification of users. Includes promotions and demotions of owners (Admin endpoints).")
@RestController
@RequestMapping("/user")
public class UserController {

  UserService userService;

  @Operation(summary = "Create new user", description = "Create a new user using the data sent. The same email cannot be used twice.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful creation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful creation. Some fields were invalid or the email is duplicated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<UserDto> createUserHandler(@Valid @RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
  }

  // This handler method is never called is only here for documentation purposes
  @Operation(summary = "Authenticate a user", description = "Authenticate an already registered user. Only the fields email and password are needed. Will send back a Bearer JWT Token on the header.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful authentication",
          headers = @Header(name = "Authorization", description = "A brand-new JWT Token"), content = @Content),
      @ApiResponse(responseCode = "400", description = "Unsuccessful. The fields were invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/authenticate", produces = "application/json")
  public ResponseEntity<HttpStatus> authenticateUserHandler(@RequestBody UserDto userDto) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "Get a user", description = "Gets the user with the given id. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "The id is not a number", content = @Content),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<UserDto> retrieveUserHandler(@PathVariable Long id) {
    return new ResponseEntity<>(userService.retrieveUser(id), HttpStatus.OK);
  }

  @Operation(summary = "Get all owners", description = "Gets all the users that owns restaurants. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @GetMapping(value = "/owners", produces = "application/json")
  public ResponseEntity<List<UserDto>> retrieveOwnersHandler() {
    return new ResponseEntity<>(userService.retrieveOwners(), HttpStatus.OK);
  }

  @Operation(summary = "Update a user", description = "Updates the past data of the user using the data sent. Requires a valid Bearer Token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some fields/parameters were invalid or the email is duplicated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not authorized to modify the user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<UserDto> updateUserHandler(
      @PathVariable Long id,
      @Valid @RequestBody UserDto userDto) {
    return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
  }

  @Operation(summary = "Promote a user to owner", description = "Promotes a normal user to an owner and then links a user and owner-less restaurant together. Requires the administrator.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. Some parameters were invalid or the restaurant has an owner",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not an administrator",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user or restaurant could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "/promote/{userId}/restaurant/{restaurantId}", produces = "application/json")
  public ResponseEntity<HttpStatus> upgradeUserHandler(
      @PathVariable Long userId,
      @PathVariable Long restaurantId) {
    userService.promoteUser(userId, restaurantId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "Demote a owner to a normal user", description = "Demotes a owner to a normal user, losing ownership of his restaurant on the process. Requires the administrator.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
      @ApiResponse(responseCode = "400", description = "Unsuccessful operation. The parameter was invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "The bearer could not be authenticated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "The bearer is not an administrator",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "The user could not be found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PutMapping(value = "/demote/{userId}", produces = "application/json")
  public ResponseEntity<HttpStatus> demoteUserHandler(@PathVariable Long userId) {
    userService.demoteUser(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
