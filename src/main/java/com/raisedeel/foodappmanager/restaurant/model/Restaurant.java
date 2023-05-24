package com.raisedeel.foodappmanager.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raisedeel.foodappmanager.user.model.UserOwner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;
  private String description;
  private String email;
  private String telephone;
  private String address;
  private double rating;
  private String photoUrl;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToOne(mappedBy = "restaurantOwned")
  private UserOwner owner;
}
