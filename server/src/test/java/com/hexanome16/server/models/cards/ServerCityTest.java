package com.hexanome16.server.models.cards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.inventory.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerCityTest {

  private ServerCity underTest;

  @BeforeEach
  void setUp() {
    underTest = new ServerCity(1, 5, "texture", new PriceMap());
  }

  @Test
  void testAddToInventory() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(true);
    when(inventory.acquireCity(underTest)).thenReturn(true);

    // Act
    boolean result = underTest.addToInventory(inventory);

    // Assert
    assertTrue(result);
    verify(inventory).hasAtLeastGivenBonuses(underTest.getCardInfo().price());
    verify(inventory).acquireCity(underTest);
  }

  @Test
  void testAddToInventoryFailsWhenPlayerDoesNotMeetRequirements() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    when(inventory.hasAtLeastGivenBonuses(any())).thenReturn(false);

    // Act
    boolean result = underTest.addToInventory(inventory);

    // Assert
    assertFalse(result);
    verify(inventory).hasAtLeastGivenBonuses(underTest.getCardInfo().price());
  }

  @Test
  void testPlayerMeetsRequirements() {
    // Arrange
    Inventory inventory = new Inventory();
    underTest = createFreeCity();

    // Act
    boolean result = underTest.playerMeetsRequirements(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testPlayerMeetsRequirementsExpensive() {
    // Arrange
    Inventory inventory = new Inventory();
    inventory.setGemBonuses(new PurchaseMap(5, 5, 5, 5, 5, 5));
    inventory.setPrestigePoints(10);
    underTest = createExpensiveCity();

    // Act
    boolean result = underTest.playerMeetsRequirements(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testPlayerDoesNotMeetRequirements() {
    // Arrange
    Inventory inventory = new Inventory();
    underTest = createExpensiveCity();

    // Act
    boolean result = underTest.playerMeetsRequirements(inventory);

    // Assert
    assertFalse(result);
  }

  private ServerCity createFreeCity() {
    return new ServerCity(1, 0, "text", new PriceMap());
  }

  private ServerCity createExpensiveCity() {
    return new ServerCity(1, 5, "text", new PriceMap(5, 5, 5, 5, 5));
  }
}
