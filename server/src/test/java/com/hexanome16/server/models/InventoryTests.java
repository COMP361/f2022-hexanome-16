package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
  private ServerLevelCard levelCard;
  private Noble noble;

  /**
   * Setting up before each test is done.
   */
  @BeforeEach
  void setup() {
    inventory = new Inventory();
  }

  /**
   * Gem bonuses should be zero when instantiated.
   */
  @Test
  void testGemBonusesInstantiation() {
    // Arrange

    // Act
    var response = inventory.getGemBonuses();

    // Assert
    assertEquals(new PurchaseMap(0, 0, 0, 0, 0, 0), response);
  }

  /**
   * Test to see if a pre-existing card can be successfully added to a player's
   * inventory. The test here is the acquireCard method of the Inventory class.
   */
  @Test
  @DisplayName("Acquire a Level Card successfully")
  void testAcquireCard() {
    levelCard = createValidCard();
    inventory.acquireCard(levelCard);
    assertTrue(inventory.getOwnedCards().contains(levelCard));
    assertEquals(levelCard.getGemBonus(), PurchaseMap.toPurchaseMap(inventory.getGemBonuses()));
  }

  /**
   * Test to see if a face up card can be successfully reserved to a player's
   * inventory. The test here is the reserveCard method of the Inventory class.
   */
  @Test
  @DisplayName("Reserve a face up Level Card successfully")
  void testReserveFaceUp() {
    // by default the card should be face down
    levelCard = createValidCard();
    // add the card to the inventory
    inventory.reserveCard(levelCard);
    // assert it was reserved successfully
    assertTrue(inventory.getReservedCards().contains(levelCard));
    assertTrue(inventory.getReservedCards()
        .get(inventory.getReservedCards().size() - 1)
        .isFaceDown());
  }

  /**
   * Test to see if a face down card can be successfully reserved to a player's
   * inventory. The test here is the reserveCard method of the Inventory class.
   */
  @Test
  @DisplayName("Reserve a face down Level Card successfully")
  void testReserveFaceDown() {
    // by default the card should be face down
    levelCard = createValidCard();
    levelCard.setFaceDown(false);
    // add the card to the inventory
    inventory.reserveCard(levelCard);
    // assert it was reserved successfully
    assertTrue(inventory.getReservedCards().contains(levelCard));
    assertFalse(inventory.getReservedCards()
        .get(inventory.getReservedCards().size() - 1)
        .isFaceDown());
  }

  /**
   * Test to see if a pre-existing noble can be successfully added to a player's
   * inventory. The test here is the acquireNoble method of the Inventory class.
   */
  @Test
  @DisplayName("Acquire a visiting Noble successfully")
  void testAcquireNoble() {
    PriceMap priceMap = new PriceMap(0, 4, 4, 0, 0);
    noble = new ServerNoble(0, 3, "noble0.png", priceMap);
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
    noble = new ServerNoble(0, 3, "noble0.png", priceMap);
    inventory.reserveNoble(noble);
    assertTrue(inventory.getReservedNobles().contains(noble));
  }

  /**
   * Test has at least true.
   */
  @Test
  void testHasAtLeastTrue() {
    // Arrange
    PriceInterface mockPrice = Mockito.mock(PriceInterface.class);

    //Gem bonuses starts at 0
    when(mockPrice.getGemCost(any())).thenReturn(0);

    // Act
    boolean response = inventory.hasAtLeastGivenBonuses(mockPrice);

    // Assert
    assertTrue(response);
  }

  /**
   * Test has at least false.
   */
  @Test
  void testHasAtLeastFalse() {
    // Arrange
    PriceInterface mockPrice = Mockito.mock(PriceInterface.class);

    //Gem bonuses starts at 0
    when(mockPrice.getGemCost(any())).thenReturn(1);

    // Act
    boolean response = inventory.hasAtLeastGivenBonuses(mockPrice);

    // Assert
    assertFalse(response);
  }

  private ServerLevelCard createValidCard() {
    PriceMap priceMap = new PriceMap(3, 0, 0, 0, 0);
    return new ServerLevelCard(0, 0, "level_one0.png", priceMap, Level.ONE, new PurchaseMap(Map.of(
        Gem.RUBY, 1)));
  }
}
