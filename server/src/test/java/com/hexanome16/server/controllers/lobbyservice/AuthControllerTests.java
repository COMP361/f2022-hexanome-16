package com.hexanome16.server.controllers.lobbyservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.server.LobbyServiceContainer;
import com.hexanome16.server.controllers.lobbyservice.auth.AuthController;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.util.UrlUtils;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlUtils.class, AuthController.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class AuthControllerTests {
  @ClassRule
  public static DockerComposeContainer<LobbyServiceContainer> lsContainer =
      LobbyServiceContainer.getInstance();

  @Autowired
  private AuthController authController;

  private final String password = "abc123_ABC123";

  @Test
  public void testValidLogin() {
    ResponseEntity<TokensInfo> tokens = authController.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    assertNotNull(tokens.getBody().getAccessToken());
    assertNotNull(tokens.getBody().getRefreshToken());
  }

  @Test
  public void testInvalidLogin() {
    try {
      authController.login("maex", "invalid_password");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  @Test
  public void testValidRefresh() {
    ResponseEntity<TokensInfo> tokens = authController.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String refreshToken = tokens.getBody().getRefreshToken();
    tokens = authController.login(refreshToken);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    assertNotNull(tokens.getBody().getAccessToken());
    assertNotNull(tokens.getBody().getRefreshToken());
  }

  @Test
  public void testInvalidRefresh() {
    ResponseEntity<TokensInfo> tokens = authController.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String refreshToken = tokens.getBody().getRefreshToken();
    try {
      authController.login(refreshToken + "invalid");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  @Test
  public void testValidLogout() {
    ResponseEntity<TokensInfo> tokens = authController.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String accessToken = tokens.getBody().getAccessToken();
    ResponseEntity<Void> response = authController.logout(accessToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  @Test
  public void testInvalidLogout() {
    try {
      authController.logout("invalid_access_token");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  @Test
  public void testValidGetPlayer() {
    ResponseEntity<TokensInfo> tokens = authController.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String accessToken = tokens.getBody().getAccessToken();
    ResponseEntity<String> response = authController.getPlayer(accessToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals("maex", response.getBody());
  }

  @Test
  public void testInvalidGetPlayer() {
    try {
      authController.getPlayer("invalid_access_token");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }
}
