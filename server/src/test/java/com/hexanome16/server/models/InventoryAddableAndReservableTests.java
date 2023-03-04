package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PriceMap;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InventoryAddable} and {@link Reservable}.
 */
public class InventoryAddableAndReservableTests {
  private final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);


  private final ServerLevelCard levelCard = new ServerLevelCard(0, 1, "texture.png", priceMap,
      Level.ONE);

  private final ServerNoble noble = new ServerNoble(0, 3, "texture.png", priceMap);

  private final Inventory inventory = new Inventory();

  /**
   * Test add level card to inventory.
   */
  @Test
  public void testAddLevelCardToInventory() {
    levelCard.addToInventory(inventory);
    assertTrue(inventory.getOwnedCards().contains(levelCard));
  }

  /**
   * Test add noble to inventory.
   */
  @Test
  public void testAddNobleToInventory() {
    noble.addToInventory(inventory);
    assertTrue(inventory.getOwnedNobles().contains(noble));
  }

  /**
   * Test reserve level card.
   */
  @Test
  public void testReserveLevelCard() {
    levelCard.reserveCard(inventory);
    assertTrue(inventory.getReservedCards().contains(levelCard));
  }

  /**
   * Test reserve noble.
   */
  @Test
  public void testReserveNoble() {
    noble.reserveCard(inventory);
    assertTrue(inventory.getReservedNobles().contains(noble));
  }
}

