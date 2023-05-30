package com.raisedeel.foodappmanager;

import com.raisedeel.foodappmanager.user.model.Role;
import com.raisedeel.foodappmanager.user.model.User;
import com.raisedeel.foodappmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@SpringBootApplication
public class FoodAppManagerApplication implements CommandLineRunner {

  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(FoodAppManagerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.findByEmail("admin").isEmpty()) {
      // Simple password, better to change in production;
      User admin = new User(null, "administrador", "admin", passwordEncoder().encode("1234"), "Nowhere", Role.ROLE_ADMIN, null);
      userRepository.save(admin);
    }
  }

  @Bean
  UserDetailsService userDetailsService() {
    return username -> userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("No user exists with that email address"));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
