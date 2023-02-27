package com.hexanome16.client.requests.backend.cards;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import dto.NobleJson;
import java.net.http.HttpRequest;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
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
  public static NobleJson updateDeck(long sessionId, Level level, String hash) {
    GetRequest request = (GetRequest) RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/" + sessionId + "/deck")
        .queryString("level", level.name())
        .queryString("accessToken", AuthUtils.getAuth().getAccessToken())
        .queryString("hash", hash);
    return RequestClient.longPoll(request, NobleJson.class);
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
