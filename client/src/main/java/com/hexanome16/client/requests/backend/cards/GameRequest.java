package com.hexanome16.client.requests.backend.cards;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.dto.PlayerJson;
import com.hexanome16.common.models.Level;
import java.util.Map;
import javafx.util.Pair;

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
  public static Pair<String, DeckJson> updateDeck(long sessionId, Level level, String hash) {
    return RequestClient.longPollWithHash(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/deck", Map.of("level", level.name(),
        "access_token", AuthUtils.getAuth().getAccessToken(), "hash", hash), DeckJson.class));
  }

  /**
   * Sends request to server for any noble update.
   *
   * @param sessionId game id
   * @param hash      long polling hash
   * @return noble json
   */
  public static Pair<String, NobleDeckJson> updateNoble(long sessionId, String hash) {
    return RequestClient.longPollWithHash(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/nobles", Map.of(
        "access_token", AuthUtils.getAuth().getAccessToken(), "hash", hash),
        NobleDeckJson.class));
  }

  /**
   * Requests current player username from server.
   *
   * @param sessionId game id
   * @param hash      long polling hash
   * @return current player username
   */
  public static Pair<String, PlayerJson> updateCurrentPlayer(long sessionId, String hash) {
    return RequestClient.longPollWithHash(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/player", Map.of(
        "access_token", AuthUtils.getAuth().getAccessToken(), "hash", hash),
        PlayerJson.class));
  }
}
