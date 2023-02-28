package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Map;
import javafx.util.Pair;
import models.sessions.Session;

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
  public static Pair<String, Session[]> execute(String hash) {
    Pair<String, Response> sessionMap = RequestClient.longPollWithHash(
        RequestClient.request(RequestMethod.GET, RequestDest.LS, "/api/sessions")
            .queryString("hash", hash), Response.class);
    if (sessionMap.getValue() == null || sessionMap.getValue().sessions == null) {
      return new Pair<>("", null);
    }
    Map<String, Session> sessions = sessionMap.getValue().sessions;
    return new Pair<>(sessionMap.getKey(), sessions.entrySet().stream().map(entry -> {
      Session session = entry.getValue();
      return new Session(Long.valueOf(entry.getKey()), session.getCreator(),
          session.getGameParameters(), session.isLaunched(), session.getPlayers(),
          session.getSaveGameId());
    }).toArray(Session[]::new));
  }

  private static class Response implements BroadcastContent {
    /**
     * Map of sessions.
     */
    public Map<String, Session> sessions;

    @Override
    public boolean isEmpty() {
      return sessions == null || sessions.isEmpty();
    }
  }
}
