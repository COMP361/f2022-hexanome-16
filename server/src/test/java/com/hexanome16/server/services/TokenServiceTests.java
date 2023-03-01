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
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.services.token.TokenService;
import dto.SessionJson;
import models.price.Gem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import util.CustomHttpResponses;

/**
 * Test of {@link TokenService}.
 */
public class TokenServiceTests {

  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(
          JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private GameServiceInterface gameService;
  private TokenService tokensService;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    GameManagerServiceInterface gameManagerMock =
        DummyGameManagerService.getDummyGameManagerService();
    gameService = DummyGameService.getDummyGameService();
    tokensService = new TokenService(gameService, gameManagerMock);

    payload.setPlayers(new ServerPlayer[] {
        objectMapper.readValue(DummyAuths.validJsonList.get(0), ServerPlayer.class),
        objectMapper.readValue(DummyAuths.validJsonList.get(1), ServerPlayer.class)});
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setGame(WinCondition.BASE.getAssocServerName());
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
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
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
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
  }

  /**
   * Testing takeTwo.
   */
  @Test
  public void testTakeTwo() {

    Game validGame = gameService.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken()).getRight().getLeft();
    ServerPlayer validPlayer = PlayerDummies.validDummies[0];
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
    Game validGame = gameService.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken()).getRight().getLeft();
    ServerPlayer validPlayer = PlayerDummies.validDummies[0];
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
}
