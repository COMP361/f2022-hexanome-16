package com.hexanome16.server.controllers.lobbyservice;

import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.controllers.lobbyservice.gameservice.GameServiceController;
import com.hexanome16.server.util.UrlUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlUtils.class, AuthController.class, GameServiceController.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class GameServiceControllerTests {

}
