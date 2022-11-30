package com.hexanome16.server.models.user;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * This class contains information about a user in Lobby Service.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class User {
  private final String name;
  private final String password;
  private final String role;
  private String preferredColour;

//  /**
//   * Constructor.
//   *
//   * @param name            The name of the user.
//   * @param password        The password of the user.
//   * @param preferredColour The preferred colour of the user.
//   * @param role            The role of the user.
//   */
//  public User(String name, String password, String role, String preferredColour) {
//    this.name = name;
//    this.password = password;
//    this.role = role;
//    this.preferredColour = preferredColour;
//  }

}
