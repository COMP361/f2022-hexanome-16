package com.hexanome16.common.models.sessions;

import java.util.Arrays;

/**
 * This enum represents possible permissions for a user.
 */
public enum Role {
  ROLE_PLAYER("PLAYER"),
  ROLE_SERVICE("SERVICE"),
  ROLE_ADMIN("ADMIN");

  private final String name;

  Role(String name) {
    this.name = name;
  }

  ;

  /**
   * Returns the role from a string.
   *
   * @param role The string to convert.
   * @return The role.
   */
  public static Role fromString(String role) {
    if (role == null || role.isBlank()) {
      return null;
    }
    return Arrays.stream(Role.values()).filter(role1 -> role.trim().equalsIgnoreCase(role1.name))
        .findFirst().orElse(null);
  }
}
