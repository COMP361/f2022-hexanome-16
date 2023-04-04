package com.hexanome16.common.dto.cards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DevelopmentCardJsonTest {

  private DevelopmentCardJson underTest;

  @BeforeEach
  void setUp() {
    underTest = new DevelopmentCardJson();
  }

  @Test
  void testIsEmpty() {
    // Arrange

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsNotEmpty() {
    // Arrange
    underTest = new DevelopmentCardJson(Level.ONE.name(), LevelCard.BonusType.NONE.name());

    // Act

    // Assert
    assertFalse(underTest.isEmpty());
  }
}