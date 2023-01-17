package com.hexanome16.server.services.auth;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.hexanome16.server.controllers.DummyAuthService;
import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
  private final DummyAuthService authService = new DummyAuthService();

  /*private URI createMockedUri() {
    String baseUrl = "http://localhost:4242";
    return URI.create(
        "%s%s?%s".formatted(baseUrl, argThat((String s) -> s.contains("oauth")),
            argThat((String s) -> s.contains("="))));
  }

  @BeforeEach
  void setup() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setBasicAuth("bgp-client-name", "bgp-client-pw");

    // Mock autowired dependencies
    RestTemplateBuilder restTemplateBuilderMock = Mockito.mock(RestTemplateBuilder.class);
    RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    UrlUtils urlUtilsMock = Mockito.mock(UrlUtils.class);
    when(urlUtilsMock.createLobbyServiceUri(anyString(), anyString()))
        .thenReturn(createMockedUri());

    for (int i = 0; i < usernames.length; i++) {
      when(restTemplateMock.postForEntity(
          urlUtilsMock.createLobbyServiceUri(
              "/oauth/login",
              "grant_type=password&username=" + usernames[i] + "&password=" + passwords[i]
          ),
          new HttpEntity<>(null, headers),
          TokensInfo.class
      )).thenReturn(authService.login(usernames[i], passwords[i]));
    }

    for (String token : tokens) {
      when(restTemplateMock.postForEntity(
          urlUtilsMock.createLobbyServiceUri(
              "/oauth/login",
              "grant_type=refresh_token&refresh_token=" + token
          ),
          new HttpEntity<>(null, headers),
          TokensInfo.class
      )).thenReturn(authService.login(token));

      when(restTemplateMock.getForEntity(
          urlUtilsMock.createLobbyServiceUri(
              "/oauth/username",
              "access_token=" + token
          ),
          TokensInfo.class
      )).thenReturn(authService.login(token));

      when(restTemplateMock.postForEntity(
          urlUtilsMock.createLobbyServiceUri(
              "/oauth/active",
              "access_token=" + token
          ),
          new HttpEntity<>(null, headers),
          Void.class
      )).thenReturn(authService.logout(token));
    }
  }*/

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
}
