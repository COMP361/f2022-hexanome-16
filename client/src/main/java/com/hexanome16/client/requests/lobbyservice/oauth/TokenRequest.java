package com.hexanome16.client.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.auth.TokensInfo;
import com.hexanome16.client.utils.AuthHeader;
import com.hexanome16.client.utils.StringConverter;
import java.net.URI;
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
   * @param username The username of the user.
   * @param password The password of the user.
   * @param refreshToken The refresh token of the user.
   * @return Object containing info about the tokens.
   */
  public static TokensInfo execute(String username, String password, String refreshToken) {
    HttpClient client = RequestClient.getClient();
    try {
      StringBuilder url = new StringBuilder("http://localhost:4242/oauth/token?");
      if (refreshToken == null || refreshToken.isBlank()) {
        url.append("grant_type=password&username=").append(StringConverter.escape(username))
            .append("&password=").append(StringConverter.escape(password));
      } else {
        url.append("grant_type=refresh_token&refresh_token=").append(refreshToken);
      }
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url.toString()))
          .header("Content-Type", "application/json")
          .header("Authorization", AuthHeader.getBasicHeader("bgp-client-name", "bgp-client-pw"))
          .POST(HttpRequest.BodyPublishers.noBody())
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
      TokensInfo tokensInfo = new Gson().fromJson(response, TokensInfo.class);
      tokensInfo.setAccessToken(StringConverter.escape(tokensInfo.access_token()));
      tokensInfo.setRefreshToken(StringConverter.escape(tokensInfo.refresh_token()));
      return tokensInfo;
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
