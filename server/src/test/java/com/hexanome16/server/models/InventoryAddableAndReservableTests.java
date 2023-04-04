package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.cards.Reservable;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.inventory.Inventory;
import com.hexanome16.server.models.inventory.InventoryAddable;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link InventoryAddable} and {@link Reservable}.
 */
public class InventoryAddableAndReservableTests {
  private final PriceMap priceMap = new PriceMap(1, 2, 3, 4, 5);


  private final ServerLevelCard levelCard = new ServerLevelCard(0, 1, "texture.png", priceMap,
      Level.ONE, new PurchaseMap(Map.of(Gem.RUBY, 1)));

  private final ServerNoble noble = new ServerNoble(0, 3, "texture.png", priceMap);

  private Inventory inventory = new Inventory();

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
    inventory = Mockito.mock(Inventory.class);
    var price = noble.getCardInfo().price();
    when(inventory.hasAtLeastGivenBonuses(price)).thenReturn(true);
    when(inventory.acquireNoble(noble)).thenReturn(true);
    assertTrue(noble.addToInventory(inventory));
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

