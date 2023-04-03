package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.inventory.Inventory;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradePostTest {

  private TradePost underTest;

  @NotNull
  private static ServerLevelCard createCard(Gem type) {
    return new ServerLevelCard(1, 1, "path", new PriceMap(), Level.ONE,
        new PurchaseMap(Map.of(type, 1)));
  }

  @BeforeEach
  void setUp() {
    underTest = new TradePost(RouteType.ONYX_ROUTE);
  }

  @Test
  void testCanBeTakenByPlayerWithOnyxRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.ONYX), createCard(Gem.ONYX), createCard(Gem.ONYX));

    when(inventory.getOwnedCards()).thenReturn(cards);
    underTest = new TradePost(RouteType.ONYX_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithOnyxRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.ONYX), createCard(Gem.ONYX), createCard(Gem.EMERALD));

    when(inventory.getOwnedCards()).thenReturn(cards);
    underTest = new TradePost(RouteType.ONYX_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCanBeTakenByPlayerWithEmeraldRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.EMERALD), createCard(Gem.EMERALD), createCard(Gem.EMERALD),
            createCard(Gem.EMERALD), createCard(Gem.EMERALD));
    List<ServerNoble> nobles = List.of(new ServerNoble());

    when(inventory.getOwnedCards()).thenReturn(cards);

    when(inventory.getOwnedNobles()).thenReturn(nobles);
    underTest = new TradePost(RouteType.EMERALD_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithEmeraldRouteWhenNotEnoughCards() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards = List.of(createCard(Gem.EMERALD), createCard(Gem.ONYX));
    List<ServerNoble> nobles = List.of(new ServerNoble());

    when(inventory.getOwnedCards()).thenReturn(cards);

    when(inventory.getOwnedNobles()).thenReturn(nobles);
    underTest = new TradePost(RouteType.EMERALD_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithEmeraldRouteWhenNotEnoughNobles() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.EMERALD), createCard(Gem.EMERALD), createCard(Gem.EMERALD),
            createCard(Gem.EMERALD), createCard(Gem.EMERALD));
    List<ServerNoble> nobles = List.of();

    when(inventory.getOwnedCards()).thenReturn(cards);

    when(inventory.getOwnedNobles()).thenReturn(nobles);
    underTest = new TradePost(RouteType.EMERALD_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCanBeTakenByPlayerWithSapphireRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.SAPPHIRE), createCard(Gem.SAPPHIRE), createCard(Gem.SAPPHIRE),
            createCard(Gem.ONYX));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.SAPPHIRE_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithSapphireRouteWhenNotEnoughSapphireCards() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards = List.of(createCard(Gem.EMERALD), createCard(Gem.ONYX));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.SAPPHIRE_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithSapphireRouteWhenNotEnoughOnyxCards() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.SAPPHIRE), createCard(Gem.SAPPHIRE), createCard(Gem.SAPPHIRE),
            createCard(Gem.EMERALD));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.SAPPHIRE_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCanBeTakenByPlayerWithDiamondRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.DIAMOND), createCard(Gem.DIAMOND));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.DIAMOND_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithDiamondRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.DIAMOND), createCard(Gem.EMERALD));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.DIAMOND_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCanBeTakenByPlayerWithRubyRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.RUBY), createCard(Gem.RUBY), createCard(Gem.RUBY),
            createCard(Gem.DIAMOND));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.RUBY_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertTrue(result);
  }

  @Test
  void testCannotBeTakenByPlayerWithRubyRoute() {
    // Arrange
    Inventory inventory = mock(Inventory.class);
    List<ServerLevelCard> cards =
        List.of(createCard(Gem.DIAMOND), createCard(Gem.EMERALD));

    when(inventory.getOwnedCards()).thenReturn(cards);

    underTest = new TradePost(RouteType.RUBY_ROUTE);

    // Act
    boolean result = underTest.canBeTakenByPlayerWith(inventory);

    // Assert
    assertFalse(result);
  }

  @Test
  void testGetBonusPrestigePointsForOnyxRoute() {
    // Arrange
    TradePost post = new TradePost(RouteType.ONYX_ROUTE);
    Map<RouteType, TradePost> tradePostMap = Map.of(RouteType.ONYX_ROUTE, post);
    underTest = new TradePost(RouteType.ONYX_ROUTE);

    // Act
    int value = underTest.getBonusPrestigePoints(tradePostMap);

    // Assert
    assertEquals(2, value);
  }

  @Test
  void testGetBonusPrestigePointsForEmeraldRoute() {
    // Arrange
    Map<RouteType, TradePost> tradePostMap = Map.of();
    underTest = new TradePost(RouteType.EMERALD_ROUTE);

    // Act
    int value = underTest.getBonusPrestigePoints(tradePostMap);

    // Assert
    assertEquals(5, value);
  }

  @Test
  void testGetBonusPrestigePointsForDefaultRoute() {
    // Arrange
    Map<RouteType, TradePost> tradePostMap = Map.of();
    underTest = new TradePost(RouteType.RUBY_ROUTE);

    // Act
    int value = underTest.getBonusPrestigePoints(tradePostMap);

    // Assert
    assertEquals(0, value);
  }
}
