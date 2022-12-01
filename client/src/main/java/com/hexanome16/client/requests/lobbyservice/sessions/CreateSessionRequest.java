package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to create a session in Lobby Service.
 */
public class CreateSessionRequest {
  private CreateSessionRequest() {
    super();
  }

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
    HttpClient client = RequestClient.getClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createLobbyServiceUri(
            "/api/sessions",
            "access_token=" + accessToken
        )).header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(
            new Gson().toJson(new Payload(creator, game, savegame))
        )).build();
    String sessionId = null;
    CompletableFuture<HttpResponse<String>> response =
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    try {
      sessionId = response.thenApply(HttpResponse::body).thenCombine(
          response.thenApply(HttpResponse::statusCode),
          (body, statusCode) -> {
            if (statusCode == 200) {
              return body;
            } else if (statusCode >= 400 && statusCode <= 403) {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              return execute(AuthUtils.getAuth().getAccessToken(), creator, game, savegame);
            } else {
              throw new RuntimeException("Unknown error");
            }
          }
      ).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return sessionId;
  }

  private static class Payload {
    /**
     * The Creator of the session.
     */
    String creator;
    /**
     * The Game of the session.
     */
    String game;
    /**
     * The Savegame of the session.
     */
    String savegame;

    /**
     * Instantiates a new Payload.
     *
     * @param creator  the creator
     * @param game     the game
     * @param savegame the savegame
     */
    public Payload(String creator, String game, String savegame) {
      this.creator = creator;
      this.game = game;
      this.savegame = savegame == null ? "" : savegame;
    }
  }
}
