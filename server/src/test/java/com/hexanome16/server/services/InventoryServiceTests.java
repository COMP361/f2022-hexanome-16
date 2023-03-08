package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.ServerNoble;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import java.util.LinkedList;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
            new WinCondition[] {WinCondition.BASE});

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
    Game gameMock =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken).getRight().getLeft();
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
    assertEquals(HttpStatus.OK, response.getStatusCode());
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
    validMockGame.getRemainingCards().put(
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
    validMockGame.getRemainingCards().put(
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
    validMockGame.getRemainingCards().put(
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
    validMockGame.getRemainingCards().put(
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

    ServerLevelCard card = createValidCard();
    MultiValueMap<String, String> goodHeaders = new HttpHeaders();
    goodHeaders.put(CustomHttpResponses.ActionType.ACTION_TYPE,
        List.of(CustomHttpResponses.ActionType.LEVEL_TWO.getMessage()));
    ResponseEntity<String> goodResponse = new ResponseEntity<>(
        goodHeaders, HttpStatus.OK);
    ServerPlayer validplayer = Mockito.mock(ServerPlayer.class);
    Game game = Mockito.mock(Game.class);
    String cardHash = DigestUtils.md5Hex(objectMapper.writeValueAsString(card));

    when(serviceUtils.validRequestAndCurrentTurn(123, "testingToken"))
        .thenReturn(
            new ImmutablePair<>(goodResponse,
                new ImmutablePair<>(game, validplayer)));
    when(validplayer.peekTopAction())
        .thenReturn(goodResponse);
    when(game.getCardByHash(cardHash)).thenReturn(card);
    when(game.getOnBoardDeck(card.getLevel())).thenReturn(new Deck<>());
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
    when(validplayer.peekTopAction())
        .thenReturn(new ResponseEntity<>(badHeaders, HttpStatus.BAD_REQUEST));
    when(game.getCardByHash(cardHash)).thenReturn(card);
    response = inventoryService
        .takeLevelTwoCard(123, "testingToken",
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
    List<Game> gameDummies = GameDummies.getInstance();
    Game gameMock = gameDummies.get(0);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(gameMock);
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    assertEquals(CustomHttpResponses.OK.getStatus(), response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.OK.getBody(), response.getBody());
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

    //TODO : fix this when merging imad's pr and test this correctly
    List<Game> gameDummies = GameDummies.getInstance();
    Game gameMock = gameDummies.get(0);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(gameMock);
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(mockNoble);

    // Act
    var response = inventoryService.acquireNoble(validSessionId, nobleHash, validAccessToken);

    // Assert
    /*
    assertEquals(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT.getStatus(),
        response.getStatusCodeValue());
    assertEquals(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT.getBody(), response.getBody());
    */
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
    List<Game> gameDummies = GameDummies.getInstance();
    Game gameMock = gameDummies.get(0);
    when(gameManagerMock.getGame(validSessionId)).thenReturn(gameMock);
    when(gameMock.getNobleByHash(nobleHash)).thenReturn(null);

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

  // TODO: TRISTAN PLS FIX THIS SHIT
  /*
   * Test acquire noble not your turn.

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
  }*/

  private ServerLevelCard createValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private ServerLevelCard createTooExpensiveValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(20, 1, 1, 1, 0), Level.ONE);
  }

  private ServerLevelCard createInvalidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }
}
