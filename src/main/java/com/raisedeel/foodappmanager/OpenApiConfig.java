package com.raisedeel.foodappmanager;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI foodAppAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Food App Manager API")
                .description("Food App Manager API streamlines user and restaurant management on a food platform. It caters to customers and owners, with features like subscriptions and menu customization. JWT token authentication ensures secure access, providing a seamless experience.")
                .version("v0.0.1")
        );
  }

  @Bean
  public GroupedOpenApi publicAPI() {
    return GroupedOpenApi.builder()
        .group("all")
        .displayName("Client and Owners endpoints")
        .pathsToMatch("/**")
        .pathsToExclude("/user/promote/**", "/user/demote/*", "/restaurant/remove/*")
        .build();
  }

  @Bean
  GroupedOpenApi adminAPI() {
    return GroupedOpenApi.builder()
        .group("exclusive")
        .displayName("Admin exclusive endpoints")
        .pathsToMatch("/user/promote/**", "/user/demote/*", "/restaurant/remove/*")
        .build();
  }
}
