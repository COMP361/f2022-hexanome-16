package com.hexanome16.client.requests.backend.cards;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * This class provides methods to perform development card related requests to the game server.
 */
public class GameRequest {

  /**
   * Updates deck.
   *
   * @param sessionId session ID.
   * @param level     level.
   * @return string representation of deck.
   */
  public static String updateDeck(long sessionId, Level level, String hash) {
    HttpClient client = RequestClient.getClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/deck",
            "level=" + level.name() + "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    String response = RequestClient.longPollAlt(request);
    return response;
  }

  /**
   * Sends request to server for any noble update.
   *
   * @param sessionId game id
   * @param hash long polling hash
   * @return noble json
   */
  public static String updateNoble(long sessionId, String hash) {
    HttpClient client = RequestClient.getClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/nobles",
            "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    String response = RequestClient.longPollAlt(request);
    return response;
  }

  /**
   * Requests current player username from server.
   *
   * @param sessionId game id
   * @param hash long polling hash
   * @return current player username
   */
  public static String updateCurrentPlayer(long sessionId, String hash) {
    HttpClient client = RequestClient.getClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/player",
            "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    String response = RequestClient.longPollAlt(request);
    return response;
  }
}
