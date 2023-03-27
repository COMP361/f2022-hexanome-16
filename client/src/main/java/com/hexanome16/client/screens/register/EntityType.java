package com.hexanome16.client.screens.register;

import lombok.Getter;

enum EntityType {
  /**
   * Title of the register screen.
   */
  TITLE("title"),
  /**
   * Register form container.
   */
  FORM("form"),
  /**
   * Username field.
   */
  USERNAME("username"),
  /**
   * Password field.
   */
  PASSWORD("password"),
  /**
   * User role field (dropdown).
   */
  ROLE("role"),
  /**
   * Submit button.
   */
  SUBMIT("submit"),
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
