package com.hexanome16.server.services.auth;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.services.DummyAuths;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * Tests for {@link AuthService}.
 */
public class AuthServiceTest {

  private final String[] usernames = {
      DummyAuths.validPlayerList.get(0).getName(),
      DummyAuths.invalidPlayerList.get(0).getName()
  };
  private final String[] passwords = {
      DummyAuths.validPasswords.get(0),
      DummyAuths.invalidPasswords.get(0)
  };
  private final String[] tokens = {
      DummyAuths.validTokensInfos.get(0).getAccessToken(),
      DummyAuths.invalidTokensInfos.get(0).getAccessToken()
  };
  private final Long[] sessionIds = {
      DummyAuths.validSessionIds.get(0),
      DummyAuths.invalidSessionIds.get(0)
  };
  private final DummyAuthService authService = new DummyAuthService();

  @Test
  void testValidLogin() {
    ResponseEntity<TokensInfo> validResponse = authService.login(usernames[0], passwords[0]);
    assertTrue("Valid login should return 200", validResponse.getStatusCodeValue() == 200);
    assertTrue("Valid login should return a token", Objects.requireNonNull(
        validResponse.getBody()).getAccessToken() != null);
    assertTrue("Valid login should return a valid token",
        validResponse.getBody().getAccessToken().equals(
            DummyAuths.validTokensInfos.get(0).getAccessToken()));
  }

  @Test
  void testInvalidLogin() {
    ResponseEntity<TokensInfo> invalidResponse = authService.login(usernames[1], passwords[1]);
    assertTrue("Invalid login should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  void testValidRefreshLogin() {
    ResponseEntity<TokensInfo> validResponse = authService.login(tokens[0]);
    assertTrue("Valid refresh should return 200", validResponse.getStatusCodeValue() == 200);
    assertTrue("Valid refresh should return a token", Objects.requireNonNull(
        validResponse.getBody()).getAccessToken() != null);
    assertEquals(
        "Valid refresh should return a valid token",
        validResponse.getBody().getAccessToken(),
        DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
  }

  @Test
  void testInvalidRefreshLogin() {
    ResponseEntity<TokensInfo> invalidResponse = authService.login(tokens[1]);
    assertTrue("Invalid login should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  void testValidGetPlayer() {
    ResponseEntity<String> validResponse = authService.getPlayer(tokens[0]);
    assertTrue("Valid get player should return 200", validResponse.getStatusCodeValue() == 200);
    assertEquals("Valid get player should return the username", Objects.requireNonNull(
        validResponse.getBody()), usernames[0]);
  }

  @Test
  void testInvalidGetPlayer() {
    ResponseEntity<String> invalidResponse = authService.getPlayer(tokens[1]);
    assertTrue("Invalid get player should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  void testValidLogout() {
    ResponseEntity<Void> validResponse = authService.logout(tokens[0]);
    assertTrue("Valid logout should return 200", validResponse.getStatusCodeValue() == 200);
  }

  @Test
  void testInvalidLogout() {
    ResponseEntity<Void> invalidResponse = authService.logout(tokens[1]);
    assertTrue("Invalid logout should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  void testValidVerifyPlayer() {
    assertTrue(
        "Valid player and session should return true",
        authService.verifyPlayer(sessionIds[0], tokens[0], null)
    );
  }

  @Test
  void testInvalidVerifyPlayer() {
    assertFalse(
        "Invalid session should return false",
        authService.verifyPlayer(sessionIds[1], tokens[0], null)
    );
    assertFalse(
        "Invalid token should return false",
        authService.verifyPlayer(sessionIds[0], tokens[1], null)
    );
    assertFalse(
        "Both invalid should return false",
        authService.verifyPlayer(sessionIds[1], tokens[1], null)
    );
  }
}
