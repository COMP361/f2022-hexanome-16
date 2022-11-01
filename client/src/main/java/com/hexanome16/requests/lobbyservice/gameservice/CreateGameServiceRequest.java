package com.hexanome16.requests.lobbyservice.gameservice;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

/**
 * This class provides methods to create a game service in Lobby Service.
 */
public class CreateGameServiceRequest {
  /**
   * Sends a request to create a game service in Lobby Service.
   *
   * @param accessToken The access token of the user (needs Service role).
   */
  public static void execute(String accessToken) {
    String url = "http://localhost:4242/api/gameservices/Splendor?access_token=" + accessToken;
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload())))
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This class represents the payload of the request (used for JSON conversion).
   */
  public static class Payload {
    String location;
    String name;
    Integer maxSessionPlayers;
    Integer minSessionPlayers;
    String displayName;
    String webSupport;

    /**
     * Default params used for testing/UI demo.
     */
    public Payload() {
      location = "http://127.0.0.1:4243/SplendorService";
      name = "Splendor";
      maxSessionPlayers = 4;
      minSessionPlayers = 2;
      displayName = "Splendor";
      webSupport = "true";
    }

    /**
     * Creates the payload with the given params.
     *
     * @param location The location of the game service.
     * @param name The name of the game service.
     * @param maxSessionPlayers The maximum number of players in a session.
     * @param minSessionPlayers The minimum number of players in a session.
     * @param displayName The display name of the game service.
     * @param webSupport Whether the game service supports web.
     */
    public Payload(String location, String name, Integer maxSessionPlayers,
                   Integer minSessionPlayers, String displayName, String webSupport) {
      this.location = location;
      this.name = name;
      this.maxSessionPlayers = maxSessionPlayers;
      this.minSessionPlayers = minSessionPlayers;
      this.displayName = displayName;
      this.webSupport = webSupport;
    }
  }
}
