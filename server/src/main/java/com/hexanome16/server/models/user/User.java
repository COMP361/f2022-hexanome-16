package com.hexanome16.server.models.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
}
