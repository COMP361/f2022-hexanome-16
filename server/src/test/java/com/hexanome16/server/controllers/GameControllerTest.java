package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.services.GameService;
import com.hexanome16.server.services.GameServiceInterface;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Unit tests for {@link GameController}.
 */
class GameControllerTest {

  /**
   * The Game controller.
   */
  GameController gameController;

  /**
   * Create game service mock with Mockito.
   *
   * @return the game service mock
   */
  GameServiceInterface createGameServiceMock() {
    return Mockito.mock(GameService.class);
  }

  /**
   * Test get game map.
   */
  @Test
  void testGetGameMap() {
    final HashMap<Long, Game> gameMapStub = new HashMap<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getGameMap()).thenReturn(gameMapStub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(gameMapStub, gameController.getGameMap());
  }

  /**
   * Test create game.
   */
  @Test
  void testCreateGame() {
    final String gameStub = "game test";
    final String game2Stub = "game test 2";

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.createGame(123L, new SessionJson())).thenReturn(gameStub);
    when(gameServiceMock.createGame(124L, new SessionJson())).thenReturn(game2Stub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(gameStub, gameController.createGame(123L, new SessionJson()));
    assertEquals(game2Stub, gameController.createGame(124L, new SessionJson()));
  }

  /**
   * Test get deck.
   */
  @Test
  void testGetDeck() {
    final DeferredResult<ResponseEntity<String>> deckStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getDeck(123L, "ONE", "123", "123B")).thenReturn(deckStub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(deckStub, gameController.getDeck(123L, "ONE", "123", "123B"));
  }

  /**
   * Test get nobles.
   */
  @Test
  void testGetNobles() {
    final DeferredResult<ResponseEntity<String>> noblesStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getNobles(123L, "123", "123B")).thenReturn(noblesStub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(noblesStub, gameController.getNobles(123L, "123", "123B"));
  }

  /**
   * Test get current player.
   */
  @Test
  void testGetCurrentPlayer() {
    final DeferredResult<ResponseEntity<String>> currentPlayerStub = new DeferredResult<>();
    final DeferredResult<ResponseEntity<String>> currentPlayer2Stub = new DeferredResult<>();


    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getCurrentPlayer(123L, "123", "123B")).thenReturn(currentPlayerStub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(currentPlayerStub, gameController.getCurrentPlayer(123L, "123", "123B"));
    assertNotEquals(currentPlayer2Stub, gameController.getCurrentPlayer(123L, "123", "123B"));
  }

  /**
   * Test get winners.
   */
  @Test
  void testGetWinners() {
    final DeferredResult<ResponseEntity<String>> winnersStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getWinners(123L, "123", "123B")).thenReturn(winnersStub);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(winnersStub, gameController.getWinners(123L, "123", "123B"));
  }

  /**
   * Test get player bank info.
   */
  @Test
  void testGetPlayerBankInfo() {
    final ResponseEntity<String> playerBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.getPlayerBankInfo(123L, "tristan")).thenReturn(playerBankInfoStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(playerBankInfoStub, gameController.getPlayerBankInfo(123L, "tristan"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test get game bank info.
   */
  @Test
  void testGetGameBankInfo() {
    final ResponseEntity<String> gameBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.getGameBankInfo(123L)).thenReturn(gameBankInfoStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(gameBankInfoStub, gameController.getGameBankInfo(123L));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test buy card.
   */
  @Test
  void testBuyCard() {
    final ResponseEntity<String> buyCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.buyCard(123L, "md5", "abc", 1, 1, 1, 1, 1, 1)).thenReturn(
          buyCardResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(buyCardResponseStub,
          gameController.buyCard(123L, "md5", "abc", 1, 1, 1, 1, 1, 1));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test reserve card.
   */
  @Test
  void testReserveCard() {
    final ResponseEntity<String> reserveCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.reserveCard(123L, "md5", "abc")).thenReturn(
          reserveCardResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(reserveCardResponseStub, gameController.reserveCard(123L, "md5", "abc"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * Test reserve face down card.
   */
  @Test
  void testReserveFaceDownCard() {
    final ResponseEntity<String> reserveCardFaceDownResponseStub =
        new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.reserveFaceDownCard(123L, "md5", "abc")).thenReturn(
          reserveCardFaceDownResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(reserveCardFaceDownResponseStub,
          gameController.reserveFaceDownCard(123L, "md5", "abc"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }
}