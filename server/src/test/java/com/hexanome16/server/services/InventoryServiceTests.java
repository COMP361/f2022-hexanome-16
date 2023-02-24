package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.InventoryAddable;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.price.PriceMap;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.models.winconditions.BaseWinCondition;
import com.hexanome16.server.util.BroadcastMap;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.ServiceUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The type Game service tests.
 */
class InventoryServiceTests {
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private Game validMockGame;
  private InventoryService inventoryService;
  private ServiceUtils serviceUtils;
  private GameManagerServiceInterface gameManagerMock;

  /**
   * Sets .
   *
   * @throws JsonProcessingException the json processing exception
   */
  @BeforeEach
  void setup() throws IOException {

    validMockGame =
        new Game(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new BaseWinCondition());

    BroadcastMap broadcastMap = new BroadcastMap();
    BroadcastContentManager<NoblesHash> noblesHashBroadcastContentManager =
        new BroadcastContentManager<>(new NoblesHash(validMockGame));
    broadcastMap.put("noble", noblesHashBroadcastContentManager);

    gameManagerMock = Mockito.mock(GameManagerService.class);
    serviceUtils = new ServiceUtils();
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    inventoryService = new InventoryService(new DummyAuthService(), gameManagerMock, serviceUtils);

  }

  /**
   * Test get player bank info.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Get valid Player Bank info")
  public void testGetPlayerBankInfo() throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<String> response =
        inventoryService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
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
  @DisplayName("Get Player Bank info with invalid session ID")
  public void testGetPlayerBankInfoInvalidSessionId()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    // Arrange

    // Act
    ResponseEntity<String> response =
        inventoryService.getPlayerBankInfo(DummyAuths.invalidSessionIds.get(0),
            DummyAuths.validPlayerList.get(0).getName());
    // Assert
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode());
  }

  /**
   * Test get player bank info invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Get Player Bank info with invalid player name")
  public void testGetPlayerBankInfoInvalidPlayerName()
      throws com.fasterxml.jackson.core.JsonProcessingException {

    // Arrange

    // Act
    ResponseEntity<String> response =
        inventoryService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidPlayerList.get(0).getName());

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Test buy card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Buy a Level Card successfully")
  public void testBuyCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    var sessionId = DummyAuths.validSessionIds.get(0);
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    this.serviceUtils.endCurrentPlayersTurn(gameManagerMock.getGame(sessionId));

    try {
      Field field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, 1, 1, 1, 0, 0, 1);


    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test reserve card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Reserve a face up Level Card successfully")
  public void testReserveCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    var sessionId = DummyAuths.validSessionIds.get(0);
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    this.serviceUtils.endCurrentPlayersTurn(validMockGame);

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    ResponseEntity<String> response = inventoryService.reserveCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);


    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test reserve face down card.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Reserve a face down Level Card successfully")
  public void testReserveFaceDownCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    var sessionId = DummyAuths.validSessionIds.get(0);
    var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    this.serviceUtils.endCurrentPlayersTurn(validMockGame);

    Field field;
    try {
      field = DeckHash.class.getDeclaredField("allCards");
      field.setAccessible(true);


      ((HashMap<String, LevelCard>) field.get(null)).put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

      Game game = validMockGame;
      field = game.getClass().getDeclaredField("onBoardDecks");
      field.setAccessible(true);
      Deck<LevelCard> testDeck = new Deck<>();
      testDeck.addCard(myCard);
      ((Map<Level, Deck<LevelCard>>) field.get(game)).put(Level.ONE, testDeck);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail();
    }

    ResponseEntity<String> response =
        inventoryService.reserveFaceDownCard(sessionId, "ONE", accessToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = inventoryService.reserveFaceDownCard(sessionId, "WRONG LEVEL NAME", accessToken);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }

  /**
   * Test buy card invalid.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  @DisplayName("Buy a Level Card unsuccessfully")
  public void testBuyCardInvalidCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    this.serviceUtils.endCurrentPlayersTurn(validMockGame);

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
    ResponseEntity<String> response = inventoryService.buyCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken, 1, 1, 1, 0, 0, 1);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode());
    // Test invalid accessToken
    response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            invalidAccessToken, 1, 1, 1, 0, 0, 1);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), response.getStatusCode());
    // Test invalid price
    response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, 3, 0, 0, 0, 0, 1);
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
    response = inventoryService.buyCard(sessionId,
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
  @DisplayName("Reserve a face up Level Card unsuccessfully")
  public void testReserveCardInvalidCard()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    LevelCard myCard = createValidCard();

    this.serviceUtils.endCurrentPlayersTurn(validMockGame);

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
    ResponseEntity<String> response = inventoryService.reserveCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode());
    // Test invalid accessToken
    response = inventoryService.reserveCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), invalidAccessToken);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), response.getStatusCode());
  }

  private LevelCard createValidCard() {
    return new LevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private LevelCard createInvalidCard() {
    return new LevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }
}
