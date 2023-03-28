package com.hexanome16.client.screens.register;

import lombok.Getter;

enum EntityType {
  /**
   * Title of the register screen.
   */
  TITLE("regTitle"),
  /**
   * Register form container.
   */
  FORM("regForm"),
  /**
   * Username label.
   */
  USER_TEXT("regUserText"),
  /**
   * Username field.
   */
  USERNAME("regUsername"),
  /**
   * Password label.
   */
  PASSWORD_TEXT("regPasswordText"),
  /**
   * Password field.
   */
  PASSWORD("regPassword"),
  /**
   * User role label.
   */
  ROLE_TEXT("regRoleText"),
  /**
   * User role field (dropdown).
   */
  ROLE("regRole"),
  /**
   * Submit button.
   */
  SUBMIT("regSubmit"),
  /**
   * Close button.
   */
  CLOSE("regClose"),
  /**
   * Background.
   */
  BACKGROUND("regBackground"),
  /**
   * Message string.
   */
  MESSAGE("regMessage");

  @Getter
  private final String entityName;

  EntityType(String entityName) {
    this.entityName = entityName;
  }
}
