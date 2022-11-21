package com.hexanome16.client.requests.lobbyservice.sessions;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.sessions.Session;
import com.hexanome16.client.utils.UrlUtils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
  public static Session[] execute(int hash) {
    HttpClient client = RequestClient.getClient();
    URI uri = UrlUtils.createLobbyServiceUri(
        "/api/sessions",
        hash != 0 ? "hash=" + hash : null
    );
    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri).header("Content-Type", "application/json")
        .GET()
        .build();
    String response = null;
    AtomicInteger returnCode = new AtomicInteger(408);
    while (returnCode.get() == 408) {
      try {
        Thread.sleep(10);
        CompletableFuture<HttpResponse<String>> res =
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response = res.thenApply(HttpResponse::statusCode)
            .thenCombine(res.thenApply(HttpResponse::body), (statusCode, body) -> {
              if (statusCode == 200) {
                returnCode.set(200);
              }
              return body;
            }).get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    Map<String, Session> sessions = new Gson().fromJson(response, Response.class).sessions;
    return sessions.entrySet().stream().map(entry -> {
      entry.getValue().setId(Long.valueOf(entry.getKey()));
      return entry.getValue();
    }).toArray(Session[]::new);
  }

  private static class Response {
    public Map<String, Session> sessions;
  }
}
