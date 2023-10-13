package com.raisedeel.foodappmanager.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raisedeel.foodappmanager.subscription.model.Subscription;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * {@link Entity} class representing a user in the application. This class is used to map user-related data to the database and
 * implements Spring Security's {@link UserDetails} interface for authentication.<br/>
 * This entity creates a "users" table in the database and is designed for inheritance, allowing other user-related entities
 * such as {@link UserOwner} (representing restaurant owners) to extend its functionality.
 * <p/><b>Fields</b>
 * <ul>
 *   <li><b>Name:</b> The complete name of the user.</li>
 *   <li><b>Email:</b> The email of the user, which must be unique.</li>
 *   <li><b>Password:</b> The encoded password for this user.</li>
 *   <li><b>Address:</b> The physical address of the user.</li>
 *   <li><b>Role:</b> The {@link Role} of the user, which defines their privileges.</li>
 *   <li><b>Subscriptions:</b> A list of restaurants to which the user is subscribed.</li>
 * </ul>
 *
 * @see Entity
 * @see UserDetails
 * @see UserOwner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  private String address;
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Subscription> subscriptions;

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.toString()));
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return this.email;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}