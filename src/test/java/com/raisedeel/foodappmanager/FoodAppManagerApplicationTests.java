package com.raisedeel.foodappmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.raisedeel.foodappmanager.security.JwtTokenUtil.createToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodAppManagerApplicationTests {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  MockMvc mockMvc;

  List<User> users;
  PasswordEncoder passwordEncoder;

  {
    passwordEncoder = new BCryptPasswordEncoder();

    users = Arrays.asList(
        new User(2L, "Angel", "angel@gmail.com", passwordEncoder.encode("11111"), "Some place", Role.ROLE_CLIENT, null),
        new User(3L, "Carlos", "carlos@gmail.com", passwordEncoder.encode("22222"), "Some place", Role.ROLE_CLIENT, null),
        new User(4L, "Omar", "omar@gmail.com", passwordEncoder.encode("33333"), "Some place", Role.ROLE_CLIENT, null)
    );
  }

  @BeforeAll
  void setup() {
    userRepository.saveAll(users);
  }

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
