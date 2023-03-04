package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.hexanome16.common.util.CustomHttpResponses;

/**
 * The type Game service tests.
 */
class GameServiceTests {
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private Game validMockGame;
  private GameService gameService;
  private GameManagerServiceInterface gameManagerMock;

  /**
   * Sets up the tests.
   */
  @BeforeEach
  void setup() {

    validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new WinCondition[] {WinCondition.BASE});

    gameManagerMock = Mockito.mock(GameManagerService.class);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    gameService = new GameService(new DummyAuthService(), gameManagerMock);

  }


  /**
   * Test get player bank info.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetPlayerBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response =
        gameService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
            DummyAuths.validPlayerList.get(0).getName());
    String string = response.getBody();
    PurchaseMap myPm = objectMapper.readValue(string, new TypeReference<>() {
    });

    assertEquals(myPm.getGemCost(Gem.RUBY), 3);
    assertEquals(myPm.getGemCost(Gem.EMERALD), 3);
    assertEquals(myPm.getGemCost(Gem.SAPPHIRE), 3);
    assertEquals(myPm.getGemCost(Gem.DIAMOND), 3);
    assertEquals(myPm.getGemCost(Gem.ONYX), 3);
    assertEquals(myPm.getGemCost(Gem.GOLD), 3);
  }

  /**
   * Test get player bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetPlayerBankInfoInvalidSessionId()
      throws com.fasterxml.jackson.core.JsonProcessingException {

    // Arrange

    // Act
    ResponseEntity<String> response =
        gameService.getPlayerBankInfo(DummyAuths.invalidSessionIds.get(0),
            DummyAuths.validPlayerList.get(0).getName());
    // Assert
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
  }

  /**
   * Test get player bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetPlayerBankInfoInvalidPlayerName()
      throws com.fasterxml.jackson.core.JsonProcessingException {

    // Arrange

    // Act
    ResponseEntity<String> response =
        gameService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidPlayerList.get(0).getName());

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Test get game bank info.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetGameBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response =
        gameService.getGameBankInfo(DummyAuths.validSessionIds.get(0));
    String string = response.getBody();
    PurchaseMap myPm = objectMapper.readValue(string, new TypeReference<>() {
    });

    assertEquals(myPm.getGemCost(Gem.RUBY), 7);
    assertEquals(myPm.getGemCost(Gem.EMERALD), 7);
    assertEquals(myPm.getGemCost(Gem.SAPPHIRE), 7);
    assertEquals(myPm.getGemCost(Gem.DIAMOND), 7);
    assertEquals(myPm.getGemCost(Gem.ONYX), 7);
    assertEquals(myPm.getGemCost(Gem.GOLD), 5);
  }

  /**
   * Test get game bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetGameBankInfoInvalidSessionId()
      throws com.fasterxml.jackson.core.JsonProcessingException {

    // Arrange
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    // Act
    ResponseEntity<String> response;
    response = gameService.getGameBankInfo(DummyAuths.invalidSessionIds.get(0));

    // Assert

    assertTrue(response.getStatusCode().is4xxClientError());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), response.getBody());
  }

  /**
   * End current players turn.
   */
  @Test
  void endCurrentPlayersTurn() {
  }

  /**
   * Test buy card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testBuyCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    Game gameMock = gameManagerMock.getGame(sessionId);
    gameService.endCurrentPlayersTurn(gameMock);

    gameMock.getLevelDeck(myCard.getLevel()).addCard(myCard);
    gameMock.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

    ResponseEntity<String> response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test reserve card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testReserveCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    gameService.endCurrentPlayersTurn(validMockGame);
    ServerLevelCard myCard = createValidCard();
    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

    ResponseEntity<String> response = gameService.reserveCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test reserve face down card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testReserveFaceDownCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    gameService.endCurrentPlayersTurn(validMockGame);
    ServerLevelCard myCard = createValidCard();
    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

    ResponseEntity<String> response =
        gameService.reserveFaceDownCard(sessionId, Level.ONE.name(), accessToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = gameService.reserveFaceDownCard(sessionId, "WRONG LEVEL NAME", accessToken);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }

  /**
   * Test buy card invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testBuyCardInvalidCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(validMockGame);

    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

    // Test invalid sessionId
    ResponseEntity<String> response = gameService.buyCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken,
        new PurchaseMap(1, 1, 1, 0, 0, 1));
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());

    // Test invalid accessToken
    response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            invalidAccessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getStatusCode().value());
    // Test invalid price
    response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, new PurchaseMap(3, 0, 0, 0, 0, 1));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Test not enough funds
    ServerLevelCard invalidCard = createInvalidCard();
    validMockGame.getLevelDeck(invalidCard.getLevel()).addCard(invalidCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(invalidCard)), invalidCard
    );

    response = gameService.buyCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(invalidCard)), accessToken,
        new PurchaseMap(7, 1, 1, 0, 0, 1));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Test reserve card invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testReserveCardInvalidCard()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(validMockGame);

    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

    // Test invalid sessionId
    ResponseEntity<String> response = gameService.reserveCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
    // Test invalid accessToken
    response = gameService.reserveCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), invalidAccessToken);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getStatusCode().value());
  }

  private ServerLevelCard createValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private ServerLevelCard createInvalidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }

  /**
   * Test find player by name.
   */
  @Test
  void testFindPlayerByName() {
    var player =
        gameService.findPlayerByName(validMockGame, DummyAuths.validPlayerList.get(0).getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by name invalid.
   */
  @Test
  void testFindPlayerByNameInvalid() {
    //Test invalid player
    var player =
        gameService.findPlayerByName(validMockGame, DummyAuths.invalidPlayerList.get(0).getName());
    assertNull(player);
  }

  /**
   * Test find player by token.
   */
  @Test
  void testFindPlayerByToken() {
    var player = gameService.findPlayerByToken(validMockGame,
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by token invalid.
   */
  @Test
  void testFindPlayerByTokenInvalid() {
    //Test invalid player
    var player = gameService.findPlayerByToken(validMockGame,
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertNull(player);
  }

  /**
   * Testing validRequestAndCurrentTurn(sessionId, authToken).
   */
  @Test
  public void testValidRequestAndCurrentTurn() {
    // Arrange
    Game gameMock = Mockito.mock(Game.class);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(false);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(true);

    // bad sessionId
    var response = gameService.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId but bad player
    response = gameService.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId and valid token but isn't their turn
    response = gameService.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getLeft().getStatusCode().value());

    // bad sessionId and token
    response = gameService.validRequestAndCurrentTurn(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId and valid token + is their turn
    response = gameService.validRequestAndCurrentTurn(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }

  /**
   * Testing validRequest(sessionId, authToken).
   */
  @Test
  public void testValidRequest() {
    // Arrange
    Game gameMock = Mockito.mock(Game.class);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[0])).thenReturn(false);
    when(gameMock.isNotPlayersTurn(PlayerDummies.validDummies[1])).thenReturn(true);

    // bad sessionId
    var response = gameService.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId but bad player
    response = gameService.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getLeft().getStatusCode().value());

    // bad sessionId and token
    response = gameService.validRequest(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(1).getAccessToken());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getLeft().getStatusCode().value());

    // good sessionId and valid token
    response = gameService.validRequest(DummyAuths.validSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(HttpStatus.OK, response.getLeft().getStatusCode());
  }
}
