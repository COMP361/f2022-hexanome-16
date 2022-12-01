package com.hexanome16.server.controllers.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.controllers.lobbyservice.gameservice.GameServiceController;
import com.hexanome16.server.models.sessions.GameParams;
import com.hexanome16.server.util.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This class tests the GameServiceController.
 * These aren't exactly unit tests since they request the external Lobby Service
 * but hey code coverage is code coverage :)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlUtils.class, GameParams.class, AuthController.class,
    GameServiceController.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameServiceControllerTests {
  @Autowired
  private GameServiceController gameServiceController;

  /**
   * Test create game service.
   */
  @Test
  public void createGameService() {
    ResponseEntity<Void> response = gameServiceController.createGameService();
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test delete game service.
   */
  @Test
  public void deleteGameService() {
    ResponseEntity<Void> response = gameServiceController.deleteGameService();
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }
}
