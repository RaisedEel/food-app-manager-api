package com.raisedeel.foodappmanager.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owners")
public class UserOwner extends User {

  private String restaurantId;

  public UserOwner(Long id, String name, String email, String password, String address, Role role, String restaurantId) {
    super(id, name, email, password, address, role);
    this.restaurantId = restaurantId;
  }
}
