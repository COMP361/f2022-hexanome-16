package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.inventory.Inventory;
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

  /**
   * Add to inventory success.
   */
  @Test
  void addToInventorySuccess() {
    // Arrange
    var inventory = Mockito.mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(true);
    when(inventory.acquireNoble(noble)).thenReturn(true);

    // Act
    boolean response = noble.addToInventory(inventory);

    // Assert
    assertTrue(response);
  }

  /**
   * Add to inventory fails sanity check.
   */
  @Test
  void addToInventoryFailsSanityCheck() {
    // Arrange
    var inventory = Mockito.mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(false);

    // Act
    boolean response = noble.addToInventory(inventory);

    // Assert
    assertFalse(response);
  }

  /**
   * Add to inventory fails when inventory tries to acquire.
   */
  @Test
  void addToInventoryFailsWhenInventoryTriesToAcquire() {
    // Arrange
    var inventory = Mockito.mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(true);
    when(inventory.acquireNoble(noble)).thenReturn(false);

    // Act
    boolean response = noble.addToInventory(inventory);

    // Assert
    assertFalse(response);
  }

  @Test
  void reserveCard() {
  }

  /**
   * Player meets requirements returns true if inventory has at least enough gem bonuses.
   */
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

  /**
   * Player meets requirements returns false if inventory has not at least enough gem bonuses.
   */
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
