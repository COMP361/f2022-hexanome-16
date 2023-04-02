package com.hexanome16.common.models.sessions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RoleTest {

  @Test
  void testFromStringSuccessIgnoresCase() {
    // Arrange

    // Act
    var result = Role.fromString("adMiN");

    // Assert
    assertEquals(Role.ROLE_ADMIN, result);
  }

  @Test
  void testFromStringSuccessTrims() {
    // Arrange

    // Act
    var result = Role.fromString("  PLAYER  ");

    // Assert
    assertEquals(Role.ROLE_PLAYER, result);
  }

  @Test
  void testFromStringSuccess() {
    // Arrange

    // Act
    var result = Role.fromString("SERVICE");

    // Assert
    assertEquals(Role.ROLE_SERVICE, result);
  }

  @Test
  void testFromStringNullWhenStringIsBlank() {
    // Arrange

    // Act
    var result = Role.fromString("");

    // Assert
    assertNull(result);
  }

  @Test
  void testFromStringNullWhenNotValidLevel() {
    // Arrange

    // Act
    var result = Role.fromString("NotValidLevel");

    // Assert
    assertNull(result);
  }

}