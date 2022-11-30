package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.controllers.lobbyservice.AuthController;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GameController.class, AuthController.class, UrlUtils.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameControllerTest {
  @Autowired
  private GameController gameController;

  @Autowired
  private AuthController authController;

  private String accessToken;

  private final String kim =
      "\"name\":" + "\"kim\"," +
          "\"preferredColour\":" + "\"#FFFFFF\"";

  private final String imad =
      "\"name\":" + "\"imad\"," +
          "\"preferredColour\":" + "\"#FFFFFF\"";

  private final String creator = "\"creator\":" + "\"imad\":";

  private final String savegame = "\"savegame\":" + "\"\":";

  private Map<String, Object> payload = new HashMap<String, Object>();

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

  @Test
  public void testCreateGame() {
    ResponseEntity<TokensInfo> tokens = authController.login("kim", "123");
    authController.login("imad", "123");
    accessToken = tokens.getBody().getAccessToken();
    payload.put("players", "[{" + kim + "},{" + imad + "}]");
    System.out.println("[{" + kim + "},{" + imad + "}]");
    payload.put("creator", creator);
    payload.put("savegame", savegame);
    String gameResponse = gameController.createGame(12345, payload);
    assertEquals("success", gameResponse);
  }

  @Test
  public void testUpdateDeck()
      throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
    String hash = DigestUtils.md5Hex(objectMapper.writeValueAsString(""));
    ResponseEntity<String> response =
        (ResponseEntity<String>) gameController.getDeck(12345, "ONE", "accessToken", hash).getResult();
    assertNotNull(response);
  }
}

