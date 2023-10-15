package com.raisedeel.foodappmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.dish.model.Dish;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.raisedeel.foodappmanager.FoodAppManagerApplicationTests.users;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(3)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests for creation, manipulation and deletion of dishes on a restaurant")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DishIntegrationTests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;

  List<Dish> dishes = List.of(
      new Dish(null, "Soup", 40.0, "Soups", "Simple soup", "url", null),
      new Dish(null, "Beef", 30.0, "Meat", "Piece of beef", "url", null)
  );

  @Test
  @DisplayName("Check if creation of a dish is denied for non owner")
  @Order(1)
  public void deniedCreationOfDishTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.post("/dish/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(1).getEmail(), users.get(1).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dishes.get(0)));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }

  @Test
  @DisplayName("Check if successful creation of dishes only for owner")
  @Order(2)
  public void successfulCreationOfDishesTest() throws Exception {
    for (Dish dish : dishes) {
      RequestBuilder request = MockMvcRequestBuilders.post("/dish/restaurant/1")
          .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(dish));

      mockMvc.perform(request)
          .andExpect(status().is2xxSuccessful())
          .andExpect(jsonPath("$.name").value(dish.getName()))
          .andExpect(jsonPath("$.description").value(dish.getDescription()));
    }
  }

  @Test
  @DisplayName("Check if dishes were created and are assigned to the restaurant")
  @Order(3)
  public void successfulGetDishesFromRestaurantTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/dish/restaurant/1");

    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(dishes.size()))
        .andExpect(jsonPath("$.[?(@.id == \"1\" && @.name == \"Soup\")]").exists())
        .andExpect(jsonPath("$.[?(@.id == \"2\" && @.name == \"Beef\")]").exists());
  }

  @Test
  @DisplayName("Check if a non owner is denied manipulation of a dish")
  @Order(4)
  public void deniedPutOfDishByNonOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/dish/2")
        .header("Authorization", JwtTokenUtil.createToken(users.get(1).getEmail(), users.get(1).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dishes.get(1)));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }

  @Test
  @DisplayName("Check if the owner can manipulate a dish")
  @Order(5)
  public void successfulPutOfDishByOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/dish/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            objectMapper.writeValueAsString(
                new Dish(
                    null,
                    "Salad",
                    dishes.get(0).getPrice(),
                    "Salads",
                    "Simple salad",
                    dishes.get(0).getPhotoUrl(),
                    null)
            ));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.name").value("Salad"))
        .andExpect(jsonPath("$.description").value("Simple salad"));
  }

  @Test
  @DisplayName("Check if a non owner can't delete the dish")
  @Order(6)
  public void deniedDeleteOfDishByNonOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.delete("/dish/2")
        .header("Authorization", JwtTokenUtil.createToken(users.get(1).getEmail(), users.get(1).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }

  @Test
  @DisplayName("Check if the owner can delete the dish")
  @Order(7)
  public void successfulDeleteOfDishByOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.delete("/dish/2")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful());

    RequestBuilder getRequest = MockMvcRequestBuilders.get("/dish/restaurant/1");

    mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.[?(@.id == \"1\" && @.name == \"Salad\")]").exists())
        .andExpect(jsonPath("$.[?(@.id == \"2\" && @.name == \"Beef\")]").doesNotExist());
  }

}
