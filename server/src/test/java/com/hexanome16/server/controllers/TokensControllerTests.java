package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.token.TokenService;
import dto.SessionJson;
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
  private TokenService tokenService;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    DummyAuthService dummyAuthService = new DummyAuthService();
    GameService gameService = Mockito.mock(GameService.class);
    tokenService = Mockito.mock(TokenService.class);
    tokensController =
        new TokensController(tokenService);
    payload.setPlayers(new ServerPlayer[] {
        objectMapper.readValue(DummyAuths.validJsonList.get(0), ServerPlayer.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), ServerPlayer.class)});
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setGame(WinCondition.BASE.getAssocServerName());
    Game game1 = Game.create(DummyAuths.validSessionIds.get(0), payload);
    Game game2 = Game.create(DummyAuths.validSessionIds.get(1), payload);
    GameManagerServiceInterface gameManagerMock = Mockito.mock(GameManagerService.class);
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
    when(tokenService.availableTwoTokensType(DummyAuths.validSessionIds.get(0)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    var response = tokensController.availableTwoTokensType(DummyAuths.validSessionIds.get(0));
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test get on /games/{sessionId}/threeTokens;.
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testGetThreeTokens() throws JsonProcessingException {
    when(tokenService.availableThreeTokensType(DummyAuths.validSessionIds.get(0)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    var response = tokensController.availableThreeTokensType(DummyAuths.validSessionIds.get(0));
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test Put on /games/{sessionId}/twoTokens;.
   */
  @Test
  public void testPutTwoTokens() {
    long validSessionId = DummyAuths.validSessionIds.get(0);
    String validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    when(tokenService.takeTwoTokens(validSessionId, validAccessToken, "RED"))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    var response = tokensController.takeTwoTokens(validSessionId, validAccessToken, "RED");
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test Put on /games/{sessionId}/threeTokens;.
   */
  @Test
  public void testPutThreeTokens() {
    long validSessionId = DummyAuths.validSessionIds.get(0);
    String validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    when(tokenService.takeThreeTokens(validSessionId, validAccessToken, "RED", "WHITE", "GREEN"))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    var response =
        tokensController.takeThreeTokens(validSessionId, validAccessToken, "RED", "WHITE", "GREEN");
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

}
