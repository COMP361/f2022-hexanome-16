package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.util.ServiceUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
            new WinCondition[] {WinCondition.BASE}, false, false);
    gameManagerMock = Mockito.mock(GameManagerService.class);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    gameService = new GameService(new DummyAuthService(), gameManagerMock);

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
        "", new WinCondition[]{WinCondition.BASE}, false, false);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(validGame);

    // Act
    ResponseEntity<String> response =
        gameService.getLevelTwoOnBoard(validSessionId);

    // Assert


    assertTrue(response.getStatusCode().is2xxSuccessful());
    LevelCard[] body = objectMapper.readValue(response.getBody(), LevelCard[].class);
    assertEquals(6, body.length);
  }

}
