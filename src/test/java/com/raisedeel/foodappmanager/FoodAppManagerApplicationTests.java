package com.raisedeel.foodappmanager;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import com.raisedeel.foodappmanager.security.JwtTokenUtil;
import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Suite
@DisplayName("Application tests setup")
@SelectPackages("com.raisedeel.foodappmanager.integration")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FoodAppManagerApplicationTests {

  public static List<User> users;
  public static Restaurant restaurant = new Restaurant(
      null,
      "Restaurant 1",
      "Bar",
      "Normal Bar",
      "res@gmail.com",
      "7551234567",
      "Some place",
      0,
      "url",
      null,
      null,
      null);

  static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  static {
    users = Arrays.asList(
        new User(2L, "Angel", "angel@gmail.com", passwordEncoder.encode("11111"), "Some place", Role.ROLE_CLIENT, null),
        new User(3L, "Carlos", "carlos@gmail.com", passwordEncoder.encode("22222"), "Some place", Role.ROLE_OWNER, null),
        new User(4L, "Omar", "omar@gmail.com", passwordEncoder.encode("33333"), "Some place", Role.ROLE_CLIENT, null)
    );
  }

  @Autowired
  UserRepository userRepository;

  @Autowired
  MockMvc mockMvc;

  @BeforeAll
  void setup() {
    userRepository.saveAll(users);
  }

  @Test
  @DisplayName("Check the existence of the admin user")
  public void checkIfAdminExists() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/user/1")
        .header("Authorization", JwtTokenUtil.createToken(users.get(0).getEmail(), users.get(0).getRole().toString()));

    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("administrador"));
  }

}
