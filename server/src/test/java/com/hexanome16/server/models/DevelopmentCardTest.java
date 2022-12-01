package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DevelopmentCard}.
 */
public class DevelopmentCardTest {
  private final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);

  private final Price price = new TokenPrice(priceMap);

  private final DevelopmentCard levelCard = new LevelCard(0, 1, "texture.png", price, Level.ONE);

  private final DevelopmentCard noble = new Noble(0, 3, "texture.png", price);

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

