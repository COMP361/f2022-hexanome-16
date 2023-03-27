package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.util.ServiceUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The type Game service tests.
 */
class GameServiceTests {

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private Game validMockGame;
  private GameService gameService;
  private ServiceUtils serviceUtils;
  private GameManagerServiceInterface gameManagerMock;

  /**
   * Sets up the tests.
   */
  @BeforeEach
  void setup() {

    validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            WinCondition.BASE);
    gameManagerMock = Mockito.mock(GameManagerService.class);
    serviceUtils = Mockito.mock(ServiceUtils.class);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    gameService = new GameService(new DummyAuthService(), gameManagerMock, serviceUtils);

  }

  /**
   * Test get game bank info.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetGameBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response =
        gameService.getGameBankInfo(DummyAuths.validSessionIds.get(0));
    String string = response.getBody();
    PurchaseMap myPm = objectMapper.readValue(string, new TypeReference<>() {
    });

    assertEquals(myPm.getGemCost(Gem.RUBY), 7);
    assertEquals(myPm.getGemCost(Gem.EMERALD), 7);
    assertEquals(myPm.getGemCost(Gem.SAPPHIRE), 7);
    assertEquals(myPm.getGemCost(Gem.DIAMOND), 7);
    assertEquals(myPm.getGemCost(Gem.ONYX), 7);
    assertEquals(myPm.getGemCost(Gem.GOLD), 5);
  }

  /**
   * Test get game bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetGameBankInfoInvalidSessionId()
      throws com.fasterxml.jackson.core.JsonProcessingException {

    // Arrange
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    // Act
    ResponseEntity<String> response;
    response = gameService.getGameBankInfo(DummyAuths.invalidSessionIds.get(0));

    // Assert

    assertTrue(response.getStatusCode().is4xxClientError());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), response.getBody());
  }

  /**
   * testing get level two on board.
   *
   * @throws JsonProcessingException if json fails.
   */
  @Test
  public void testGetLevelTwoOnBoard() throws JsonProcessingException {
    // Arrange
    long validSessionId = DummyAuths.validSessionIds.get(0);

    Game validGame = Game.create(123L,
        PlayerDummies.validDummies, PlayerDummies.validDummies[0].getName(),
        "", WinCondition.BASE);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(validGame);

    // Act
    ResponseEntity<String> response =
        gameService.getLevelTwoOnBoard(validSessionId);

    // Assert


    assertTrue(response.getStatusCode().is2xxSuccessful());
    LevelCard[] body = objectMapper.readValue(response.getBody(), LevelCard[].class);
    assertEquals(6, body.length);
  }

  /**
   * testing get level one on board.
   *
   * @throws JsonProcessingException if json fails.
   */
  @Test
  public void testGetLevelOneOnBoard() throws JsonProcessingException {
    // Arrange
    long validSessionId = DummyAuths.validSessionIds.get(0);

    Game validGame = Game.create(123L,
        PlayerDummies.validDummies, PlayerDummies.validDummies[0].getName(),
        "", WinCondition.BASE);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(validGame);

    // Act
    ResponseEntity<String> response =
        gameService.getLevelOneOnBoard(validSessionId);

    // Assert


    assertTrue(response.getStatusCode().is2xxSuccessful());
    LevelCard[] body = objectMapper.readValue(response.getBody(), LevelCard[].class);
    assertEquals(6, body.length);
  }

  /**
   * testing get level two on board.
   */
  @Test
  public void testGetPlayerAction() {

    long sessionId = DummyAuths.validSessionIds.get(0);
    String accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    ServerPlayer serverPlayer = Mockito.mock(ServerPlayer.class);
    Game game = DummyAuths.validGames.get(sessionId);

    when(gameManagerMock.getGame(123341231)).thenReturn(null);
    when(gameManagerMock.getGame(sessionId))
        .thenReturn(game);
    when(serviceUtils.findPlayerByToken(game, "bad")).thenReturn(null);
    when(serviceUtils.findPlayerByToken(game, accessToken))
        .thenReturn(serverPlayer);
    when(serverPlayer.peekTopAction()).thenReturn(Mockito.mock(Action.class));
    when(serverPlayer.peekTopAction().getActionDetails())
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));


    // Valid everything
    ResponseEntity<String> response =
        gameService.getPlayerAction(sessionId, accessToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());

    // Bad session Id
    response = gameService.getPlayerAction(123341231, accessToken);
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // Bad player
    response = gameService.getPlayerAction(sessionId, "bad");
    assertFalse(response.getStatusCode().is2xxSuccessful());

  }

}
