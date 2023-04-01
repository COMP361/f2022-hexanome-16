package com.hexanome16.common.models.sessions;

import lombok.Getter;

/**
 * This enum represents possible permissions for a user.
 */
public enum Role {
  ROLE_PLAYER,
  ROLE_SERVICE,
  ROLE_ADMIN;

  /**
   * Returns the role from a string.
   *
   * @param role The string to convert.
   * @return The role.
   */
  public static Role fromString(String role) {
    return switch (role.toUpperCase()) {
      case "PLAYER" -> ROLE_PLAYER;
      case "SERVICE" -> ROLE_SERVICE;
      case "ADMIN" -> ROLE_ADMIN;
      default -> null;
    };
  }
}
