package com.hexanome16.client.requests.lobbyservice.oauth;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to log out the user.
 */
public class LogoutRequest {
  private LogoutRequest() {
    super();
  }

  /**
   * Sends a request to log out the user and erase user token information.
   */
  public static void execute() {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createLobbyServiceUri(
              "/oauth/active",
              "access_token=" + AuthUtils.getAuth().getAccessToken()
          )).DELETE()
          .build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::statusCode).get();
      if (statusCode == 200) {
        AuthUtils.setAuth(null);
        AuthUtils.setPlayer(null);
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      AuthUtils.setAuth(null);
      AuthUtils.setPlayer(null);
    }
  }
}
