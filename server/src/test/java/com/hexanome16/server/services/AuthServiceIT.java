package com.hexanome16.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hexanome16.server.LobbyServiceContainer;
import com.hexanome16.server.services.auth.AuthService;
import com.hexanome16.server.util.UrlUtils;
import com.hexanome16.common.models.auth.TokensInfo;
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

/**
 * This class tests the AuthController.
 * These aren't exactly unit tests since they request the external Lobby Service
 * but hey code coverage is code coverage :)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UrlUtils.class, AuthService.class})
@RestClientTest(excludeAutoConfiguration = MockRestServiceServerAutoConfiguration.class)
public class AuthServiceIT {
  /**
   * The Ls container.
   */
  @ClassRule
  public static DockerComposeContainer<LobbyServiceContainer> lsContainer =
      LobbyServiceContainer.getInstance();
  private final String password = "abc123_ABC123";
  @Autowired
  private AuthService authService;

  /**
   * Test valid login.
   */
  @Test
  public void testValidLogin() {
    ResponseEntity<TokensInfo> tokens = authService.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    assertNotNull(tokens.getBody().getAccessToken());
    assertNotNull(tokens.getBody().getRefreshToken());
  }

  /**
   * Test invalid login.
   */
  @Test
  public void testInvalidLogin() {
    try {
      authService.login("maex", "invalid_password");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  /**
   * Test valid refresh.
   */
  @Test
  public void testValidRefresh() {
    ResponseEntity<TokensInfo> tokens = authService.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String refreshToken = tokens.getBody().getRefreshToken();
    tokens = authService.login(refreshToken);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    assertNotNull(tokens.getBody().getAccessToken());
    assertNotNull(tokens.getBody().getRefreshToken());
  }

  /**
   * Test invalid refresh.
   */
  @Test
  public void testInvalidRefresh() {
    ResponseEntity<TokensInfo> tokens = authService.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String refreshToken = tokens.getBody().getRefreshToken();
    try {
      authService.login(refreshToken + "invalid");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  /**
   * Test valid logout.
   */
  @Test
  public void testValidLogout() {
    ResponseEntity<TokensInfo> tokens = authService.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String accessToken = tokens.getBody().getAccessToken();
    ResponseEntity<Void> response = authService.logout(accessToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }

  /**
   * Test invalid logout.
   */
  @Test
  public void testInvalidLogout() {
    try {
      authService.logout("invalid_access_token");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }

  /**
   * Test valid get player.
   */
  @Test
  public void testValidGetPlayer() {
    ResponseEntity<TokensInfo> tokens = authService.login("maex", password);
    assertTrue(tokens.getStatusCode().is2xxSuccessful());
    assertNotNull(tokens.getBody());
    String accessToken = tokens.getBody().getAccessToken();
    ResponseEntity<String> response = authService.getPlayer(accessToken);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals("maex", response.getBody());
  }

  /**
   * Test invalid get player.
   */
  @Test
  public void testInvalidGetPlayer() {
    try {
      authService.getPlayer("invalid_access_token");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("400"));
    }
  }
}
