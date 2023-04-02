package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.DummyGameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

/**
 * Unit test for service utility class.
 */
public class ServiceUtilsTests {
  // fields
  private Game validMockGame;
  private ServiceUtils underTest;

  private GameManagerServiceInterface gameManagerServiceInterface;

  /**
   * Sets .
   */
  @BeforeEach
  void setup() {
    gameManagerServiceInterface = DummyGameManagerService.getDummyGameManagerService();
    validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            WinCondition.BASE);
    underTest = new ServiceUtils(gameManagerServiceInterface,
        new DummyAuthService());
  }

  /**
   * Test find player by name.
   */
  @Test
  void testFindPlayerByName() {
    var player =
        underTest.findPlayerByName(validMockGame, DummyAuths.validPlayerList.get(0).getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by name invalid.
   */
  @Test
  void testFindPlayerByNameInvalid() {
    //Test invalid player
    var player =
        underTest.findPlayerByName(validMockGame, DummyAuths.invalidPlayerList.get(0).getName());
    assertNull(player);
  }

  /**
   * Test find player by token.
   */
  @Test
  void testFindPlayerByToken() {
    var player = this.underTest.findPlayerByToken(validMockGame,
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by token invalid.
   */
  @Test
  void testFindPlayerByTokenInvalid() {
    //Test invalid player
    var player = this.underTest.findPlayerByToken(validMockGame,
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertNull(player);
  }

  /**
   * Testing validRequestAndCurrentTurn(sessionId, authToken).
   */
  @Test
  public void testValidRequestAndCurrentTurn() {
    // Arrange
    Game gameMock = gameManagerServiceInterface.getGame(DummyAuths.validSessionIds.get(0));
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(false);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(true);

    // bad sessionId
    var response = this.underTest.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(),
        response.getLeft().getBody());

    // good sessionId but bad player
    response = this.underTest.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(),
        response.getLeft().getBody());

    // good sessionId and valid token but isn't their turn
    response = this.underTest.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getBody(),
        response.getLeft().getBody());

    // bad sessionId and token
    response = this.underTest.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(),
        response.getLeft().getBody());

    // good sessionId and valid token + is their turn
    response = this.underTest.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }

  /**
   * Testing validRequest(sessionId, authToken).
   */
  @Test
  public void testValidRequest() {
    // Arrange
    Game gameMock = Mockito.mock(Game.class);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(false);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(true);

    // bad sessionId
    var response = this.underTest.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId but bad player
    response = this.underTest.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());

    // bad sessionId and token
    response = this.underTest.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId and valid token
    response = this.underTest.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }

  @Test
  void testEndCurrentPlayersTurn() {
    // Arrange
    Game game = Mockito.mock(Game.class);
    when(game.getCurrentPlayerIndex()).thenReturn(0);
    BroadcastMap broadcastMap = Mockito.mock(BroadcastMap.class);
    when(game.getWinCondition()).thenReturn(WinCondition.BASE);
    when(game.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(game.getBroadcastContentManagerMap()).thenReturn(broadcastMap);

    // Act
    underTest.endCurrentPlayersTurn(game);

    // Assert
    verify(broadcastMap, times(1)).updateValue(any(), any());
  }

  @Test
  void testEndCurrentPlayersTurnWhenMultipleWinners() {
    // Arrange
    Game game = Mockito.mock(Game.class);
    when(game.getCurrentPlayerIndex()).thenReturn(0);
    final BroadcastMap broadcastMap = Mockito.mock(BroadcastMap.class);
    when(game.getWinCondition()).thenReturn(WinCondition.BASE);
    for (ServerPlayer player : PlayerDummies.validDummies) {
      player.getInventory().setPrestigePoints(20);
    }
    when(game.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(game.getBroadcastContentManagerMap()).thenReturn(broadcastMap);

    // Act
    underTest.endCurrentPlayersTurn(game);

    // Assert
    verify(broadcastMap, times(2)).updateValue(any(), any());
  }

  @Test
  void testGetValidPlayerByName() {
    // Arrange

    // Act
    var result = underTest.getValidPlayerByName(DummyAuths.validSessionIds.get(0),
        DummyAuths.validPlayerList.get(0).getName());

    // Assert
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), result.getName());
  }

  @Test
  void testGetValidPlayerByNameThrowsIfInvalidPlayer() {
    // Arrange
    Executable executable = () -> underTest.getValidPlayerByName(DummyAuths.validSessionIds.get(0),
        "invalid player name");

    // Act

    // Assert
    Throwable throwable = assertThrows(IllegalArgumentException.class, executable);
    assertEquals("Invalid Player.", throwable.getMessage());
  }
}
