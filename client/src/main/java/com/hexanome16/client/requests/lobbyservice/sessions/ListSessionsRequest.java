package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.common.models.sessions.Session;
import java.util.Map;
import javafx.util.Pair;

/**
 * This class provides methods to list sessions in Lobby Service.
 */
public class ListSessionsRequest {
  private ListSessionsRequest() {
    super();
  }

  /**
   * Sends a request to list sessions in Lobby Service.
   *
   * @param hash A hashcode used for long polling (to see if there are changes to the list).
   * @return An array of sessions in Lobby Service.
   */
  public static Pair<String, Map<String, Session>> execute(String hash) {
    Pair<String, Response> sessionMap = RequestClient.longPollWithHash(new Request<>(
        RequestMethod.GET, RequestDest.LS, "/api/sessions", Map.of("hash", hash), Response.class));
    if (sessionMap.getValue() == null || sessionMap.getValue().sessions == null) {
      return new Pair<>("", null);
    }
    return new Pair<>(sessionMap.getKey(), sessionMap.getValue().sessions);
  }

  private static class Response {
    /**
     * Map of sessions.
     */
    public Map<String, Session> sessions;
  }
}
