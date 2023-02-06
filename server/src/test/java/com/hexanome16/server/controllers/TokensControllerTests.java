package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.GameManagerService;
import com.hexanome16.server.services.GameManagerServiceInterface;
import com.hexanome16.server.services.GameService;
import com.hexanome16.server.services.TokenService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test of {@link TokensController}.
 */
public class TokensControllerTests {
  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private TokensController tokensController;
  private String gameResponse;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    DummyAuthService dummyAuthService = new DummyAuthService();
    GameManagerServiceInterface gameManagerMock = Mockito.mock(GameManagerService.class);
    GameService gameService = new GameService(dummyAuthService, gameManagerMock);
    tokensController =
        new TokensController(
            new TokenService(gameService, dummyAuthService, gameManagerMock));
    payload.setPlayers(new Player[] {
        objectMapper.readValue(DummyAuths.validJsonList.get(0), Player.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), Player.class)});
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setWinCondition(new BaseWinCondition());
    try {
      Game game1 = new Game(DummyAuths.validSessionIds.get(0), payload);
      Game game2 = new Game(DummyAuths.validSessionIds.get(1), payload);
    } catch (IOException e) {
      fail("Game creation failed");
    }
    when(gameManagerMock.createGame(DummyAuths.validSessionIds.get(0), payload)).thenReturn(
        "success");
    when(gameManagerMock.createGame(DummyAuths.validSessionIds.get(1), payload)).thenReturn(
        "success");
    gameResponse = gameManagerMock.createGame(DummyAuths.validSessionIds.get(0), payload);
    gameResponse = gameManagerMock.createGame(DummyAuths.validSessionIds.get(1), payload);
  }

  /**
   * Test get on /games/{sessionId}/twoTokens;.
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testGetTwoTokens() throws JsonProcessingException {
    ResponseEntity<String> myResponse =
        tokensController.availableTwoTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(myResponse.getStatusCode(), HttpStatus.OK);
    Set<String> available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(myResponse.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "GREEN", "BLUE", "WHITE", "BLACK", "NULL"));
  }

  /**
   * Test get on /games/{sessionId}/threeTokens;.
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testGetThreeTokens() throws JsonProcessingException {
    ResponseEntity<String> myResponse =
        tokensController.availableThreeTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(myResponse.getStatusCode(), HttpStatus.OK);
    Set<String> available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(myResponse.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "GREEN", "BLUE", "WHITE", "BLACK", "NULL"));
  }

  /**
   * Test Put on /games/{sessionId}/twoTokens;.
   */
  @Test
  public void testPutTwoTokens() {
    ResponseEntity<String> myResponse =
        tokensController.takeTwoTokens(
            DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(), "RED");
    assertEquals(myResponse.getStatusCode(), HttpStatus.OK);
  }

  /**
   * Test Put on /games/{sessionId}/threeTokens;.
   */
  @Test
  public void testPutThreeTokens() {
    ResponseEntity<String> myResponse =
        tokensController.takeThreeTokens(
            DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(),
            "RED", "GREEN", "WHITE");
    assertEquals(myResponse.getStatusCode(), HttpStatus.OK);
  }

}
