package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.price.PriceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link ServerNoble}.
 */
public class ServerNobleTest {
  ServerNoble noble;

  @BeforeEach
  void setUp() {
    var price = new PriceMap();
    noble = new ServerNoble(123, 2, "boo", price);
  }

  @Test
  void addToInventory() {
  }

  @Test
  void reserveCard() {
  }

  @Test
  void playerMeetsRequirementsReturnsTrueIfHasAtLeast() {
    // Arrange
    var inventory = Mockito.mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(true);

    // Act
    boolean response = noble.playerMeetsRequirements(inventory);

    // Assert
    assertTrue(response);
  }

  @Test
  void playerMeetsRequirementsReturnsFalseIfHasNotAtLeast() {
    // Arrange
    var inventory = Mockito.mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(false);

    // Act
    boolean response = noble.playerMeetsRequirements(inventory);

    // Assert
    assertFalse(response);
  }
}
