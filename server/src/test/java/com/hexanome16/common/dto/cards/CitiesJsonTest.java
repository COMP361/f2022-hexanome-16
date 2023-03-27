package com.hexanome16.common.dto.cards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.City;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CitiesJsonTest {
  private CitiesJson underTest;

  @BeforeEach
  void setUp() {
    underTest = new CitiesJson(Map.of());
  }

  @Test
  void testConstructor() {
    // Arrange
    City e1 = new City();
    City e2 = new City();
    List<City> cityList = List.of(e1, e2);

    // Act
    try {
      underTest = new CitiesJson(cityList);
    } catch (JsonProcessingException e) {
      fail();
    }

    // Assert
    assertTrue(underTest.getCities().containsValue(e1));
    assertTrue(underTest.getCities().containsValue(e2));
  }

  @Test
  void testIsEmpty() {
    // Arrange
    underTest = new CitiesJson(Map.of());

    // Act

    // Assert
    assertTrue(underTest.isEmpty());
  }

  @Test
  void testIsNotEmpty() {
    // Arrange
    underTest = new CitiesJson(Map.of("City 1", new City()));

    // Act

    // Assert
    assertFalse(underTest.isEmpty());
  }
}