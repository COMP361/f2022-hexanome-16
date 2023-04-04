package com.hexanome16.server.controllers;

import static org.mockito.Mockito.verify;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.longpolling.LongPollingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link GameController}.
 */
class GameControllerTest {
  @Mock
  GameService gameService;
  @Mock
  GameManagerService gameManagerService;
  @Mock
  LongPollingService longPollingService;
  AutoCloseable autoCloseable;
  private GameController underTest;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    underTest = new GameController(gameService, gameManagerService, longPollingService);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  void testCreateGame() {
    // Arrange
    long id = 1L;
    SessionJson sessionJson = new SessionJson();

    // Act
    underTest.createGame(id, sessionJson);

    // Assert
    verify(gameManagerService).createGame(id, sessionJson);
  }

  @Test
  void testDeleteGame() {
    // Arrange
    long id = 1L;

    // Act
    underTest.deleteGame(id);

    // Assert
    verify(gameManagerService).deleteGame(id);
  }

  @Test
  void testGetDeck() {
    // Arrange
    long id = 1L;
    String level = "level";
    String accessToken = "access";
    String hash = "hash";

    // Act
    underTest.getDeck(id, level, accessToken, hash);

    // Assert
    verify(longPollingService).getDeck(id, level, accessToken, hash);
  }

  @SneakyThrows
  @Test
  void testGetLevelTwoOnBoard() {
    // Arrange
    long id = 1L;

    // Act
    underTest.getLevelTwoOnBoard(id);

    // Assert
    verify(gameService).getLevelTwoOnBoard(id);
  }

  @SneakyThrows
  @Test
  void testGetLevelOneOnBoard() {
    // Arrange
    long id = 1L;

    // Act
    underTest.getLevelOneOnBoard(id);

    // Assert
    verify(gameService).getLevelOneOnBoard(id);
  }

  @Test
  void testGetNobles() {
    // Arrange
    long id = 1L;
    String accessToken = "access";
    String hash = "hash";

    // Act
    underTest.getNobles(id, accessToken, hash);

    // Assert
    verify(longPollingService).getNobles(id, accessToken, hash);
  }

  @Test
  void testGetCities() {
    // Arrange
    long id = 1L;
    String accessToken = "access";
    String hash = "hash";

    // Act
    underTest.getCities(id, accessToken, hash);

    // Assert
    verify(longPollingService).getCities(id, accessToken, hash);
  }

  @Test
  void testGetPlayers() {
    // Arrange
    long id = 1L;
    String accessToken = "access";
    String hash = "hash";

    // Act
    underTest.getPlayers(id, accessToken, hash);

    // Assert
    verify(longPollingService).getPlayers(id, accessToken, hash);
  }

  @Test
  void testGetWinners() {
    // Arrange
    long id = 1L;
    String accessToken = "access";
    String hash = "hash";

    // Act
    underTest.getWinners(id, accessToken, hash);

    // Assert
    verify(longPollingService).getWinners(id, accessToken, hash);
  }

  @SneakyThrows
  @Test
  void testGetGameBankInfo() {
    // Arrange
    long id = 1L;

    // Act
    underTest.getGameBankInfo(id);

    // Assert
    verify(gameService).getGameBankInfo(id);
  }

  @Test
  void testGetPlayerAction() {
    // Arrange
    long id = 1L;
    String username = "username";
    String access = "access";

    // Act
    underTest.getPlayerAction(id, username, access);

    // Assert
    verify(gameService).getPlayerAction(id, access);
  }
}


