package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.sessions.Session;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to get details about a session in Lobby Service.
 */
public class SessionDetailsRequest {
  /**
   * Sends a request to get details about a session in Lobby Service.
   *
   * @param sessionId The id of the session to get details about.
   * @param hash A hashcode used for long polling (to check if the session details have changed).
   * @return The session details.
   */
  public static Session execute(long sessionId, int hash) {
    HttpClient client = RequestClient.getClient();
    try {
      String url = "http://127.0.0.1:4242/api/sessions/" + sessionId;
      if (hash > 0) {
        url += "?hash=" + hash;
      }
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Content-Type", "application/json")
          .GET()
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get();
      return new Gson().fromJson(response, Session.class);
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }
}
