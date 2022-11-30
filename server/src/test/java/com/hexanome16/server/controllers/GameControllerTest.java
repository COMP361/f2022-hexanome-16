package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.async.DeferredResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Tests for {@link GameController}.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GameController.class, AuthController.class, UrlUtils.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameControllerTest {
  private final String kim = "{\"name\":" + "\"kim\"," + "\"preferredColour\":" + "\"#FFFFFF\"}";
  private final String imad = "{\"name\":" + "\"imad\"," + "\"preferredColour\":" + "\"#FFFFFF\"}";
  private final String creator = "imad";
  private final String savegame = "";
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  private final Map<String, Object> payload = new HashMap<String, Object>();
  @Autowired
  private GameController gameController;
  @Autowired
  private AuthController authController;
  private String accessToken;
  private String invalidAccessToken;
  private String gameResponse;

  /**
   * Setup mock for game tests.
   *
   * @throws com.fasterxml.jackson.core.JsonProcessingException --
   */
  @BeforeEach
  public void createGame() throws com.fasterxml.jackson.core.JsonProcessingException {
    ResponseEntity<TokensInfo> tokens = authController.login("kim", "123");
    authController.login("imad", "123");
    accessToken = tokens.getBody().getAccessToken();
    tokens = authController.login("peini", "123");
    invalidAccessToken = tokens.getBody().getAccessToken();
    List playerList = new ArrayList<String>();
    playerList.add(objectMapper.readValue(imad, Map.class));
    playerList.add(objectMapper.readValue(kim, Map.class));
    payload.put("players", playerList);
    payload.put("creator", creator);
    payload.put("savegame", savegame);
    gameResponse = gameController.createGame(12345, payload);
  }

  @Test
  public void testCreateGame() throws com.fasterxml.jackson.core.JsonProcessingException {
    assertEquals("success", gameResponse);
  }

  @Test
  public void testUpdateDeckSuccess()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameController.getDeck(12345, "ONE", accessToken, hash)
            .getResult();
    assertNotNull(response);
  }

  @Test
  public void testUpdateDeckFail()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameController.getDeck(12345, "ONE",
            invalidAccessToken, hash);
    assertNull(response);
  }

  @Test
  public void testUpdateNoblesSuccess()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameController.getNobles(12345, accessToken, hash).getResult();
    assertNotNull(response);
  }

  @Test
  public void testUpdateNoblesFail()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameController.getNobles(12345, invalidAccessToken,
            hash);
    assertNull(response);
  }

  @Test
  public void testCurrentPlayerSuccess()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameController.getCurrentPlayer(12345, accessToken, hash)
            .getResult();
    assertNotNull(response);
  }

  @Test
  public void testCurrentPlayerFail()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    DeferredResult<ResponseEntity<String>> response =
        gameController.getCurrentPlayer(12345,
            invalidAccessToken, hash);
    assertNull(response);
  }
}

