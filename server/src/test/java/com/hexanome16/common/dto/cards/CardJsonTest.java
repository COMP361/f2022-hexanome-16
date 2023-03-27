package com.hexanome16.common.dto.cards;

import static org.junit.jupiter.api.Assertions.*;

import com.hexanome16.common.models.price.PriceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardJsonTest {
  CardJson underTest;

  @BeforeEach
  void setUp() {
    underTest = new CardJson();
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
    underTest = new CardJson(1, 1, new PriceMap());

    // Act

    // Assert
    assertFalse(underTest.isEmpty());
  }
}