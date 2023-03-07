package com.hexanome16.client.requests.backend;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.common.dto.TradePostJson;
import java.util.Map;

/**
 * Class to send request about trade post to the server.
 */
public class TradePostRequest {
  /**
   * Get reserved cards of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return a list of trade posts.
   */
  public static TradePostJson[] getTradePosts(long sessionId, String username) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/tradePost", Map.of("username", username),
        TradePostJson[].class));
  }
}
