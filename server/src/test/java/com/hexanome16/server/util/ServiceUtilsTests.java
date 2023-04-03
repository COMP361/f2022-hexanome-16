package com.hexanome16.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.DummyGameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

  @Test
  void testCheckForNextActions() {
    // Arrange
    Game gameMock = mock(Game.class);
    Deck<ServerNoble> nobles = new Deck<>();
    ServerNoble noble = new ServerNoble();
    nobles.addCard(noble);
    when(gameMock.getOnBoardNobles()).thenReturn(nobles);
    BroadcastMap broadcastMap = mock(BroadcastMap.class);
    when(gameMock.getWinCondition()).thenReturn(WinCondition.BASE);
    when(gameMock.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(gameMock.getBroadcastContentManagerMap()).thenReturn(broadcastMap);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(any())).thenReturn(false);
    when(playerMock.peekTopAction()).thenReturn(null);

    // Act
    ResponseEntity<String> response = underTest.checkForNextActions(gameMock, playerMock);

    // Assert
    assertEquals(CustomHttpResponses.END_OF_TURN.getBody(), response.getBody());
    assertEquals(CustomHttpResponses.END_OF_TURN.getStatus(), response.getStatusCodeValue());
  }

  @Test
  void testCheckForNextActionsReturnsNextAction() {
    // Arrange
    Game gameMock = mock(Game.class);
    Deck<ServerNoble> nobles = new Deck<>();
    ServerNoble noble = new ServerNoble();
    nobles.addCard(noble);
    when(gameMock.getOnBoardNobles()).thenReturn(nobles);
    BroadcastMap broadcastMap = mock(BroadcastMap.class);
    when(gameMock.getWinCondition()).thenReturn(WinCondition.BASE);
    when(gameMock.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(gameMock.getBroadcastContentManagerMap()).thenReturn(broadcastMap);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(any())).thenReturn(false);
    Action actionMock = mock(Action.class);
    ResponseEntity<String> expectedResponse =
        CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO);
    try {
      when(actionMock.getActionDetails()).thenReturn(expectedResponse);
    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    when(playerMock.peekTopAction()).thenReturn(actionMock);

    // Act
    ResponseEntity<String> response = underTest.checkForNextActions(gameMock, playerMock);

    // Assert
    assertEquals(expectedResponse.getBody(), response.getBody());
    assertEquals(expectedResponse.getStatusCodeValue(), response.getStatusCodeValue());
  }

  @SneakyThrows
  @Test
  void testAddNobleAction() {
    // Arrange
    Game gameMock = mock(Game.class);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Deck<ServerNoble> nobles = new Deck<>();
    ServerNoble noble = new ServerNoble();
    nobles.addCard(noble);
    when(gameMock.getOnBoardNobles()).thenReturn(nobles);
    when(playerMock.canBeVisitedBy(noble)).thenReturn(true);

    // Act
    ResponseEntity<String> response = ServiceUtils.addNobleAction(gameMock, playerMock);

    // Assert
    assertNull(response);
    verify(playerMock).addNobleListToPerform(any());
  }

  @SneakyThrows
  @Test
  void testAddNobleActionWhenNoneCanVisit() {
    // Arrange
    Game gameMock = mock(Game.class);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Deck<ServerNoble> nobleDeck = new Deck<>();
    ServerNoble noble = new ServerNoble();
    nobleDeck.addCard(noble);
    when(gameMock.getOnBoardNobles()).thenReturn(nobleDeck);
    when(playerMock.canBeVisitedBy(noble)).thenReturn(false);

    // Act
    ResponseEntity<String> response = ServiceUtils.addNobleAction(gameMock, playerMock);

    // Assert
    assertNull(response);
    verify(playerMock, never()).addNobleListToPerform(any());
  }

  @SneakyThrows
  @Test
  void testAddCityAction() {
    // Arrange
    Game gameMock = mock(Game.class);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Deck<ServerCity> cities = new Deck<>();
    ServerCity city1 = new ServerCity();
    cities.addCard(city1);
    when(gameMock.getOnBoardCities()).thenReturn(cities);
    when(playerMock.canBeVisitedBy(city1)).thenReturn(true);

    // Act
    ResponseEntity<String> response = ServiceUtils.addCityAction(gameMock, playerMock);

    // Assert
    assertNull(response);
    verify(playerMock).addCitiesToPerform(any());
  }

  @SneakyThrows
  @Test
  void testAddCityActionWhenNoneCanVisit() {
    // Arrange
    Game gameMock = mock(Game.class);
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Deck<ServerCity> cities = new Deck<>();
    ServerCity city1 = new ServerCity();
    cities.addCard(city1);
    when(gameMock.getOnBoardCities()).thenReturn(cities);
    when(playerMock.canBeVisitedBy(city1)).thenReturn(false);

    // Act
    ResponseEntity<String> response = ServiceUtils.addCityAction(gameMock, playerMock);

    // Assert
    assertNull(response);
    verify(playerMock, never()).addCitiesToPerform(any());
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
  }
}
