package com.hexanome16.client.requests.lobbyservice.oauth;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;
import static com.hexanome16.client.requests.RequestClient.mapObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import models.auth.TokensInfo;

/**
 * This class provides methods to log in the user and get the associated OAuth tokens.
 */
public class TokenRequest {
  private TokenRequest() {
    super();
  }

  /**
   * Sends a request to log in the user and sets global user token information.
   *
   * @param params The parameters of the request.
   */
  @SneakyThrows
  private static void execute(Map<String, Object> params) {
    RequestClient.request(RequestMethod.POST, RequestDest.LS, "/oauth/token")
        .queryString(params)
        .basicAuth("bgp-client-name", "bgp-client-pw")
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), TokensInfo.class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(response -> {
          System.out.println(response.getParsingError());
          AuthUtils.setAuth(response.getBody());
        })
        .ifFailure(response -> AuthUtils.setAuth(null));
  }

  /**
   * Refreshes user's login via refresh token.
   *
   * @param refreshToken The refresh token of the user.
   */
  public static void execute(String refreshToken) {
    assert refreshToken != null && !refreshToken.isBlank();
    execute(Map.of("grant_type", "refresh_token", "refresh_token", refreshToken));
  }

  /**
   * Logs in the user via Lobby Service with username/password.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   */
  public static void execute(String username, String password) {
    assert username != null && !username.isBlank() && password != null && !password.isBlank();
    execute(Map.of("grant_type", "password", "username", username, "password", password));
  }
}
