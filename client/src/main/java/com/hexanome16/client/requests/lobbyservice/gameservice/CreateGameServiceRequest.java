package com.hexanome16.client.requests.lobbyservice.gameservice;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.sessions.GameParams;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

/**
 * This class provides methods to create a game service in Lobby Service.
 */
public class CreateGameServiceRequest {
  private CreateGameServiceRequest() {
    super();
  }

  /**
   * Sends a request to create a game service in Lobby Service.
   *
   * @param accessToken The access token of the user (needs Service role).
   */
  public static void execute(String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createLobbyServiceUri(
              "/api/gameservices/Splendor",
              "access_token=" + accessToken
          )).header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new GameParams())))
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
