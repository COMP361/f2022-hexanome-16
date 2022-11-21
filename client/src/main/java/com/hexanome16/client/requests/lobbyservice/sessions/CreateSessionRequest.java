package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class provides methods to create a session in Lobby Service.
 */
public class CreateSessionRequest {
  /**
   * Sends a request to create a session in Lobby Service.
   *
   * @param accessToken The access token of the user.
   * @param creator     The creator of the session.
   * @param game        The game service associated with the session.
   * @param savegame    The savegame associated with the session (can be empty).
   * @return The id of the created session.
   */
  public static String execute(String accessToken, String creator, String game, String savegame) {
    if (savegame == null) {
      savegame = "";
    }
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/api/sessions",
              "access_token=" + accessToken,
              null,
              true
          )).header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(
              new Gson().toJson(new Payload(creator, game, savegame))
          )).build();
      return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static class Payload {
    String creator;
    String game;
    String savegame;

    public Payload(String creator, String game, String savegame) {
      this.creator = creator;
      this.game = game;
      this.savegame = savegame;
    }
  }
}
