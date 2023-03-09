package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.services.longpolling.LongPollingService;
import com.hexanome16.server.services.longpolling.LongPollingServiceInterface;
import org.junit.jupiter.api.DisplayName;
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
   * Create game manager service mock with Mockito.
   *
   * @return the game manager service mock
   */
  GameManagerServiceInterface createGameManagerServiceMock() {
    return Mockito.mock(GameManagerService.class);
  }

  LongPollingServiceInterface createLongPollingServiceMock() {
    return Mockito.mock(LongPollingService.class);
  }

  /**
   * Test create game.
   */
  @Test
  void testCreateGame() {
    final String gameStub = "game test";
    final String game2Stub = "game test 2";
    final SessionJson testJson = new SessionJson();

    GameManagerServiceInterface gameManagerServiceMock = createGameManagerServiceMock();
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    when(gameManagerServiceMock.createGame(123L, testJson)).thenReturn(gameStub);
    when(gameManagerServiceMock.createGame(124L, testJson)).thenReturn(game2Stub);
    this.gameController = new GameController(null, gameManagerServiceMock, longPollingServiceMock);

    assertEquals(gameStub, gameController.createGame(123L, testJson));
    assertEquals(game2Stub, gameController.createGame(124L, testJson));
  }

  /**
   * Test get deck.
   */
  @Test
  void testGetDeck() {
    final DeferredResult<ResponseEntity<String>> deckStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    when(longPollingServiceMock.getDeck(123L, "ONE", "123", "123B")).thenReturn(deckStub);
    this.gameController = new GameController(gameServiceMock, null, longPollingServiceMock);

    assertEquals(deckStub, gameController.getDeck(123L, "ONE", "123", "123B"));
  }

  /**
   * Test get nobles.
   */
  @Test
  void testGetNobles() {
    final DeferredResult<ResponseEntity<String>> noblesStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    when(longPollingServiceMock.getNobles(123L, "123", "123B")).thenReturn(noblesStub);
    this.gameController = new GameController(gameServiceMock, null, longPollingServiceMock);

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
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    when(longPollingServiceMock.getPlayers(123L, "123", "123B"))
        .thenReturn(currentPlayerStub);
    this.gameController = new GameController(gameServiceMock, null, longPollingServiceMock);

    assertEquals(currentPlayerStub, gameController.getPlayers(123L, "123", "123B"));
    assertNotEquals(currentPlayer2Stub, gameController.getPlayers(123L, "123", "123B"));
  }

  /**
   * Test get winners.
   */
  @Test
  void testGetWinners() {
    final DeferredResult<ResponseEntity<String>> winnersStub = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    when(longPollingServiceMock.getWinners(123L, "123", "123B")).thenReturn(winnersStub);
    this.gameController = new GameController(gameServiceMock, null, longPollingServiceMock);

    assertEquals(winnersStub, gameController.getWinners(123L, "123", "123B"));
  }

  /**
   * Test get game bank info.
   */
  @Test
  void testGetGameBankInfo() {
    final ResponseEntity<String> gameBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    LongPollingServiceInterface longPollingServiceMock = createLongPollingServiceMock();
    try {
      when(gameServiceMock.getGameBankInfo(123L)).thenReturn(gameBankInfoStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock, null, longPollingServiceMock);

    try {
      assertEquals(gameBankInfoStub, gameController.getGameBankInfo(123L));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  /**
   * testing get level 2 cards.
   */
  @Test
  @DisplayName("get Level 2 cards on board")
  void testGetLevelTwoOnBoard() {
    GameServiceInterface gameServiceMock = createGameServiceMock();
    gameController = new GameController(gameServiceMock,
        createGameManagerServiceMock(), createLongPollingServiceMock());
    ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
    try {
      when(gameServiceMock.getLevelTwoOnBoard(DummyAuths.validSessionIds.get(0)))
          .thenReturn(res);
      assertEquals(res, gameController.getLevelTwoOnBoard(DummyAuths.validSessionIds.get(0)));
    } catch (Exception e) {
      fail("Mockito threw an exception.");
    }
  }
}


