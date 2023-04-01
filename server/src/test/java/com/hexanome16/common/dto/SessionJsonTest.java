package com.hexanome16.common.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionJsonTest {

  private SessionJson underTest;

  @BeforeEach
  void setUp() {
    underTest = new SessionJson();
  }

  @Test
  void testIsEmpty() {
    // Arrange

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsEmptyWhenNoPlayers() {
    // Arrange
    underTest = new SessionJson(new Player[] {}, "creator", "savegame", "game");

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsNotEmpty() {
    // Arrange
    underTest = new SessionJson(new Player[] {new Player()}, "creator", "savegame", "game");

    // Act

    // Assert
    assertFalse(underTest.isEmpty());
  }
}