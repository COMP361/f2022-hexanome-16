package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.InventoryAddable;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.price.PriceMap;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.ServiceUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
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
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws IOException {

    validMockGame =
        new Game(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new BaseWinCondition());
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

  private LevelCard createValidCard() {
    return new LevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private LevelCard createInvalidCard() {
    return new LevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }
}
