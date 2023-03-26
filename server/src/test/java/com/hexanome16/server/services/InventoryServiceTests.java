package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * The type Game service tests.
 */
public class InventoryServiceTests {
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private Game validMockGame;
  private InventoryService inventoryService;
  private ServiceUtils serviceUtils;
  private GameManagerServiceInterface gameManagerMock;

  /**
   * Setup.
   */
  @BeforeEach
  void setup() {

    validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            WinCondition.BASE);

    gameManagerMock = DummyGameManagerService.getDummyGameManagerService();
    serviceUtils = DummyServiceUtils.getDummyServiceUtils();

    inventoryService = new InventoryService(gameManagerMock, serviceUtils);

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
   * Test get invalid player bank info.
   */
  @Test
  @SneakyThrows
  @DisplayName("Get Player Bank info with invalid username should return http error")
  public void testGetInvalidPlayerBankInfo() {
    ResponseEntity<String> response =
        inventoryService.getPlayerBankInfo(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidPlayerList.get(0).getName());

    assertEquals(CustomHttpResponses.PLAYER_NOT_IN_GAME.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.PLAYER_NOT_IN_GAME.getBody(), response.getBody());
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
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
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
  public void testBuyCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Action mockAction = mock(Action.class);
    when(playerMock.peekTopAction()).thenReturn(mockAction);
    when(playerMock.hasAtLeast(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
        anyInt())).thenReturn(true);
    when(mockAction.getActionDetails()).thenReturn(
        CustomResponseFactory.getResponse(CustomHttpResponses.OK));
    when(serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken)).thenReturn(
        Pair.of(CustomResponseFactory.getResponse(CustomHttpResponses.OK),
            Pair.of(gameMock, playerMock)));

    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);
    Deck<ServerLevelCard> mockDeck = Mockito.mock(Deck.class);
    when(mockDeck.getCardList()).thenReturn(new LinkedList<>());
    when(gameMock.getOnBoardDeck(any())).thenReturn(mockDeck);
    var mapMock = Mockito.mock(BroadcastMap.class);
    when(gameMock.getBroadcastContentManagerMap()).thenReturn(mapMock);


    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId, cardHash,
            accessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.OK.getBody(), response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  /**
   * Test buy card adds take level two action.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testBuyCardAddsTakeLevelTwoAction()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidTakeTwoCard();
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Action mockAction = mock(Action.class);
    when(playerMock.peekTopAction()).thenReturn(mockAction);
    when(playerMock.hasAtLeast(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
        anyInt())).thenReturn(true);
    when(mockAction.getActionDetails()).thenReturn(
        CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO));
    when(serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken)).thenReturn(
        Pair.of(CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO),
            Pair.of(gameMock, playerMock)));

    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);
    Deck<ServerLevelCard> mockDeck = Mockito.mock(Deck.class);
    when(mockDeck.getCardList()).thenReturn(new LinkedList<>());
    when(gameMock.getOnBoardDeck(any())).thenReturn(mockDeck);
    var mapMock = Mockito.mock(BroadcastMap.class);
    when(gameMock.getBroadcastContentManagerMap()).thenReturn(mapMock);


    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId, cardHash,
            accessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.TAKE_LEVEL_TWO.getBody(), response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(playerMock, times(1)).addTakeTwoToPerform();
    verify(playerMock, times(0)).addNobleListToPerform(any());
  }

  /**
   * Test buy card adds correct action in right order.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testBuyCardAddsCorrectActionsInRightOrder()
      throws com.fasterxml.jackson.core.JsonProcessingException {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidTakeTwoCard();
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = mock(ServerPlayer.class);
    Action mockAction = mock(Action.class);
    when(playerMock.peekTopAction()).thenReturn(mockAction);
    when(playerMock.hasAtLeast(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
        anyInt())).thenReturn(true);
    when(mockAction.getActionDetails()).thenReturn(
        CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO));
    when(serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken)).thenReturn(
        Pair.of(CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO),
            Pair.of(gameMock, playerMock)));

    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);
    Deck<ServerLevelCard> mockDeck = Mockito.mock(Deck.class);
    when(mockDeck.getCardList()).thenReturn(new LinkedList<>());
    when(gameMock.getOnBoardDeck(any())).thenReturn(mockDeck);
    ServerNoble mockNoble = mock(ServerNoble.class);
    when(gameMock.getRemainingNobles()).thenReturn(Map.of("noble1", mockNoble));
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    var mapMock = Mockito.mock(BroadcastMap.class);
    when(gameMock.getBroadcastContentManagerMap()).thenReturn(mapMock);


    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId, cardHash,
            accessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.TAKE_LEVEL_TWO.getBody(), response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(playerMock, times(1)).addTakeTwoToPerform();
    verify(playerMock, times(1)).addNobleListToPerform(any());
  }

  /**
   * Test buy invalid card using invalid hash.
   */
  @Test
  @SneakyThrows
  @DisplayName("BuyCard should fail if given invalid hash")
  public void testBuyInvalidCardInvalidHash() {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    // Card is created but not added to deck hash
    ServerLevelCard myCard = createValidCard();

    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getBody(), response.getBody());
  }

  /**
   * Test buy invalid card using invalid proposed deal.
   */
  @Test
  @SneakyThrows
  @DisplayName("BuyCard should fail if given invalid proposed deal")
  public void testBuyInvalidCardInvalidDeal() {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    Game gameMock =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken).getRight().getLeft();
    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);

    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId, cardHash,
            accessToken, new PurchaseMap(0, 0, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.INVALID_PROPOSED_DEAL.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INVALID_PROPOSED_DEAL.getBody(), response.getBody());
  }

  @Test
  @SneakyThrows
  @DisplayName("BuyCard should fail if player does not have funds for given deal")
  public void testBuyInvalidCardInsufficientFunds() {
    // Arrange
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createTooExpensiveValidCard();
    Game gameMock =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken).getRight().getLeft();
    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);

    // Act
    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId, cardHash,
            accessToken, new PurchaseMap(20, 1, 1, 0, 0, 1));

    // Assert
    assertEquals(CustomHttpResponses.INSUFFICIENT_FUNDS.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INSUFFICIENT_FUNDS.getBody(), response.getBody());
  }

  /**
   * Test reserve card.
   */
  @Test
  @SneakyThrows
  public void testReserveCard() {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    var requestResponse =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);

    Game gameMock =
        requestResponse.getRight().getLeft();
    String cardHash = "";

    when(gameMock.getCardByHash(cardHash)).thenReturn(myCard);

    ResponseEntity<String> response =
        inventoryService.reserveCard(sessionId, cardHash, accessToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @SneakyThrows
  @DisplayName("Reserve invalid card hash should return error http")
  public void testReserveInvalidCard() {
    // Arrange
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getHashToCardMap().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

    // Act
    ResponseEntity<String> response = inventoryService.reserveCard(sessionId,
        "invalid hash", accessToken);

    // Assert
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getBody(), response.getBody());
  }

  /*
   * Test reserve face down card.
   */
  @Test
  public void testReserveFaceDownCard() {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    Game gameMock =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken).getRight().getLeft();
    Deck<ServerLevelCard> deckMock = Mockito.mock(Deck.class);
    when(deckMock.removeNextCard()).thenReturn(myCard);
    when(gameMock.getLevelDeck(any())).thenReturn(deckMock);


    ResponseEntity<String> response =
        inventoryService.reserveFaceDownCard(sessionId, Level.ONE.name(), accessToken);

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
  public void testBuyCardInvalidCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();


    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getHashToCardMap().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

    // Test invalid sessionId
    ResponseEntity<String> response = inventoryService.buyCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken,
        new PurchaseMap(1, 1, 1, 0, 0, 1));
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());

    // Test invalid accessToken
    response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            invalidAccessToken, new PurchaseMap(1, 1, 1, 0, 0, 1));
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getStatusCode().value());
    // Test invalid price
    response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
            accessToken, new PurchaseMap(3, 0, 0, 0, 0, 1));
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Test not enough funds
    ServerLevelCard invalidCard = createInvalidCard();
    validMockGame.getLevelDeck(invalidCard.getLevel()).addCard(invalidCard);
    validMockGame.getHashToCardMap().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(invalidCard)), invalidCard
    );

    response = inventoryService.buyCard(sessionId,
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
    final var accessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(0).getAccessToken();

    ServerLevelCard myCard = createValidCard();


    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getHashToCardMap().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard);

    // Test invalid sessionId
    ResponseEntity<String> response = inventoryService.reserveCard(invalidSessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), accessToken);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        response.getStatusCode().value());
    // Test invalid accessToken
    response = inventoryService.reserveCard(sessionId,
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), invalidAccessToken);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getStatusCode().value());
  }

  /**
   * testing takeLevelTwoCard().
   *
   * @throws JsonProcessingException if Json parsing fails.
   */
  @Test
  void testTakeLevelTwoCard() throws JsonProcessingException {

    ServerLevelCard card = new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.TWO,
        new PurchaseMap(Map.of(Gem.RUBY, 1)));;
    MultiValueMap<String, String> goodHeaders = new HttpHeaders();
    goodHeaders.put(CustomHttpResponses.ActionType.ACTION_TYPE,
        List.of(CustomHttpResponses.ActionType.LEVEL_TWO.getMessage()));
    ResponseEntity<String> goodResponse = new ResponseEntity<>(
        goodHeaders, HttpStatus.OK);
    ServerPlayer validPlayer = Mockito.mock(ServerPlayer.class);
    Game game = Mockito.mock(Game.class);
    String cardHash = DigestUtils.md5Hex(objectMapper.writeValueAsString(card));

    when(serviceUtils.validRequestAndCurrentTurn(123, "testingToken"))
        .thenReturn(
            new ImmutablePair<>(goodResponse,
                new ImmutablePair<>(game, validPlayer)));
    Action mockAction = mock(Action.class);
    when(mockAction.getActionDetails()).thenReturn(goodResponse);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.LEVEL_TWO);
    when(validPlayer.peekTopAction())
        .thenReturn(mockAction);
    when(game.getCardByHash(cardHash)).thenReturn(card);
    when(game.getOnBoardDeck(Level.TWO))
        .thenReturn(new Deck<>());
    when(game.getOnBoardDeck(Level.REDTWO)).thenReturn(new Deck<>(new ServerLevelCard[]{card}));
    var mapMock = Mockito.mock(BroadcastMap.class);
    when(game.getBroadcastContentManagerMap()).thenReturn(mapMock);

    ResponseEntity<String> response = inventoryService
        .takeLevelTwoCard(123, "testingToken",
            cardHash);
    assertEquals(goodResponse, response);

    ResponseEntity<String> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    when(serviceUtils.validRequestAndCurrentTurn(1234, "bad"))
        .thenReturn(new ImmutablePair<>(
            badRequest,
            new ImmutablePair<>(null, null)));

    response = inventoryService.takeLevelTwoCard(1234,
        "bad", cardHash);
    assertEquals(badRequest, response);

    when(game.getCardByHash(cardHash)).thenReturn(null);
    response = inventoryService
        .takeLevelTwoCard(123, "testingToken",
            cardHash);
    assertFalse(response.getStatusCode().is2xxSuccessful());


    MultiValueMap<String, String> badHeaders = new HttpHeaders();
    badHeaders.put(CustomHttpResponses.ActionType.ACTION_TYPE,
        List.of(CustomHttpResponses.ActionType.END_TURN.getMessage()));
    mockAction = mock(Action.class);
    when(mockAction.getActionDetails()).thenReturn(
        new ResponseEntity<>(badHeaders, HttpStatus.BAD_REQUEST));
    when(validPlayer.peekTopAction())
        .thenReturn(mockAction);
    when(game.getCardByHash(cardHash)).thenReturn(card);
    response = inventoryService
        .takeLevelTwoCard(123, "testingToken",
            cardHash);
    assertFalse(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * testing takeLevelTwoCard().
   *
   * @throws JsonProcessingException if Json parsing fails.
   */
  @Test
  void testTakeLevelOneCard() throws JsonProcessingException {

    ServerLevelCard card = new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE,
        new PurchaseMap(Map.of(Gem.RUBY, 1)));;
    MultiValueMap<String, String> goodHeaders = new HttpHeaders();
    goodHeaders.put(CustomHttpResponses.ActionType.ACTION_TYPE,
        List.of(CustomHttpResponses.ActionType.LEVEL_ONE.getMessage()));
    ResponseEntity<String> goodResponse = new ResponseEntity<>(
        goodHeaders, HttpStatus.OK);
    ServerPlayer validPlayer = Mockito.mock(ServerPlayer.class);
    Game game = Mockito.mock(Game.class);
    String cardHash = DigestUtils.md5Hex(objectMapper.writeValueAsString(card));

    when(serviceUtils.validRequestAndCurrentTurn(123, "testingToken"))
        .thenReturn(
            new ImmutablePair<>(goodResponse,
                new ImmutablePair<>(game, validPlayer)));
    Action mockAction = mock(Action.class);
    when(mockAction.getActionDetails()).thenReturn(goodResponse);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.LEVEL_ONE);
    when(validPlayer.peekTopAction())
        .thenReturn(mockAction);
    when(game.getCardByHash(cardHash)).thenReturn(card);
    when(game.getOnBoardDeck(Level.ONE))
        .thenReturn(new Deck<>());
    when(game.getOnBoardDeck(Level.REDONE)).thenReturn(new Deck<>(new ServerLevelCard[]{card}));
    var mapMock = Mockito.mock(BroadcastMap.class);
    when(game.getBroadcastContentManagerMap()).thenReturn(mapMock);

    ResponseEntity<String> response = inventoryService
        .takeLevelOneCard(123, "testingToken",
            cardHash);
    assertEquals(goodResponse, response);

    ResponseEntity<String> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    when(serviceUtils.validRequestAndCurrentTurn(1234, "bad"))
        .thenReturn(new ImmutablePair<>(
            badRequest,
            new ImmutablePair<>(null, null)));

    response = inventoryService.takeLevelOneCard(1234,
        "bad", cardHash);
    assertEquals(badRequest, response);

    when(game.getCardByHash(cardHash)).thenReturn(null);
    response = inventoryService
        .takeLevelOneCard(123, "testingToken",
            cardHash);
    assertFalse(response.getStatusCode().is2xxSuccessful());


    MultiValueMap<String, String> badHeaders = new HttpHeaders();
    badHeaders.put(CustomHttpResponses.ActionType.ACTION_TYPE,
        List.of(CustomHttpResponses.ActionType.END_TURN.getMessage()));
    mockAction = mock(Action.class);
    when(mockAction.getActionDetails()).thenReturn(
        new ResponseEntity<>(badHeaders, HttpStatus.BAD_REQUEST));
    when(validPlayer.peekTopAction())
        .thenReturn(mockAction);
    when(game.getCardByHash(cardHash)).thenReturn(card);
    response = inventoryService
        .takeLevelOneCard(123, "testingToken",
            cardHash);
    assertFalse(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test noble happy path.
   */
  @Test
  @SneakyThrows
  public void testNobleHappyPath() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    when(playerMock.addCardToInventory(mockNoble)).thenReturn(true);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.NOBLE);
    when(playerMock.peekTopAction()).thenReturn(mockAction).thenReturn(null);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.END_OF_TURN.getBody(), response.getBody());
    assertEquals(CustomHttpResponses.END_OF_TURN.getStatus(), response.getStatusCodeValue());
  }

  /**
   * Test noble next action returns.
   */
  @Test
  @SneakyThrows
  public void testNobleNextActionReturns() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    when(playerMock.addCardToInventory(mockNoble)).thenReturn(true);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.NOBLE);
    Action mockActionNext = mock(Action.class);
    when(mockActionNext.getActionDetails()).thenReturn(
        CustomResponseFactory.getResponse(CustomHttpResponses.CHOOSE_CITY));
    when(playerMock.peekTopAction()).thenReturn(mockAction).thenReturn(mockActionNext);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.CHOOSE_CITY.getBody(), response.getBody());
    assertEquals(CustomHttpResponses.CHOOSE_CITY.getStatus(), response.getStatusCodeValue());
  }

  /**
   * Test noble current action null.
   */
  @Test
  @SneakyThrows
  public void testNobleCurrentActionNull() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    when(playerMock.addCardToInventory(mockNoble)).thenReturn(true);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    when(playerMock.peekTopAction()).thenReturn(null);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.SERVER_SIDE_ERROR.getBody(), response.getBody());
    assertEquals(CustomHttpResponses.SERVER_SIDE_ERROR.getStatus(), response.getStatusCodeValue());
  }

  /**
   * Test noble illegal action.
   */
  @Test
  @SneakyThrows
  public void testNobleIllegalAction() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    when(playerMock.addCardToInventory(mockNoble)).thenReturn(true);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.CITY);
    when(playerMock.peekTopAction()).thenReturn(mockAction).thenReturn(null);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.ILLEGAL_ACTION.getBody(), response.getBody());
    assertEquals(CustomHttpResponses.ILLEGAL_ACTION.getStatus(), response.getStatusCodeValue());
  }

  /**
   * Test noble player cannot be visited by.
   */
  @Test
  @SneakyThrows
  public void testNoblePlayerCannotBeVisitedBy() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(false);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.NOBLE);
    when(playerMock.peekTopAction()).thenReturn(mockAction);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT.getBody(), response.getBody());
  }

  /**
   * Test noble player cannot be added to inventory.
   */
  @Test
  @SneakyThrows
  public void testNoblePlayerCannotBeAddedToInventory() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "valid hash";
    final ServerNoble mockNoble = Mockito.mock(ServerNoble.class);

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(playerMock.canBeVisitedBy(mockNoble)).thenReturn(true);
    when(playerMock.addCardToInventory(mockNoble)).thenReturn(false);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.NOBLE);
    when(playerMock.peekTopAction()).thenReturn(mockAction).thenReturn(null);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.SERVER_SIDE_ERROR.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.SERVER_SIDE_ERROR.getBody(), response.getBody());
  }

  /**
   * Test noble invalid hash.
   */
  @Test
  @SneakyThrows
  public void testNobleInvalidHash() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "invalid hash";

    var request = serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken);
    Game gameMock = request.getValue().getLeft();
    ServerPlayer playerMock = Mockito.mock(ServerPlayer.class);
    when(serviceUtils.validRequestAndCurrentTurn(validSessionId, validAccessToken)).thenReturn(
        Pair.of(ResponseEntity.ok().build(), Pair.of(gameMock, playerMock)));
    when(gameMock.getCardByHash(nobleHash)).thenReturn(null);
    Action mockAction = mock(Action.class);
    when(mockAction.getActionType()).thenReturn(CustomHttpResponses.ActionType.NOBLE);
    when(playerMock.peekTopAction()).thenReturn(mockAction);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.BAD_CARD_HASH.getBody(), response.getBody());
  }

  /**
   * Test acquire noble invalid session id.
   */
  @Test
  @SneakyThrows
  public void testAcquireNobleInvalidSessionId() {
    // Arrange
    final var invalidSessionId = DummyAuths.invalidSessionIds.get(0);
    final var validAccessToken = DummyAuths.validTokensInfos.get(0).getAccessToken();
    final var nobleHash = "";

    // Act
    var response = inventoryService.acquireNoble(invalidSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), response.getBody());
  }

  /**
   * Test acquire noble invalid access token.
   */
  @Test
  @SneakyThrows
  public void testAcquireNobleInvalidAccessToken() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(0).getAccessToken();
    final var nobleHash = "";

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, invalidAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), response.getBody());
  }

  /*
   * Test acquire noble not your turn.
   */
  @Test
  @SneakyThrows
  public void testAcquireNobleNotPlayerTurn() {
    // Arrange
    final var validSessionId = DummyAuths.validSessionIds.get(0);
    final var invalidAccessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var nobleHash = "";

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, invalidAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.NOT_PLAYERS_TURN.getBody(), response.getBody());
  }

  private ServerLevelCard createValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE, new PurchaseMap(
        Map.of(Gem.RUBY, 1)));
  }

  private ServerLevelCard createValidTakeTwoCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE,
        LevelCard.BonusType.CASCADING_TWO, new PurchaseMap(
        Map.of(Gem.RUBY, 1)));
  }

  private ServerLevelCard createTooExpensiveValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(20, 1, 1, 1, 0), Level.ONE,
        new PurchaseMap(Map.of(Gem.RUBY, 1)));
  }

  private ServerLevelCard createInvalidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE,
        new PurchaseMap(Map.of(Gem.RUBY, 1)));
  }
}
