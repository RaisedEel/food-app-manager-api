package com.raisedeel.foodappmanager.user.model;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

  @OneToOne
  @JoinColumn(name = "restaurantId", referencedColumnName = "id")
  private Restaurant restaurantOwned;

  public UserOwner(Long id, String name, String email, String password, String address, Role role, Restaurant restaurantOwned) {
    super(id, name, email, password, address, role);
    this.restaurantOwned = restaurantOwned;
  }
}
