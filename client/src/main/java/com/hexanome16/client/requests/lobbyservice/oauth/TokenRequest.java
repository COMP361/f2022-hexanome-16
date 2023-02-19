package com.hexanome16.client.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
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
   * @param username     The username of the user.
   * @param password     The password of the user.
   * @param refreshToken The refresh token of the user.
   */
  private static void execute(String username, String password, String refreshToken) {
    HttpClient client = RequestClient.getClient();
    try {
      StringBuilder params = new StringBuilder();
      if (refreshToken == null || refreshToken.isBlank()) {
        params.append("grant_type=password&username=").append(username)
            .append("&password=").append(password);
      } else {
        params.append("grant_type=refresh_token&refresh_token=")
            .append(refreshToken);
      }
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createLobbyServiceUri(
              "/oauth/token",
              params.toString()
          )).header("Content-Type", "application/json")
          .header("Authorization", AuthUtils.getBasicHeader("bgp-client-name", "bgp-client-pw"))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get();
      AuthUtils.setAuth(new Gson().fromJson(response, TokensInfo.class));
      if (AuthUtils.getAuth().getAccessToken() == null) {
        AuthUtils.setAuth(null);
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      AuthUtils.setAuth(null);
    }
  }

  /**
   * Refreshes user's login via refresh token.
   *
   * @param refreshToken The refresh token of the user.
   */
  public static void execute(String refreshToken) {
    execute(null, null, refreshToken);
  }

  /**
   * Logs in the user via Lobby Service with username/password.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   */
  public static void execute(String username, String password) {
    assert username != null && !username.isBlank() && password != null && !password.isBlank();
    execute(username, password, null);
  }
}
