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

class GameControllerTest {

  GameController gameController;

  GameServiceInterface createGameServiceMock() {
    return Mockito.mock(GameService.class);
  }

  @Test
  void testGetGameMap() {
    HashMap<Long, Game> defaultGameMap = new HashMap<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getGameMap()).thenReturn(defaultGameMap);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(defaultGameMap, gameController.getGameMap());
  }

  @Test
  void testCreateGame() {
    String game = "game test";
    String game2 = "game test 2";

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.createGame(123L, new SessionJson())).thenReturn(game);
    when(gameServiceMock.createGame(124L, new SessionJson())).thenReturn(game2);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(game, gameController.createGame(123L, new SessionJson()));
    assertEquals(game2, gameController.createGame(124L, new SessionJson()));
  }

  @Test
  void testGetDeck() {
    DeferredResult<ResponseEntity<String>> deck = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getDeck(123L, "ONE", "123", "123B")).thenReturn(deck);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(deck, gameController.getDeck(123L, "ONE", "123", "123B"));
  }

  @Test
  void testGetNobles() {
    DeferredResult<ResponseEntity<String>> nobles = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getNobles(123L, "123", "123B")).thenReturn(nobles);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(nobles, gameController.getNobles(123L, "123", "123B"));
  }

  @Test
  void testGetCurrentPlayer() {
    DeferredResult<ResponseEntity<String>> currentPlayer = new DeferredResult<>();
    DeferredResult<ResponseEntity<String>> currentPlayer2 = new DeferredResult<>();


    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getCurrentPlayer(123L, "123", "123B")).thenReturn(currentPlayer);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(currentPlayer, gameController.getCurrentPlayer(123L, "123", "123B"));
    assertNotEquals(currentPlayer2, gameController.getCurrentPlayer(123L, "123", "123B"));
  }

  @Test
  void testGetWinners() {
    DeferredResult<ResponseEntity<String>> winners = new DeferredResult<>();

    GameServiceInterface gameServiceMock = createGameServiceMock();
    when(gameServiceMock.getWinners(123L, "123", "123B")).thenReturn(winners);
    this.gameController = new GameController(gameServiceMock);

    assertEquals(winners, gameController.getWinners(123L, "123", "123B"));
  }

  @Test
  void testGetPlayerBankInfo() {
    ResponseEntity<String> playerBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

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

  @Test
  void testGetGameBankInfo() {
    ResponseEntity<String> gameBankInfoStub = new ResponseEntity<>(HttpStatus.OK);

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

  @Test
  void testBuyCard() {
    ResponseEntity<String> buyCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.buyCard(123L, "md5", "abc", 1, 1, 1, 1, 1, 1)).thenReturn(
          buyCardResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(buyCardResponseStub, gameController.buyCard(123L, "md5", "abc", 1, 1, 1, 1, 1, 1));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }

  @Test
  void testReserveCard() {
    ResponseEntity<String> reserveCardResponseStub = new ResponseEntity<>(HttpStatus.OK);

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

  @Test
  void testReserveFaceDownCard() {
    ResponseEntity<String> reserveCardFaceDownResponseStub = new ResponseEntity<>(HttpStatus.OK);

    GameServiceInterface gameServiceMock = createGameServiceMock();
    try {
      when(gameServiceMock.reserveFaceDownCard(123L, "md5", "abc")).thenReturn(
          reserveCardFaceDownResponseStub);
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
    this.gameController = new GameController(gameServiceMock);

    try {
      assertEquals(reserveCardFaceDownResponseStub, gameController.reserveFaceDownCard(123L, "md5", "abc"));
    } catch (JsonProcessingException e) {
      fail("Mock threw a JsonProcessingException");
    }
  }
}