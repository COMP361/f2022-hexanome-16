package com.hexanome16.common.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LevelTest {

  @Test
  void testFromStringSuccessIgnoresCase() {
    // Arrange

    // Act
    var result = Level.fromString("oNe");

    // Assert
    assertEquals(Level.ONE, result);
  }

  @Test
  void testFromStringSuccessTrims() {
    // Arrange

    // Act
    var result = Level.fromString("  ONE  ");

    // Assert
    assertEquals(Level.ONE, result);
  }

  @Test
  void testFromStringSuccess() {
    // Arrange

    // Act
    var result = Level.fromString("TWO");

    // Assert
    assertEquals(Level.TWO, result);
  }

  @Test
  void testFromStringNullWhenStringIsBlank() {
    // Arrange

    // Act
    var result = Level.fromString("");

    // Assert
    assertNull(result);
  }

  @Test
  void testFromStringNullWhenNotValidLevel() {
    // Arrange

    // Act
    var result = Level.fromString("NotValidLevel");

    // Assert
    assertNull(result);
  }
}
