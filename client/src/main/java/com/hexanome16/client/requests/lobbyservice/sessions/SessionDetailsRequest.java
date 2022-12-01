package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.sessions.Session;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpRequest;
import javafx.util.Pair;

/**
 * This class provides methods to get details about a session in Lobby Service.
 */
public class SessionDetailsRequest {
  private SessionDetailsRequest() {
    super();
  }

  /**
   * Sends a request to get details about a session in Lobby Service.
   *
   * @param sessionId The id of the session to get details about.
   * @param hash      A hashcode used for long polling (check session details have changed)
   * @return The session details.
   */
  public static Pair<String, Session> execute(long sessionId, String hash) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createLobbyServiceUri(
            "/api/sessions" + sessionId,
            hash == null || hash.isBlank() ? null : "hash=" + hash
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    Pair<String, String> response = RequestClient.longPoll(request);
    return new Pair<>(response.getKey(), new Gson().fromJson(response.getValue(), Session.class));
  }
}
