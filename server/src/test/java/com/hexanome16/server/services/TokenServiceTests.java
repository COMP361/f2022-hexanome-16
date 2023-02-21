package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.Arrays;
import java.util.Set;
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
    //
    //    when(tokensService.validRequest(DummyAuths.invalidSessionIds.get(0),
    //        DummyAuths.invalidTokensInfos.get(0).getAccessToken())).thenReturn(
    //            new ImmutablePair<>(
    //                new ResponseEntity<>()CustomHttpResponses.INVALID_SESSION_ID),
    //                new ImmutablePair<>(null, null)));

    var response = tokensService.takeTwoTokens(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), "RED");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }


  /**
   * Testing takeThree.
   */
  @Test
  //public void testTakeThree() {
  public void takeThreeTokens_shouldRemoveTokensFromGameBank() {
    // Arrange
    long sessionId = DummyAuths.validSessionIds.get(0);
    String authTokenPlayer1 = DummyAuths.validTokensInfos.get(0).getAccessToken();
    Game game = gameManagerMock.getGame(sessionId);

    GameBank testGameBank = new GameBank();
    when(game.getGameBank()).thenReturn(testGameBank);

    // Act

    // Assert

    tokensService.takeThreeTokens(sessionId, authTokenPlayer1, "RED", "GREEN", "WHITE");

    testGameBank.removeGemsFromBank(new PurchaseMap(1, 1, 0, 1, 0, 0));

    assertEquals(game.getGameBank(), testGameBank);

    String authTokenPlayer2 = DummyAuths.validTokensInfos.get(1).getAccessToken();

    assertEquals(tokensService.takeThreeTokens(sessionId, authTokenPlayer2,
            "RED", "RED", "WHITE").getStatusCode(),
        HttpStatus.BAD_REQUEST);
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
    when(gameService.findPlayerByToken(any(),
        eq(DummyAuths.validTokensInfos.get(1).getAccessToken()))).thenReturn(
        PlayerDummies.validDummies[1]);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))
        .isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(
        false);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))
        .isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(
        true);

    // bad sessionId
    var response = tokensService.validRequest(
        DummyAuths.invalidSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode());

    // good sessionId and valid token + is their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());

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
  }

}
