package com.hexanome16.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import com.hexanome16.types.lobby.auth.TokensInfo;
import com.hexanome16.utils.AuthHeader;
import com.hexanome16.utils.StringConverter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TokenRequest {

  public static TokensInfo execute(String username, String password, String refresh_token) {
    HttpClient client = RequestClient.getClient();
    try {
      StringBuilder url = new StringBuilder("http://localhost:4242/oauth/token?");
      if (refresh_token == null || refresh_token.isBlank()) {
        url.append("grant_type=password&username=").append(StringConverter.escape(username))
            .append("&password=").append(StringConverter.escape(password));
      } else {
        url.append("grant_type=refresh_token&refresh_token=").append(refresh_token);
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
      tokensInfo.setAccess_token(StringConverter.escape(tokensInfo.access_token()));
      tokensInfo.setRefresh_token(StringConverter.escape(tokensInfo.refresh_token()));
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
