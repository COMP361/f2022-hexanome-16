package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.services.InventoryService;
import com.hexanome16.server.services.InventoryServiceInterface;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for {@link InventoryController}.
 */
class InventoryControllerTests {
  // field we are testing
  InventoryController inventoryController;

  // fields we are using
  InventoryServiceInterface inventoryServiceMock;
  GameManagerServiceInterface gameManagerServiceMock;
  ServiceUtils serviceUtils;

  /**
   * Create inventory service mock with Mockito.
   *
   * @return the game service mock
   */
  InventoryServiceInterface createInventoryServiceMock() {
    return Mockito.mock(InventoryService.class);
  }

  /**
   * Create game manager service mock with Mockito.
   *
   * @return the game manager service mock
   */
  GameManagerServiceInterface createGameManagerServiceMock() {
    return Mockito.mock(GameManagerService.class);
  }

  /**
   * Create service utils mock with Mockito.
   *
   * @return the game manager service mock
   */
  ServiceUtils createServiceUtilsMock() {
    return Mockito.mock(ServiceUtils.class);
  }

  /**
   * Setting up before each test is done.
   */
  @BeforeEach
  void setUp() {
    this.inventoryServiceMock = createInventoryServiceMock();
    this.gameManagerServiceMock = createGameManagerServiceMock();
    this.serviceUtils = createServiceUtilsMock();
  }

  /**
   * Test get player bank info.
   */
  @Test
  @DisplayName("Get Player Bank info")
  void testGetPlayerBankInfo() {
    final ResponseEntity<String> playerBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

    try {
      when(this.inventoryServiceMock.getPlayerBankInfo(123L, "tristan")).thenReturn(
          playerBankInfoStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }

    this.inventoryController =
        new InventoryController(this.inventoryServiceMock, this.gameManagerServiceMock,
            this.serviceUtils);

    try {
      assertEquals(playerBankInfoStub, this.inventoryController.getPlayerBankInfo(123L, "tristan"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test buy card.
   */
  @Test
  @DisplayName("Buy a card")
  void testBuyCard() {
    final ResponseEntity<String> buyCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

    try {
      when(this.inventoryServiceMock.buyCard(123L, "md5", "abc",
          new PurchaseMap(1, 1, 1, 1, 1, 1))).thenReturn(
          buyCardResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }

    this.inventoryController =
        new InventoryController(this.inventoryServiceMock, this.gameManagerServiceMock,
            this.serviceUtils);

    try {
      assertEquals(buyCardResponseStub,
          this.inventoryController.buyCard(123L, "md5", "abc", new PurchaseMap(1, 1, 1, 1, 1, 1)));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test reserve card.
   */
  @Test
  @DisplayName("Reserve a face up Level Card successfully")
  void testReserveCard() {
    final ResponseEntity<String> reserveCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

    try {
      when(this.inventoryServiceMock.reserveCard(123L, "md5", "abc")).thenReturn(
          reserveCardResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }

    this.inventoryController =
        new InventoryController(this.inventoryServiceMock, this.gameManagerServiceMock,
            this.serviceUtils);

    try {
      assertEquals(reserveCardResponseStub,
          this.inventoryController.reserveCard(123L, "md5", "abc"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test reserve face down card.
   */
  @Test
  @DisplayName("Reserve a face down Level Card successfully")
  void testReserveFaceDownCard() {
    final ResponseEntity<String> reserveCardFaceDownResponseStub =
        new ResponseEntity<>(HttpStatus.OK);

    try {
      when(this.inventoryServiceMock.reserveFaceDownCard(123L, "md5", "abc")).thenReturn(
          reserveCardFaceDownResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }

    this.inventoryController =
        new InventoryController(this.inventoryServiceMock, this.gameManagerServiceMock,
            this.serviceUtils);

    try {
      assertEquals(reserveCardFaceDownResponseStub,
          this.inventoryController.reserveFaceDownCard(123L, "md5", "abc"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

}
