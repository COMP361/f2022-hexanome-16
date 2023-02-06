package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.InventoryAddable;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.PriceMap;
import com.hexanome16.server.models.PurchaseMap;
import com.hexanome16.server.models.TokenPrice;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * The type Game service tests.
 */
class GameServiceTests {
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private final SessionJson payload = new SessionJson();
  private GameService gameService;
  private String gameResponse;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws JsonProcessingException {
    gameService = new GameService(new DummyAuthService());
    payload.setPlayers(
        new Player[] {objectMapper.readValue(DummyAuths.validJsonList.get(0), Player.class),
            objectMapper.readValue(DummyAuths.validJsonList.get(1), Player.class)}
    );
    payload.setCreator("tristan");
    payload.setSavegame("");
    payload.setWinCondition(new BaseWinCondition());
    gameResponse = gameService.createGame(DummyAuths.validSessionIds.get(0), payload);
    gameResponse = gameService.createGame(DummyAuths.validSessionIds.get(1), payload);
  }

  /**
   * Should have non-null map on creation.
   */
  @Test
  void shouldHaveNonNullMapOnCreation() {
    assertNotNull(gameService.getGameMap());
  }

  /**
   * Should get success on game creation.
   */
  @Test
  void shouldGetSuccessOnGameCreation() {
    System.out.println(gameResponse);
    assertEquals("success", gameResponse);
  }

  /**
   * Test update deck success.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testUpdateDeckSuccess() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        null;
    response = (ResponseEntity<String>) gameService.getDeck(DummyAuths.validSessionIds.get(0),
        "REDTHREE", DummyAuths.validTokensInfos.get(0).getAccessToken(), hash).getResult();
    assertNotNull(response);
  }

  /**
   * Test update deck fail.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testUpdateDeckFail() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameService.getDeck(DummyAuths.validSessionIds.get(0), "ONE",
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
    response = gameService.getDeck(DummyAuths.invalidSessionIds.get(0), "ONE",
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
  }

  /**
   * Test update nobles success.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testUpdateNoblesSuccess() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameService.getNobles(DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(), hash).getResult();
    assertNotNull(response);
  }

  /**
   * Test update nobles fail.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testUpdateNoblesFail() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameService.getNobles(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
    response = gameService.getNobles(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
  }

  /**
   * Test current player success.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testCurrentPlayerSuccess() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameService.getCurrentPlayer(DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(), hash).getResult();
    assertNotNull(response);
  }

  /**
   * Test current player fail.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testCurrentPlayerFail() throws com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameService.getCurrentPlayer(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
    response = gameService.getCurrentPlayer(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    assertNull(response);
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

    assertEquals(myPm.getRubyAmount(), 3);
    assertEquals(myPm.getEmeraldAmount(), 3);
    assertEquals(myPm.getSapphireAmount(), 3);
    assertEquals(myPm.getDiamondAmount(), 3);
    assertEquals(myPm.getOnyxAmount(), 3);
    assertEquals(myPm.getGoldAmount(), 3);
  }

  /**
   * Test get player bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetPlayerBankInfoInvalid()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response;
    //Test invalid session id
    response =
        gameService.getPlayerBankInfo(DummyAuths.invalidSessionIds.get(0),
            DummyAuths.validPlayerList.get(0).getName());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    //Test invalid player name
    response =
        gameService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidPlayerList.get(0).getName());
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

    assertEquals(myPm.getRubyAmount(), 7);
    assertEquals(myPm.getEmeraldAmount(), 7);
    assertEquals(myPm.getSapphireAmount(), 7);
    assertEquals(myPm.getDiamondAmount(), 7);
    assertEquals(myPm.getOnyxAmount(), 7);
    assertEquals(myPm.getGoldAmount(), 5);
  }

  /**
   * Test get game bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testGetGameBankInfoInvalid()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response;

    response =
        gameService.getGameBankInfo(DummyAuths.invalidSessionIds.get(0));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(gameService.getGameMap().get(sessionId));

    try {
      Field field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }
    ResponseEntity<String> response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, 1, 1, 1, 0, 0, 1);


    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test reserve card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testReserveCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    var sessionId = DummyAuths.validSessionIds.get(0);
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(gameService.getGameMap().get(sessionId));

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    ResponseEntity<String> response =
        gameService.reserveCard(sessionId,
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
    var sessionId = DummyAuths.validSessionIds.get(0);
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(gameService.getGameMap().get(sessionId));

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

      Game game = gameService.getGameMap().get(sessionId);
      field = game.getClass().getDeclaredField("onBoardDecks");
      field.setAccessible(true);
      Deck<LevelCard> testDeck = new Deck<>();
      testDeck.addCard(myCard);
      ((Map<Level, Deck<LevelCard>>) field.get(game)).put(Level.ONE, testDeck);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    ResponseEntity<String> response =
        gameService.reserveFaceDownCard(sessionId,
            "ONE", accessToken);

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

    LevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(gameService.getGameMap().get(sessionId));

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    // Test invalid sessionId
    ResponseEntity<String> response = gameService.buyCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken, 1, 1, 1, 0, 0, 1);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // Test invalid accessToken
    response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            invalidAccessToken, 1, 1, 1, 0, 0, 1);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // Test invalid price
    response =
        gameService.buyCard(sessionId, DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, 3, 1, 1, 0, 0, 1);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Test not enough funds
    LevelCard invalidCard = createInvalidCard();

    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(invalidCard)), invalidCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }
    response = gameService.buyCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(invalidCard)), accessToken, 7, 1, 1, 0,
        0, 1);
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

    LevelCard myCard = createValidCard();

    gameService.endCurrentPlayersTurn(gameService.getGameMap().get(sessionId));

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, InventoryAddable>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    // Test invalid sessionId
    ResponseEntity<String> response = gameService.reserveCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    // Test invalid accessToken
    response =
        gameService.reserveCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), invalidAccessToken);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  private LevelCard createValidCard() {
    return new LevelCard(20, 0, "", new TokenPrice(new PriceMap(1, 1, 1, 1, 0)), Level.ONE);
  }

  private LevelCard createInvalidCard() {
    return new LevelCard(20, 0, "", new TokenPrice(new PriceMap(7, 1, 1, 1, 0)), Level.ONE);
  }

  /**
   * Test find player by name.
   */
  @Test
  void testFindPlayerByName() {
    var game = gameService.getGameMap().get(DummyAuths.validSessionIds.get(0));
    var player = gameService.findPlayerByName(game, DummyAuths.validPlayerList.get(0).getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by name invalid.
   */
  @Test
  void testFindPlayerByNameInvalid() {
    var game = gameService.getGameMap().get(DummyAuths.validSessionIds.get(0));

    //Test invalid player
    var player = gameService.findPlayerByName(game,
        DummyAuths.invalidPlayerList.get(0).getName());
    assertNull(player);
  }

  /**
   * Test find player by token.
   */
  @Test
  void testFindPlayerByToken() {
    var game = gameService.getGameMap().get(DummyAuths.validSessionIds.get(0));
    var player =
        gameService.findPlayerByToken(game, DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertEquals(DummyAuths.validPlayerList.get(0).getName(), player.getName());
    assertEquals(DummyAuths.validPlayerList.get(0).getPreferredColour(),
        player.getPreferredColour());
  }

  /**
   * Test find player by token invalid.
   */
  @Test
  void testFindPlayerByTokenInvalid() {
    var game = gameService.getGameMap().get(DummyAuths.validSessionIds.get(0));

    //Test invalid player
    var player =
        gameService.findPlayerByToken(game, DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertNull(player);
  }
}
