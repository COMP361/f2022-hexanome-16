package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.ServiceUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test of {@link TokenService}.
 */

public class TokenServiceTests {

  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      new com.fasterxml.jackson.databind.ObjectMapper().registerModule(new ParameterNamesModule(
          JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private DummyAuthService dummyAuthService;
  private GameManagerServiceInterface gameManagerMock;
  private TokenService tokensService;
  @Mock
  private final ServiceUtils serviceUtils = Mockito.mock(ServiceUtils.class);

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    dummyAuthService = new DummyAuthService();
    gameManagerMock = DummyGameManagerService.getDummyGameManagerService();
    tokensService =
        new TokenService(dummyAuthService, gameManagerMock, serviceUtils);
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
    Game validGame = new GameDummies().validGames.get(0);

    Player validPlayer = PlayerDummies.validDummies[0];

    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), gameManagerMock, dummyAuthService))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT CANT TAKE TWO OF TOKEN MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), gameManagerMock, dummyAuthService))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
            new ImmutablePair<>(validGame, validPlayer)));
    when(validGame.allowedTakeTwoOf(Gem.RUBY)).thenReturn(false);

    // VALID REQUEST + CAN TAKE TWO OF TOKEN MOCK
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
    Game validGame = new GameDummies().validGames.get(0);

    Player validPlayer = PlayerDummies.validDummies[0];

    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), gameManagerMock, dummyAuthService))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT CANT TAKE THREE OF TOKEN MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), gameManagerMock, dummyAuthService))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
            new ImmutablePair<>(validGame, validPlayer)));
    when(validGame.allowedTakeThreeOf(Gem.RUBY, Gem.DIAMOND, Gem.ONYX)).thenReturn(false);

    // VALID REQUEST + CAN TAKE THREE OF TOKEN MOCK
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
