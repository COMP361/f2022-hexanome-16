package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.util.CustomHttpResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

/**
 * Test of {@link TokenService}.
 */
public class TokenServiceTests {

  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(
          JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private DummyAuthService dummyAuthService;
  private GameService gameService;
  private GameManagerServiceInterface gameManagerMock;
  private TokenService tokensService;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    dummyAuthService = new DummyAuthService();
    gameManagerMock = DummyGameManagerService.getDummyGameManagerService();
    gameService = Mockito.mock(GameService.class);
    tokensService =
        new TokenService(gameService, dummyAuthService, gameManagerMock);

    payload.setPlayers(new Player[] {
        objectMapper.readValue(DummyAuths.validJsonList.get(0), Player.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), Player.class)});
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setWinCondition(new BaseWinCondition());
    gameManagerMock.createGame(DummyAuths.validSessionIds.get(0), payload);
    gameManagerMock.createGame(DummyAuths.validSessionIds.get(1), payload);
  }

  /**
   * Testing availableTwoTokensType.
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testAvailableTwoTokensType() throws JsonProcessingException {
    var response = tokensService.availableTwoTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    response = tokensService.availableTwoTokensType(DummyAuths.invalidSessionIds.get(0));
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode());
  }

  /**
   * Testing availableThreeTokensType().
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testAvailableThreeTokensType() throws JsonProcessingException {
    var response = tokensService.availableThreeTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    response = tokensService.availableThreeTokensType(DummyAuths.invalidSessionIds.get(0));
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode());
  }

  /**
   * Testing takeTwo.
   */
  @Test
  public void testTakeTwo() {

    Game validGame = gameManagerMock.getGame(DummyAuths.validSessionIds.get(0));
    Player validPlayer = PlayerDummies.validDummies[0];
    when(gameService.findPlayerByToken(any(),
        eq(DummyAuths.validTokensInfos.get(0).getAccessToken()))).thenReturn(
        validPlayer);
    when(validGame
        .isNotPlayersTurn(validPlayer)).thenReturn(
        false);
    when(validGame.allowedTakeTwoOf(Gem.RUBY)).thenReturn(false);
    when(validGame.allowedTakeTwoOf(Gem.DIAMOND)).thenReturn(true);

    // INVALID REQUEST
    var response = tokensService.takeTwoTokens(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), "RED");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST BUT CANT TAKE TWO OF TOKEN
    response = tokensService.takeTwoTokens(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "RED");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST + CAN TAKE TWO OF TOKEN
    response = tokensService.takeTwoTokens(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "WHITE");
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }


  /**
   * Testing takeThree.
   */
  @Test
  //public void testTakeThree() {
  public void testTakeThreeTokens() {
    Game validGame = gameManagerMock.getGame(DummyAuths.validSessionIds.get(0));
    Player validPlayer = PlayerDummies.validDummies[0];
    when(gameService.findPlayerByToken(any(),
        eq(DummyAuths.validTokensInfos.get(0).getAccessToken()))).thenReturn(
        validPlayer);
    when(validGame
        .isNotPlayersTurn(validPlayer)).thenReturn(
        false);
    when(validGame.allowedTakeThreeOf(Gem.RUBY, Gem.DIAMOND, Gem.ONYX)).thenReturn(false);
    when(validGame.allowedTakeThreeOf(Gem.DIAMOND, Gem.SAPPHIRE, Gem.RUBY)).thenReturn(true);

    // INVALID REQUEST
    var response = tokensService.takeThreeTokens(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), "RED", "WHITE", "BLACK");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST BUT CANT TAKE THREE OF TOKENS
    response = tokensService.takeThreeTokens(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "RED", "WHITE", "BLACK");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST + CAN TAKE THREE OF TOKENS
    response = tokensService.takeThreeTokens(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "WHITE", "BLUE", "RED");
    assertTrue(response.getStatusCode().is2xxSuccessful());

  }


  /**
   * Testing validRequest(sessionId, authToken).
   */
  @Test
  public void testValidRequest() {
    // Arrange
    when(gameService.findPlayerByToken(any(),
        eq(DummyAuths.validTokensInfos.get(0).getAccessToken()))).thenReturn(
        PlayerDummies.validDummies[0]);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))
        .isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(
        false);
    when(gameService.findPlayerByToken(any(),
        eq(DummyAuths.validTokensInfos.get(1).getAccessToken()))).thenReturn(
        PlayerDummies.validDummies[1]);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))
        .isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(
        true);

    // bad sessionId
    var response = tokensService.validRequest(
        DummyAuths.invalidSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId but bad player
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.invalidTokensInfos.get(0).getAccessToken()
    );
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token but isn't their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(1).getAccessToken()
    );
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getLeft().getStatusCode());

    // bad sessionId and token
    response = tokensService.validRequest(
        DummyAuths.invalidSessionIds.get(0), DummyAuths.invalidTokensInfos.get(1).getAccessToken()
    );
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token + is their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }

}
