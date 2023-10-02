package com.raisedeel.foodappmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import com.raisedeel.foodappmanager.user.model.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.raisedeel.foodappmanager.FoodAppManagerApplicationTests.restaurant;
import static com.raisedeel.foodappmanager.FoodAppManagerApplicationTests.users;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(2)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests for promotion of owners and their manipulation access")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestaurantIntegrationTests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Check creation of restaurant for any authorized user")
  @Order(1)
  public void successfulCreationOfRestaurantTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.post("/restaurant")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(restaurant));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  @DisplayName("Check if a normal user cannot promote other users")
  @Order(2)
  public void deniedPromotionToOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/user/upgrade/3/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Check if admin can promote an user")
  @Order(3)
  public void successfulPromotionToOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/user/upgrade/2/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken("admin", Role.ROLE_ADMIN.toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful());
    users.get(0).setRole(Role.ROLE_OWNER);
  }

  @Test
  @DisplayName("Check for the creation of the new owner")
  @Order(4)
  public void successfulGetAllOwnersTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/user/owners")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.[?(@.id == \"5\" && @.name == \"Angel\" && @.restaurantOwned.id == \"1\")]").exists());
  }

  @Test
  @DisplayName("Check if non owners can manipulate the restaurant")
  @Order(5)
  public void forbiddenPutIntoRestaurantFromNonOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(2).getEmail(), users.get(2).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(restaurant));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }

  @Test
  @DisplayName("Check if the owner can manipulate the restaurant")
  @Order(6)
  public void successfulPutIntoRestaurantFromOwnerTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            new Restaurant(
                null,
                restaurant.getName(),
                restaurant.getType(),
                "Awesome Bar",
                restaurant.getEmail(),
                restaurant.getTelephone(),
                "Some new place",
                restaurant.getRating(),
                restaurant.getPhotoUrl(),
                restaurant.getOwner(),
                restaurant.getMenu(),
                restaurant.getSubscriptions())
        ));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.description").value("Awesome Bar"))
        .andExpect(jsonPath("$.address").value("Some new place"));
  }

}
