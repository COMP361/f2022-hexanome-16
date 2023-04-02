package com.hexanome16.common.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {
  private Player underTest;

  @BeforeEach
  void setUp() {
    underTest = new Player();
  }

  @Test
  void testIsEmptyWhenNameIsBlank() {
    // Arrange
    underTest = new Player("", "color", 1);

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsEmptyWhenNameIsNull() {
    // Arrange
    underTest = new Player(null, "color", 1);

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsNotEmpty() {
    // Arrange
    underTest = new Player("not empty", "color", 1);

    // Act

    // Assert
    assertFalse(underTest.isEmpty());
  }
}
