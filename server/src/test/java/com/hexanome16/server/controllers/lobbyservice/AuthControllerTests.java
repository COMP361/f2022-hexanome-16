package com.hexanome16.server.controllers.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import com.hexanome16.server.LobbyServiceContainer;
import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;

/**
 * This class is used to test the auth controller (Lobby Service).
 */
@ExtendWith(SpringExtension.class)
@RestClientTest(AuthController.class)
public class AuthControllerTests {
  @ClassRule
  public static DockerComposeContainer<LobbyServiceContainer> lsContainer =
      LobbyServiceContainer.getInstance();

  @Autowired
  private AuthController authController;

  @MockBean
  private final UrlUtils urlUtils = new UrlUtils();

  private final String password = "abc123_ABC123";

  @Test
  public void testValidLogin() {
    ResponseEntity<TokensInfo> tokensInfo = authController.login("maex", password);
    assertTrue(tokensInfo.getStatusCode().is2xxSuccessful());
    assertNotNull(tokensInfo.getBody());
    assertNotNull(tokensInfo.getBody().getAccessToken());
    assertNotNull(tokensInfo.getBody().getRefreshToken());
  }

  @Test
  public void testInvalidLogin() {
    ResponseEntity<TokensInfo> tokensInfo = authController.login("maex", "invalid_password");
    assertTrue(tokensInfo.getStatusCode().is4xxClientError());
  }

  @Test
  public void testValidRefresh() {
    ResponseEntity<TokensInfo> response = authController.login("maex", password);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertNotNull(response.getBody());
    String refreshToken = response.getBody().getRefreshToken();
    response = authController.login(refreshToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertNotNull(response.getBody());
  }

  @Test
  public void testInvalidRefresh() {
    ResponseEntity<TokensInfo> response = authController.login("maex", password);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertNotNull(response.getBody());
    String refreshToken = response.getBody().getRefreshToken();
    response = authController.login(refreshToken + "random");
    assertTrue(response.getStatusCode().is4xxClientError());
  }
}
