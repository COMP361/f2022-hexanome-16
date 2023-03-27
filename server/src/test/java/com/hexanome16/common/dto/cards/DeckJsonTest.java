package com.hexanome16.common.dto.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hexanome16.common.models.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeckJsonTest {

  private DeckJson underTest;

  @BeforeEach
  void setUp() {
    underTest = new DeckJson();
  }

  @Test
  void testConstructor() {
    // Arrange
    underTest = new DeckJson(Level.ONE);

    // Act

    // Assert
    assertEquals(Level.ONE, underTest.getDeckLevel());
  }
}