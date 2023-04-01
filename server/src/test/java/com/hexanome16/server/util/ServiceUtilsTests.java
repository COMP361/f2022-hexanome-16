package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.DummyGameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

/**
 * Unit test for service utility class.
 */
public class ServiceUtilsTests {
  // fields
  private Game validMockGame;
  private ServiceUtils serviceUtils;

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
    serviceUtils = new ServiceUtils(gameManagerServiceInterface,
        new DummyAuthService());
  }

  /**
   * Test find player by name.
   */
  @Test
  void testFindPlayerByName() {
    var player =
        serviceUtils.findPlayerByName(validMockGame, DummyAuths.validPlayerList.get(0).getName());
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
        serviceUtils.findPlayerByName(validMockGame, DummyAuths.invalidPlayerList.get(0).getName());
    assertNull(player);
  }

  /**
   * Test find player by token.
   */
  @Test
  void testFindPlayerByToken() {
    var player = this.serviceUtils.findPlayerByToken(validMockGame,
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
    var player = this.serviceUtils.findPlayerByToken(validMockGame,
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
    var response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(),
        response.getLeft().getBody());

    // good sessionId but bad player
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(),
        response.getLeft().getBody());

    // good sessionId and valid token but isn't their turn
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getBody(),
        response.getLeft().getBody());

    // bad sessionId and token
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(),
        response.getLeft().getBody());

    // good sessionId and valid token + is their turn
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
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
    var response = this.serviceUtils.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId but bad player
    response = this.serviceUtils.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());

    // bad sessionId and token
    response = this.serviceUtils.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId and valid token
    response = this.serviceUtils.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }
}
