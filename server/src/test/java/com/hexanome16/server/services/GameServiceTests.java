package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * The type Game service tests.
 */
class GameServiceTests {

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private Game validMockGame;
  private GameService gameService;
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

    serviceUtils = new ServiceUtils();

    gameManagerMock = Mockito.mock(GameManagerService.class);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    gameService = new GameService(new DummyAuthService(), gameManagerMock, serviceUtils);

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
        (ResponseEntity<String>) gameService.getDeck(DummyAuths.validSessionIds.get(0), "ONE",
            DummyAuths.validTokensInfos.get(0).getAccessToken(), hash).getResult();
    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * GetDeck should return http error when invalid access token.
   */
  @SneakyThrows
  @Test
  public void getDeck_shouldReturnHttpError_whenInvalidAccessToken() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    DeferredResult<ResponseEntity<String>> response =
        gameService.getDeck(DummyAuths.validSessionIds.get(0), "ONE",
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), result.getStatusCode());
  }

  /**
   * GetDeck should return http error when invalid session id.
   */
  @SneakyThrows
  @Test
  public void getDeck_shouldReturnHttpError_WhenInvalidSessionId() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getDeck(DummyAuths.invalidSessionIds.get(0), "ONE",
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), result.getStatusCode());
  }

  @SneakyThrows
  @Test
  public void getWinners_shouldReturnSuccess_whenValidParams() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    DeferredResult<ResponseEntity<String>> response =
        gameService.getWinners(DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(response);
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  /**
   * GetWinners should return http error when invalid access token.
   */
  @SneakyThrows
  @Test
  public void getWinners_shouldReturnHttpError_whenInvalidAccessToken() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    DeferredResult<ResponseEntity<String>> response =
        gameService.getWinners(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), result.getStatusCode());
  }

  /**
   * GetWinners should return http error when invalid session id.
   */
  @SneakyThrows
  @Test
  public void getWinners_shouldReturnHttpError_WhenInvalidSessionId() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getWinners(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), result.getStatusCode());
  }

  /**
   * Test update nobles success.
   *
   * @throws JsonProcessingException the json processing exception
   */
  @Test
  public void testUpdateNoblesSuccess() throws com.fasterxml.jackson.core.JsonProcessingException {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameService.getNobles(DummyAuths.validSessionIds.get(0),
            DummyAuths.validTokensInfos.get(0).getAccessToken(), hash).getResult();

    // Assert

    assertNotNull(response);
  }

  /**
   * GetNobles should return http error when invalid session id.
   */
  @SneakyThrows
  @Test
  public void getNobles_shouldReturnHttpError_whenInvalidSessionId() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getNobles(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), result.getStatusCode());
  }

  /**
   * GetNobles should return http error when invalid access token.
   */
  @SneakyThrows
  @Test
  public void getNobles_shouldReturnHttpError_whenInvalidAccessToken() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getNobles(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), result.getStatusCode());
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
   * GetCurrentPlayer should return http error when invalid access token.
   */
  @SneakyThrows
  @Test
  public void getCurrentPlayer_shouldReturnHttpError_whenInvalidAccessToken() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getCurrentPlayer(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(), result.getStatusCode());
  }

  /**
   * GetCurrentPlayer should return http error when invalid session id.
   */
  @SneakyThrows
  @Test
  public void getCurrentPlayer_shouldReturnHttpError_whenInvalidSessionId() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    var response = gameService.getCurrentPlayer(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(), result.getStatusCode());
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

  private LevelCard createValidCard() {
    return new LevelCard(20, 0, "", new PriceMap(1, 1, 1, 1, 0), Level.ONE);
  }

  private LevelCard createInvalidCard() {
    return new LevelCard(20, 0, "", new PriceMap(7, 1, 1, 1, 0), Level.ONE);
  }
}
