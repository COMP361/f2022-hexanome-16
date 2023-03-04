package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.PlayerDummies;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.longpolling.LongPollingService;
import com.hexanome16.server.services.longpolling.LongPollingServiceInterface;
import java.io.IOException;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import com.hexanome16.common.util.CustomHttpResponses;

/**
 * Unit tests for {@link LongPollingService}.
 */
public class LongPollingServiceTests {
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private LongPollingServiceInterface longPollingService;

  /**
   * Sets up tests with mocks.
   */
  @BeforeEach
  void setup() throws IOException {

    Game validMockGame =
        Game.create(DummyAuths.validSessionIds.get(0), PlayerDummies.validDummies, "imad", "",
            new WinCondition[]{WinCondition.BASE});

    GameManagerServiceInterface gameManagerMock = Mockito.mock(GameManagerService.class);
    when(gameManagerMock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(validMockGame);
    when(gameManagerMock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);

    longPollingService = new LongPollingService(gameManagerMock, new DummyAuthService());
  }

  /**
   * Test update deck success.
   */
  @SneakyThrows
  @Test
  public void testUpdateDeckSuccess() {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) longPollingService.getDeck(
            DummyAuths.validSessionIds.get(0), "ONE",
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
        longPollingService.getDeck(DummyAuths.validSessionIds.get(0), "ONE",
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        result.getStatusCode().value());
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
    var response = longPollingService.getDeck(DummyAuths.invalidSessionIds.get(0), "ONE",
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        result.getStatusCode().value());
  }

  @SneakyThrows
  @Test
  public void getWinners_shouldReturnSuccess_whenValidParams() {
    // Arrange
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));

    // Act
    DeferredResult<ResponseEntity<String>> response =
        longPollingService.getWinners(DummyAuths.validSessionIds.get(0),
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
        longPollingService.getWinners(DummyAuths.validSessionIds.get(0),
            DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        result.getStatusCode().value());
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
    var response = longPollingService.getWinners(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        result.getStatusCode().value());
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
        (ResponseEntity<String>) longPollingService.getNobles(DummyAuths.validSessionIds.get(0),
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
    var response = longPollingService.getNobles(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        result.getStatusCode().value());
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
    var response = longPollingService.getNobles(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        result.getStatusCode().value());
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
        (ResponseEntity<String>) longPollingService.getCurrentPlayer(
            DummyAuths.validSessionIds.get(0),
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
    var response = longPollingService.getCurrentPlayer(DummyAuths.validSessionIds.get(0),
        DummyAuths.invalidTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_ACCESS_TOKEN.getStatus(),
        result.getStatusCode().value());
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
    var response = longPollingService.getCurrentPlayer(DummyAuths.invalidSessionIds.get(0),
        DummyAuths.validTokensInfos.get(0).getAccessToken(), hash);
    var result = (ResponseEntity<String>) response.getResult();

    // Assert
    assertNotNull(result);
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getBody(), result.getBody());
    assertEquals(CustomHttpResponses.INVALID_SESSION_ID.getStatus(),
        result.getStatusCode().value());
  }
}
