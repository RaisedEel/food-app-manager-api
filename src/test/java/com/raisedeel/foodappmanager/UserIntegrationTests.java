package com.raisedeel.foodappmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import com.raisedeel.foodappmanager.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static com.raisedeel.foodappmanager.FoodAppManagerApplicationTests.users;
import static com.raisedeel.foodappmanager.security.JwtTokenUtil.createToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests for the user endpoints")
public class UserIntegrationTests {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("Check if authentication process is working correctly")
  public void checkAuthenticationProcessTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/user/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new User(null, null, users.get(0).getEmail(), "11111", null, null, null)));

    MvcResult result = mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(header().exists("Authorization"))
        .andReturn();

    String jwtToken = Objects.requireNonNull(result.getResponse().getHeader("Authorization"));

    String user = JwtTokenUtil.getSubjectFromToken(jwtToken);
    String userRole = JwtTokenUtil.getRoleFromToken(jwtToken);

    assertEquals(user, users.get(0).getEmail());
    assertEquals(userRole, users.get(0).getRole().toString());
  }

  @Test
  @DisplayName("Check if authorization process is working for authenticated users")
  public void checkSuccessfulGetUserTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/user/2")
        .header("Authorization", createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.name").value(users.get(0).getName()))
        .andExpect(jsonPath("$.email").value(users.get(0).getEmail()));
  }

  @Test
  @DisplayName("Check if the manipulation of data from the user that owns the data is successful")
  public void checkIfSuccessfulPutFromAuthorizedUserTest() throws Exception {
    String address = "Someplace that is not here";
    RequestBuilder request = MockMvcRequestBuilders.put("/user/2")
        .header("Authorization", createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new User(null, users.get(0).getName(), users.get(0).getEmail(), "11111", address, null, null)));

    mockMvc.perform(request)
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.address").value(address));
  }

  @Test
  @DisplayName("Check if manipulation from an user that don't owns the data is denied")
  public void checkIfForbiddenPutFromUnauthorizedUserTest() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.put("/user/3")
        .header("Authorization", createToken(users.get(0).getEmail(), users.get(0).getRole().toString()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(users.get(1)));

    mockMvc.perform(request)
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value(403));
  }
}