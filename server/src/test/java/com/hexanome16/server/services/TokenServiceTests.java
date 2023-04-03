package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.token.TokenService;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.ServiceUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test of {@link TokenService}.
 */

public class TokenServiceTests {

  private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
      ObjectMapperUtils.getObjectMapper();
  private final SessionJson payload = new SessionJson();
  private DummyAuthService dummyAuthService;
  private GameManagerServiceInterface gameManagerMock;
  private TokenService tokensService;
  private final ServiceUtils serviceUtils = Mockito.mock(ServiceUtils.class);

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    dummyAuthService = new DummyAuthService();
    gameManagerMock =
        DummyGameManagerService.getDummyGameManagerService();
    tokensService = new TokenService(dummyAuthService, gameManagerMock, serviceUtils);

    payload.setPlayers(DummyAuths.validPlayerList.toArray(ServerPlayer[]::new));
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setGame(WinCondition.BASE.getGameServiceJson().getName());
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
    Game validGame = GameDummies.getInstance().get(0);
    ServerPlayer validPlayer = PlayerDummies.validDummies[0];
    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken()))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT CANT TAKE TWO OF TOKEN MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken()))
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
    if (serviceUtils == null) {
      System.out.println("null1");
    }
    if (serviceUtils.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken())
        == null) {
      System.out.println("null2");
    }
    Game validGame = GameDummies.getInstance().get(0);
    ServerPlayer validPlayer = PlayerDummies.validDummies[0];
    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken()))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT CANT TAKE THREE OF TOKEN MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken()))
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

  /**
   * Testing takeOne.
   */
  @Test
  public void testTakeOne() {
    Game validGame = GameDummies.getInstance().get(0);
    ServerPlayer validPlayer = PlayerDummies.validDummies[0];
    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken()))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT CANT TAKE ONE OF TOKEN MOCK
    when(serviceUtils.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken()))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
            new ImmutablePair<>(validGame, validPlayer)));
    when(validGame.allowedTakeOneOf(Gem.ONYX)).thenReturn(false);

    // VALID REQUEST + CAN TAKE ONE OF TOKEN MOCK
    when(validGame.allowedTakeOneOf(Gem.RUBY)).thenReturn(true);

    // INVALID REQUEST
    var response = tokensService.takeOneToken(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), "BLACK");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST BUT CANT TAKE ONE OF TOKENS
    response = tokensService.takeOneToken(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "BLACK");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    // VALID REQUEST + CAN TAKE ONE OF TOKENS
    response = tokensService.takeOneToken(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), "RED");
    assertTrue(response.getStatusCode().is2xxSuccessful());

  }

  /**
   * Testing discard token.
   */
  @Test
  public void testDiscardToken() {
    final Game validGame = GameDummies.getInstance().get(0);
    final ServerPlayer validPlayer1 = Mockito.mock(ServerPlayer.class);
    final ServerPlayer validPlayer2 = Mockito.mock(ServerPlayer.class);
    final long badSessionId = DummyAuths.invalidSessionIds.get(0);
    final long goodSessionId = DummyAuths.validSessionIds.get(0);
    final String badTokenInfo = DummyAuths.invalidTokensInfos.get(0).getAccessToken();
    final String goodTokenInfo1 = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final String goodTokenInfo2 = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final Action returnedAction = Mockito.mock(Action.class);
    final Action badReturnedAction = Mockito.mock(Action.class);

    // INVALID REQUEST MOCK
    when(serviceUtils.validRequestAndCurrentTurn(badSessionId, badTokenInfo))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),
            new ImmutablePair<>(null, null)));

    // VALID REQUEST BUT NOT VALID PLAYER DOESNT HAVE DISCARD AS TOP ACTION
    when(serviceUtils.validRequestAndCurrentTurn(goodSessionId, goodTokenInfo1))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
            new ImmutablePair<>(validGame, validPlayer1)));
    when(validPlayer1.peekTopAction()).thenReturn(null);

    // VALID REQUEST + GOOD ACTION QUEUE
    when(serviceUtils.validRequestAndCurrentTurn(goodSessionId, goodTokenInfo2))
        .thenReturn(new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
            new ImmutablePair<>(validGame, validPlayer2)));
    when(validPlayer2.peekTopAction()).thenReturn(returnedAction, null);
    when(returnedAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.DISCARD);

    var response = tokensService.discardToken(badSessionId, badTokenInfo, "RED");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    response = tokensService.discardToken(goodSessionId, goodTokenInfo1, "RED");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    when(validPlayer1.peekTopAction()).thenReturn(badReturnedAction);
    when(badReturnedAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.END_TURN);

    response = tokensService.discardToken(goodSessionId, goodTokenInfo1, "RED");
    assertFalse(response.getStatusCode().is2xxSuccessful());

    response = tokensService.discardToken(goodSessionId, goodTokenInfo2, "RED");
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

}
