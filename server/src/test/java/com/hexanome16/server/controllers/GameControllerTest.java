package com.hexanome16.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hexanome16.server.controllers.lobbyservice.AuthController;
import com.hexanome16.server.util.UrlUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GameController.class, AuthController.class, UrlUtils.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameControllerTest {
  @Autowired
  private GameController gameController;

  private final String kim =
      "\"name\":" + "\"kim\"," + "\"preferredColor\":" +
          "\"preferredColor\":" + "\"#FFFFFF\"";

  private final String imad =
      "\"name\":" + "\"imad\"," + "\"preferredColor\":" +
          "\"preferredColor\":" + "\"#FFFFFF\"";

  private final String creator = "\"creator\":" + "\"imad\":";

  private final String savegame = "\"savegame\":" + "\"\":";

  private Map<String, Object> payload = new HashMap<String, Object>();

  @Test
  public void testCreateGame() {
    payload.put("player", "[{" + kim + "},{" + imad + "}]");
    payload.put("creator", "[{" + creator + "}]");
    payload.put("savegame", "[{" + savegame + "}]");
    String gameResponse = gameController.createGame(12345, payload);
    assertEquals("success", gameResponse);
  }
}

