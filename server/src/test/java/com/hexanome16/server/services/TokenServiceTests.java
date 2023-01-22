package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.controllers.TokensController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link TokenService}
 */
public class TokenServiceTests {

  private DummyAuthService dummyAuthService;
  private GameService gameService;
  private TokenService tokensService;
  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(
          JsonCreator.Mode.PROPERTIES));
  private final Map<String, Object> payload = new HashMap<>();



  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    dummyAuthService = new DummyAuthService();
    gameService = new GameService(dummyAuthService);
    tokensService  =
            new TokenService(gameService, dummyAuthService);

    var playerPayload = List.of(objectMapper.readValue(DummyAuths.validJsonList.get(0), Map.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), Map.class));
    payload.put("players", playerPayload);
    String creator = "tristan";
    payload.put("creator", creator);
    String savegame = "";
    payload.put("savegame", savegame);
    gameService.createGame(DummyAuths.validSessionIds.get(0), payload);
    gameService.createGame(DummyAuths.validSessionIds.get(1), payload);
  }

  /**
   * Testing availableTwoTokensType.
   */
  @Test
  public void testAvailableTwoTokensType() {

  }
}
