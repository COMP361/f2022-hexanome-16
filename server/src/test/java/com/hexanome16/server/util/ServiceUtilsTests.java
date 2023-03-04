package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.GameManagerService;
import com.hexanome16.server.services.GameManagerServiceInterface;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.io.IOException;
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
  private GameManagerServiceInterface gameManagerMock;
  private AuthServiceInterface authServiceMock;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws IOException {

    validMockGame =
        new Game(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new BaseWinCondition());

    BroadcastMap broadcastMap = new BroadcastMap();
    BroadcastContentManager<NoblesHash> noblesHashBroadcastContentManager =
        new BroadcastContentManager<>(new NoblesHash(validMockGame));
    broadcastMap.put("noble", noblesHashBroadcastContentManager);
    serviceUtils = new ServiceUtils();
    gameManagerMock = Mockito.mock(GameManagerService.class);
    authServiceMock = new DummyAuthService();
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

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
        DummyAuths.validTokensInfos.get(0).getAccessToken(), authServiceMock);
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
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), authServiceMock);
    assertNull(player);
  }

  /**
   * Testing validRequestAndCurrentTurn(sessionId, authToken).
   */
  @Test
  public void testValidRequestAndCurrentTurn() {
    // Arrange
    Game gameMock = Mockito.mock(Game.class);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(false);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(true);

    // bad sessionId
    var response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId but bad player
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token but isn't their turn
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(1).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getLeft().getStatusCode());

    // bad sessionId and token
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token + is their turn
    response = this.serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
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
        DummyAuths.validTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId but bad player
    response = this.serviceUtils.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode());

    // bad sessionId and token
    response = this.serviceUtils.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token
    response = this.serviceUtils.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), this.gameManagerMock,
        this.authServiceMock);
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }
}
