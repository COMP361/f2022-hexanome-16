package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.GameBank;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    gameService = new GameService(dummyAuthService, gameManagerMock);
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
    ResponseEntity<String> response =
        tokensService.availableTwoTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    Set<String> available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(response.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "GREEN", "BLUE", "WHITE", "BLACK", "NULL"));
    Game game = gameManagerMock.getGame(DummyAuths.validSessionIds.get(0));
    game.incGameBank(-3, -4, 0, 0, 0, 0);

    response =
        tokensService.availableTwoTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(response.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "BLUE", "WHITE", "BLACK", "NULL"));
  }

  /**
   * Testing availableThreeTokensType().
   *
   * @throws JsonProcessingException possible json processing error
   */
  @Test
  public void testAvailableThreeTokensType() throws JsonProcessingException {
    ResponseEntity<String> response =
        tokensService.availableThreeTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    Set<String> available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(response.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "GREEN", "BLUE", "WHITE", "BLACK", "NULL"));
    Game game = gameManagerMock.getGame(DummyAuths.validSessionIds.get(0));
    game.incGameBank(-3, -4, 0, -7, 0, 0);

    response =
        tokensService.availableThreeTokensType(DummyAuths.validSessionIds.get(0));
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    available =
        Set.copyOf(Arrays.asList(objectMapper.readValue(response.getBody(), String[].class)));
    assertEquals(available,
        Set.of("RED", "BLUE", "GREEN", "BLACK", "NULL"));
  }

  /**
   * Testing takeTwo.
   */
  @Test
  public void testTakeTwo() {
    long sessionId = DummyAuths.validSessionIds.get(0);
    String authTokenPlayer1 = DummyAuths.validTokensInfos.get(0).getAccessToken();

    Game game = gameManagerMock.getGame(sessionId);

    GameBank testGameBank = new GameBank();

    assertEquals(game.getGameBank(), testGameBank);
    tokensService.takeTwoTokens(sessionId, authTokenPlayer1, "RED");
    testGameBank.incBank(-2, 0, 0, 0, 0, 0);
    assertEquals(game.getGameBank(), testGameBank);
    game.incGameBank(-10, -10, -10, -10, -10, -10);

    String authTokenPlayer2 = DummyAuths.validTokensInfos.get(1).getAccessToken();
    assertEquals(tokensService.takeTwoTokens(sessionId, authTokenPlayer2, "RED").getStatusCode(),
        HttpStatus.BAD_REQUEST);
  }


  /**
   * Testing takeThree.
   */
  @Test
  public void testTakeThree() {
    long sessionId = DummyAuths.validSessionIds.get(0);
    String authTokenPlayer1 = DummyAuths.validTokensInfos.get(0).getAccessToken();

    Game game = gameManagerMock.getGame(sessionId);

    GameBank testGameBank = new GameBank();

    //TODO: why are you testing this?
    assertEquals(game.getGameBank(), testGameBank);
    tokensService.takeThreeTokens(sessionId, authTokenPlayer1, "RED", "GREEN", "WHITE");

    testGameBank.incBank(-1, -1, 0, -1, 0, 0);

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
    // bad sessionId
    ResponseEntity<String> response = tokensService.validRequest(
        DummyAuths.invalidSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    // good sessionId and valid token + is their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    assertEquals(response.getStatusCode(), HttpStatus.OK);

    // good sessionId and valid token but isnt their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(1).getAccessToken()
    );
    assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    Game game = gameManagerMock.getGame(DummyAuths.validSessionIds.get(0));
    gameService.endCurrentPlayersTurn(game);

    // good sessionId and valid token + is their turn
    response = tokensService.validRequest(
        DummyAuths.validSessionIds.get(0), DummyAuths.validTokensInfos.get(1).getAccessToken()
    );
    assertEquals(response.getStatusCode(), HttpStatus.OK);

    // bad sessionId and token
    response = tokensService.validRequest(
        DummyAuths.invalidSessionIds.get(0), DummyAuths.invalidTokensInfos.get(1).getAccessToken()
    );
    assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

}
