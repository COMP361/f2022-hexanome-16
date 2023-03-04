package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.ServiceUtils;
import java.io.IOException;
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
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new WinCondition[] {WinCondition.BASE});

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
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), response.getStatusCode().value());
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
    var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();
    Game gameMock = gameManagerMock.getGame(sessionId);
    serviceUtils.endCurrentPlayersTurn(gameMock);

    gameMock.getLevelDeck(myCard.getLevel()).addCard(myCard);
    gameMock.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

    ResponseEntity<String> response =
        inventoryService.buyCard(sessionId,
            DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)),
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

    serviceUtils.endCurrentPlayersTurn(validMockGame);
    ServerLevelCard myCard = createValidCard();
    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

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
  public void testReserveFaceDownCard() throws com.fasterxml.jackson.core.JsonProcessingException {
    final var sessionId = DummyAuths.validSessionIds.get(0);
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();

    serviceUtils.endCurrentPlayersTurn(validMockGame);
    ServerLevelCard myCard = createValidCard();
    validMockGame.getLevelDeck(myCard.getLevel()).addCard(myCard);
    validMockGame.getRemainingCards().put(
        DigestUtils.md5Hex(objectMapper.writeValueAsString(myCard)), myCard
    );

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
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();

    serviceUtils.endCurrentPlayersTurn(validMockGame);

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
    final var accessToken = DummyAuths.validTokensInfos.get(1).getAccessToken();
    final var invalidAccessToken = DummyAuths.invalidTokensInfos.get(1).getAccessToken();

    ServerLevelCard myCard = createValidCard();

    serviceUtils.endCurrentPlayersTurn(validMockGame);

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

  private ServerLevelCard createValidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private ServerLevelCard createInvalidCard() {
    return new ServerLevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }
}
