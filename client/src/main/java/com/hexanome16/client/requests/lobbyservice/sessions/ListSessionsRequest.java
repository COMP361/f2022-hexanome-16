package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.sessions.Session;
import com.hexanome16.client.utils.UrlUtils;
import java.net.URI;
import java.net.http.HttpRequest;
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
  public static Pair<String, Session[]> execute(String hash) {
    URI uri = UrlUtils.createLobbyServiceUri(
        "/api/sessions",
        hash == null || hash.isBlank() ? null : "hash=" + hash
    );
    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .header("Content-Type", "application/json")
        .GET()
        .build();
    Pair<String, String> response = RequestClient.longPoll(request);
    Response res = new Gson().fromJson(response.getValue(), Response.class);
    if (res == null || res.sessions == null) {
      return new Pair<>("", null);
    }
    Map<String, Session> sessions = res.sessions;
    return new Pair<>(response.getKey(), sessions.entrySet().stream().map(entry -> {
      entry.getValue().setId(Long.valueOf(entry.getKey()));
      return entry.getValue();
    }).toArray(Session[]::new));
  }

  private static class Response {
    /**
     * Map of sessions.
     */
    public Map<String, Session> sessions;
  }
}
