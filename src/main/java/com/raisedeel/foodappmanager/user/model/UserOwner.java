package com.raisedeel.foodappmanager.user.model;

import com.raisedeel.foodappmanager.restaurant.model.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * {@link Entity} class that represents a user owning a restaurant in the application.<br/>
 * This class extends the base {@link User} class and includes a reference to the {@link Restaurant} they own.
 * It creates a new table in the database called "owners," which references the table created by the {@link User} class.
 *
 * @see User
 * @see Restaurant
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owners")
public class UserOwner extends User {

  @OneToOne
  @JoinColumn(name = "restaurantId", referencedColumnName = "id")
  private Restaurant restaurantOwned;

  public UserOwner(Long id, String name, String email, String password, String address, Restaurant restaurantOwned) {
    super(id, name, email, password, address, Role.ROLE_OWNER, null);
    this.restaurantOwned = restaurantOwned;
  }
}
