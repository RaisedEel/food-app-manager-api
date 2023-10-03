package com.raisedeel.foodappmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import com.raisedeel.foodappmanager.subscription.dto.SubscriptionDto;
import com.raisedeel.foodappmanager.user.model.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.raisedeel.foodappmanager.FoodAppManagerApplicationTests.users;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Order(4)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests for the correct deletion of a restaurant and resources related")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeletionIntegrationTests {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Check if a user can create subscriptions")
  @Order(1)
  public void creationOfSubscriptionTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.post("/subscription/user/3/restaurant/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(1).getEmail(), users.get(1).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new SubscriptionDto()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.rating").value(0))
        .andExpect(jsonPath("$.userId").value(3))
        .andExpect(jsonPath("$.restaurantId").value(1));
  }

  @Test
  @DisplayName("Check if a restaurant with an owner assigned can be deleted")
  @Order(2)
  public void deniedDeletionOfRestaurantTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.delete("/restaurant/remove/1")
        .header("Authorization", JwtTokenUtil.createToken("admin", Role.ROLE_ADMIN.toString()));

    mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value(400));
  }

  @Test
  @DisplayName("Check if a non admin user can demote an owner")
  @Order(3)
  public void deniedDemotionOfUserTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/user/demote/5")
        .header("Authorization", JwtTokenUtil.createToken(users.get(1).getEmail(), users.get(1).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }

  @Test
  @DisplayName("Check if the admin can demote the owner")
  @Order(4)
  public void successfulDemotionOfUserTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/user/demote/5")
        .header("Authorization", JwtTokenUtil.createToken("admin", Role.ROLE_ADMIN.toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  @DisplayName("Check if the owner is found")
  @Order(5)
  public void emptyGetAllOwnersTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/user/owners")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.size()").value(0))
        .andExpect(jsonPath("$.[?(@.id == \"5\" && @.name == \"Angel\" && @.restaurantOwned.id == \"1\")]").doesNotExist());
  }

  @Test
  @DisplayName("Check if the admin can delete the now owner-less restaurant")
  @Order(6)
  public void successfulDeletionOfUserTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.delete("/restaurant/remove/1")
        .header("Authorization", JwtTokenUtil.createToken("admin", Role.ROLE_ADMIN.toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  @DisplayName("Check if the restaurant can be found")
  @Order(7)
  public void notFoundRestaurantTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/restaurant/1");

    mockMvc.perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value(404));
  }

  @Test
  @DisplayName("Check if the dish was deleted with the restaurant")
  @Order(8)
  public void successfulDeletionOfRestaurantRelatedDishTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/dish/1");

    mockMvc.perform(request)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorCode").value(404));
  }

  @Test
  @DisplayName("Check if the subscription was deleted with the restaurant")
  @Order(9)
  public void successfulDeletionOfRestaurantRelatedSubscriptionTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/subscription/user/6");

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.size()").value(0));
  }

}
