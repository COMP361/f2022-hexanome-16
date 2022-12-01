package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Inventory tests.
 *
 * @author Elea
 */
@ExtendWith(MockitoExtension.class)
public class InventoryTests {
  /* field we are testing */
  private Inventory inventory;

  /* fields we are using */
  private LevelCard levelCard;
  private Noble noble;

  /**
   * Setting up before each test is done.
   */
  @BeforeEach
  void setup() {
    inventory = new Inventory();
  }

  /**
   * Test to see if a pre-existing card can be successfully added to a player's
   * inventory. The test here is the acquireCard method of the Inventory class.
   */
  @Test
  @DisplayName("Acquire a Level Card successfully")
  void testAcquireCard() {
    PriceMap priceMap = new PriceMap(3, 0, 0, 0, 0);
    Price price = new TokenPrice(priceMap);
    levelCard = new LevelCard(0, 0, "level_one0.png", price, Level.ONE);
    inventory.acquireCard(levelCard);
    assertTrue(inventory.getOwnedCards().contains(levelCard));
  }

  /**
   * Test to see if a pre-existing card can be successfully reserved to a player's
   * inventory. The test here is the reserveCard method of the Inventory class.
   */
  @Test
  @DisplayName("Reserve a Level Card successfully")
  void testReserveCard() {
    PriceMap priceMap = new PriceMap(3, 0, 0, 0, 0);
    Price price = new TokenPrice(priceMap);
    levelCard = new LevelCard(0, 0, "level_one0.png", price, Level.ONE);
    inventory.reserveCard(levelCard);
    assertTrue(inventory.getReservedCards().contains(levelCard));
  }

  /**
   * Test to see if a pre-existing noble can be successfully added to a player's
   * inventory. The test here is the acquireNoble method of the Inventory class.
   */
  @Test
  @DisplayName("Acquire a visiting Noble successfully")
  void testAcquireNoble() {
    PriceMap priceMap = new PriceMap(0, 4, 4, 0, 0);
    Price price = new TokenPrice(priceMap);
    noble = new Noble(0, 3, "noble0.png", price);
    inventory.acquireNoble(noble);
    assertTrue(inventory.getOwnedNobles().contains(noble));
  }

  /**
   * Test to see if a pre-existing noble can be successfully reserved to a player's
   * inventory. The test here is the reserveNoble method of the Inventory class.
   */
  @Test
  @DisplayName("Reserve a Noble successfully")
  void testReserveNoble() {
    PriceMap priceMap = new PriceMap(0, 4, 4, 0, 0);
    Price price = new TokenPrice(priceMap);
    noble = new Noble(0, 3, "noble0.png", price);
    inventory.reserveNoble(noble);
    assertTrue(inventory.getReservedNobles().contains(noble));
  }
}
