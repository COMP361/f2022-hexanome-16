package com.hexanome16.client.screens.register;

import lombok.Getter;

enum EntityType {
  /**
   * Title of the register screen.
   */
  TITLE("title"),
  /**
   * Username field.
   */
  USERNAME("username"),
  /**
   * Password field.
   */
  PASSWORD("password"),
  /**
   * Colour field (color picker?).
   */
  COLOR("color"),
  /**
   * User role field (dropdown).
   */
  ROLE("role"),
  /**
   * Submit button.
   */
  SUBMIT("submit"),
  /**
   * Cancel button.
   */
  CANCEL("cancel"),
  /**
   * Close button.
   */
  CLOSE("close"),
  /**
   * Background.
   */
  BACKGROUND("background"),
  /**
   * Message string.
   */
  MESSAGE("message");

  @Getter
  private final String entityName;

  EntityType(String entityName) {
    this.entityName = entityName;
  }
}
