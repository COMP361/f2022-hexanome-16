package com.hexanome16.server.services.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.hexanome16.server.models.auth.TokensInfo;
import com.hexanome16.server.services.DummyAuths;
import com.hexanome16.server.util.UrlUtils;
import java.net.URI;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Tests for {@link AuthService}.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {
  private AuthService authService;
  private UrlUtils urlUtilsMock;
  @Mock
  private RestTemplate restTemplateMock;

  @BeforeEach
  void setup() {
    // Mock autowired dependencies
    RestTemplateBuilder restTemplateBuilderMock = Mockito.mock(RestTemplateBuilder.class);
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    urlUtilsMock = Mockito.mock(UrlUtils.class);
    when(urlUtilsMock.createLobbyServiceUri(anyString(), anyString())).thenAnswer(invocation -> {
      String path = invocation.getArgument(0);
      String params = invocation.getArgument(1);
      return URI.create("http://localhost:4242" + path + "?" + params);
    });

    authService = new AuthService(restTemplateBuilderMock, urlUtilsMock);
    ReflectionTestUtils.setField(authService, "lsUsername", "bgp-client-name");
    ReflectionTestUtils.setField(authService, "lsPassword", "bgp-client-pwd");
  }

  @Test
  @Order(1)
  void testValidLogin() {
    when(restTemplateMock.postForEntity(any(URI.class), any(), eq(TokensInfo.class)))
        .thenReturn(new ResponseEntity<>(DummyAuths.validTokensInfos.get(0), HttpStatus.OK));
    ResponseEntity<TokensInfo> validResponse = authService.login(
        DummyAuths.validPlayerList.get(0).getName(),
        DummyAuths.validPasswords.get(0)
    );
    assertTrue("Valid login should return 200", validResponse.getStatusCodeValue() == 200);
    assertTrue("Valid login should return a token", Objects.requireNonNull(
        validResponse.getBody()).getAccessToken() != null);
    assertTrue("Valid login should return a valid token",
        validResponse.getBody().getAccessToken().equals(
            DummyAuths.validTokensInfos.get(0).getAccessToken()));
  }

  @Test
  @Order(2)
  void testInvalidLogin() {
    when(restTemplateMock.postForEntity(any(URI.class), any(), eq(TokensInfo.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    ResponseEntity<TokensInfo> invalidResponse = authService.login(
        DummyAuths.invalidPlayerList.get(0).getName(),
        DummyAuths.invalidPasswords.get(0)
    );
    assertTrue("Invalid login should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  @Order(3)
  void testValidRefreshLogin() {
    when(restTemplateMock.postForEntity(any(URI.class), any(), eq(TokensInfo.class)))
        .thenReturn(new ResponseEntity<>(DummyAuths.validTokensInfos.get(0), HttpStatus.OK));
    ResponseEntity<TokensInfo> validResponse = authService.login(
        DummyAuths.validTokensInfos.get(0).getRefreshToken()
    );
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
  @Order(4)
  void testInvalidRefreshLogin() {
    when(restTemplateMock.postForEntity(any(URI.class), any(), eq(TokensInfo.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    ResponseEntity<TokensInfo> invalidResponse = authService.login(
        DummyAuths.invalidTokensInfos.get(0).getRefreshToken()
    );
    assertTrue("Invalid login should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  @Order(5)
  void testValidGetPlayer() {
    URI uri = urlUtilsMock.createLobbyServiceUri(
        "/oauth/username",
        "access_token=" + DummyAuths.validTokensInfos.get(0).getAccessToken()
    );
    when(restTemplateMock.getForEntity(uri, String.class))
        .thenReturn(ResponseEntity.ok(DummyAuths.validPlayerList.get(0).getName()));
    ResponseEntity<String> validResponse =
        authService.getPlayer(DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertTrue("Valid get player should return 200", validResponse.getStatusCodeValue() == 200);
    assertEquals("Valid get player should return the username", Objects.requireNonNull(
        validResponse.getBody()), DummyAuths.validPlayerList.get(0).getName());
  }

  @Test
  @Order(6)
  void testInvalidGetPlayer() {
    URI uri = urlUtilsMock.createLobbyServiceUri(
        "/oauth/username",
        "access_token=" + DummyAuths.invalidTokensInfos.get(0).getAccessToken()
    );
    when(restTemplateMock.getForEntity(uri, String.class))
        .thenReturn(ResponseEntity.badRequest().build());
    ResponseEntity<String> invalidResponse =
        authService.getPlayer(DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertTrue("Invalid get player should return 400", invalidResponse.getStatusCodeValue() == 400);
  }

  @Test
  @Order(7)
  void testValidLogout() {
    ResponseEntity<Void> validResponse =
        authService.logout(DummyAuths.validTokensInfos.get(0).getAccessToken());
    assertTrue("Valid logout should return 200", validResponse.getStatusCodeValue() == 200);
  }

  @Test
  @Order(8)
  void testInvalidLogout() {
    ResponseEntity<Void> invalidResponse =
        authService.logout(DummyAuths.invalidTokensInfos.get(0).getAccessToken());
    assertTrue("Invalid logout should also return 200",
        invalidResponse.getStatusCodeValue() == 200);
  }
}
