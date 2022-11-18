package com.hexanome16.client.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.auth.TokensInfo;
import com.hexanome16.client.utils.AuthHeader;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class provides methods to log in the user and get the associated OAuth tokens.
 */
public class TokenRequest {
  /**
   * Sends a request to log in the user.
   * Either username + password combo or refreshToken must be provided.
   *
   * @param username     The username of the user.
   * @param password     The password of the user.
   * @param refreshToken The refresh token of the user.
   * @return Object containing info about the tokens.
   */
  public static TokensInfo execute(String username, String password, String refreshToken) {
    HttpClient client = RequestClient.getClient();
    try {
      StringBuilder params = new StringBuilder();
      if (refreshToken == null || refreshToken.isBlank()) {
        assert username != null && !username.isBlank() && password != null && !password.isBlank();
        params.append("grant_type=password&username=").append(username)
            .append("&password=").append(password);
      } else {
        params.append("grant_type=refresh_token&refresh_token=").append(refreshToken);
      }
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/oauth/token",
              params.toString(),
              null,
              true
          )).header("Content-Type", "application/json")
          .header("Authorization", AuthHeader.getBasicHeader("bgp-client-name", "bgp-client-pw"))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
      return new Gson().fromJson(response, TokensInfo.class);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    TokensInfo response = TokenRequest.execute("testservice", "testpass", null);
    System.out.println(response);
  }
}
