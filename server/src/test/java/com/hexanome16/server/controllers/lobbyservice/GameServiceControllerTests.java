package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.controllers.lobbyservice.gameservice.GameServiceController;
import com.hexanome16.server.models.sessions.GameParams;
import com.hexanome16.server.util.UrlUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlUtils.class, GameParams.class, AuthController.class,
    GameServiceController.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameServiceControllerTests {
  @Autowired
  private GameServiceController gameServiceController;

  @Test
  public void createGameService() {

  }
}
