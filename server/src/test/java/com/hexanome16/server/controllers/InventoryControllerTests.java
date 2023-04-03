package com.hexanome16.server.controllers;

import static org.mockito.Mockito.verify;

import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.server.services.InventoryService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link InventoryController}.
 */
class InventoryControllerTests {
  private InventoryController underTest;
  @Mock
  private InventoryService inventoryService;
  private AutoCloseable autoCloseable;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    underTest = new InventoryController(inventoryService);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @SneakyThrows
  @Test
  void testGetCards() {
    // Arrange
    long sessionId = 1L;
    String username = "username";

    // Act
    underTest.getCards(sessionId, username);

    // Assert
    verify(inventoryService).getCards(sessionId, username);
  }

  @Test
  void testGetOwnedBonuses() {
    // Arrange
    long sessionId = 1L;
    String username = "username";

    // Act
    underTest.getOwnedBonuses(sessionId, username);

    // Assert
    verify(inventoryService).getOwnedBonuses(sessionId, username);
  }

  @SneakyThrows
  @Test
  void testGetNobles() {
    // Arrange
    long sessionId = 1L;
    String username = "username";

    // Act
    underTest.getNobles(sessionId, username);

    // Assert
    verify(inventoryService).getNobles(sessionId, username);
  }

  @SneakyThrows
  @Test
  void testGetReservedCards() {
    // Arrange
    long sessionId = 1L;
    String username = "username";
    String accessToken = "access";

    // Act
    underTest.getReservedCards(sessionId, username, accessToken);

    // Assert
    verify(inventoryService).getReservedCards(sessionId, username, accessToken);
  }

  @SneakyThrows
  @Test
  void testGetReservedNobles() {
    // Arrange
    long sessionId = 1L;
    String username = "username";

    // Act
    underTest.getReservedNobles(sessionId, username);

    // Assert
    verify(inventoryService).getReservedNobles(sessionId, username);
  }

  @SneakyThrows
  @Test
  void testGetPlayerBankInfo() {
    // Arrange
    long sessionId = 1L;
    String username = "username";

    // Act
    underTest.getPlayerBankInfo(sessionId, username);

    // Assert
    verify(inventoryService).getPlayerBankInfo(sessionId, username);
  }

  @SneakyThrows
  @Test
  void testBuyCard() {
    // Arrange
    long sessionId = 1L;
    String cardMd5 = "cardMd5";
    String access = "access";
    OrientPurchaseMap map = new OrientPurchaseMap(0, 0, 0, 0, 0, 0, 0);

    // Act
    underTest.buyCard(sessionId, cardMd5, access, map);

    // Assert
    verify(inventoryService).buyCard(sessionId, cardMd5, access, map);
  }

  @SneakyThrows
  @Test
  void testGetDiscountedPrice() {
    // Arrange
    long sessionId = 1L;
    String cardMd5 = "cardMd5";
    String access = "access";

    // Act
    underTest.getDiscountedPrice(sessionId, cardMd5, access);

    // Assert
    verify(inventoryService).getDiscountedPrice(sessionId, cardMd5, access);
  }

  @SneakyThrows
  @Test
  void testReserveCard() {
    // Arrange
    long sessionId = 1L;
    String cardMd5 = "cardMd5";
    String access = "access";

    // Act
    underTest.reserveCard(sessionId, cardMd5, access);

    // Assert
    verify(inventoryService).reserveCard(sessionId, cardMd5, access);
  }

  @SneakyThrows
  @Test
  void testReserveFaceDownCard() {
    // Arrange
    long sessionId = 1L;
    String level = "level";
    String access = "access";

    // Act
    underTest.reserveFaceDownCard(sessionId, level, access);

    // Assert
    verify(inventoryService).reserveFaceDownCard(sessionId, level, access);
  }

  @SneakyThrows
  @Test
  void testClaimNoble() {
    // Arrange
    long sessionId = 1L;
    String nobleMd5 = "nobleMd5";
    String access = "access";

    // Act
    underTest.claimNoble(sessionId, nobleMd5, access);

    // Assert
    verify(inventoryService).acquireNoble(sessionId, nobleMd5, access);
  }

  @SneakyThrows
  @Test
  void testTakeLevelTwoCard() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String chosenCard = "chosenCard";

    // Act
    underTest.takeLevelTwoCard(sessionId, access, chosenCard);

    // Assert
    verify(inventoryService).takeLevelTwoCard(sessionId, access, chosenCard);
  }

  @SneakyThrows
  @Test
  void testTakeLevelOneCard() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String chosenCard = "chosenCard";

    // Act
    underTest.takeLevelOneCard(sessionId, access, chosenCard);

    // Assert
    verify(inventoryService).takeLevelOneCard(sessionId, access, chosenCard);
  }

  @SneakyThrows
  @Test
  void testAssociateBagCard() {
    // Arrange
    long sessionId = 1L;
    String access = "access";
    String tokenType = "tokenType";

    // Act
    underTest.associateBagCard(sessionId, access, tokenType);

    // Assert
    verify(inventoryService).associateBagCard(sessionId, access, tokenType);
  }

  @SneakyThrows
  @Test
  void testReserveNoble() {
    long sessionId = 1;
    String cardMd5 = "card";
    String accessToken = "token";
    underTest.reserveNoble(sessionId, cardMd5, accessToken);
    verify(inventoryService).reserveNoble(sessionId, cardMd5, accessToken);
  }
}
