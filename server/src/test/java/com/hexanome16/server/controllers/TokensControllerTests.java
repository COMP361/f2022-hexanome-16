package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.GameService;
import com.hexanome16.server.services.TokenService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test of {@link TokensController}.
 */
public class TokensControllerTests {
  private DummyAuthService dummyAuthService;
  private GameService gameService;
  private TokensController tokensController;
  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private final Map<String, Object> payload = new HashMap<>();
  private String gameResponse;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    dummyAuthService = new DummyAuthService();
    gameService = new GameService(dummyAuthService);
    tokensController  =
        new TokensController(
            new TokenService(gameService, dummyAuthService));

    var playerPayload = List.of(objectMapper.readValue(DummyAuths.validJsonList.get(0), Map.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), Map.class));
    payload.put("players", playerPayload);
    String creator = "tristan";
    payload.put("creator", creator);
    String savegame = "";
    payload.put("savegame", savegame);
    gameResponse = gameService.createGame(DummyAuths.validSessionIds.get(0), payload);
    gameResponse = gameService.createGame(DummyAuths.validSessionIds.get(1), payload);
  }

  /**
   * Test get on /games/{sessionId}/twoTokens;.
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
