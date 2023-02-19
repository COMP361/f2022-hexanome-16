package com.hexanome16.client.requests.backend.cards;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpRequest;
import models.Level;

/**
 * This class provides methods to perform development card related requests to the game server.
 */
public class GameRequest {

  /**
   * Updates deck.
   *
   * @param sessionId session ID.
   * @param level     level.
   * @param hash      the hash
   * @return string representation of deck.
   */
  public static String updateDeck(long sessionId, Level level, String hash) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/deck",
            "level=" + level.name() + "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    return RequestClient.longPoll(request);
  }

  /**
   * Sends request to server for any noble update.
   *
   * @param sessionId game id
   * @param hash      long polling hash
   * @return noble json
   */
  public static String updateNoble(long sessionId, String hash) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/nobles",
            "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    return RequestClient.longPoll(request);
  }

  /**
   * Requests current player username from server.
   *
   * @param sessionId game id
   * @param hash      long polling hash
   * @return current player username
   */
  public static String updateCurrentPlayer(long sessionId, String hash) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/player",
            "&accessToken=" + AuthUtils.getAuth().getAccessToken()
                + "&hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    return RequestClient.longPoll(request);
  }
}
