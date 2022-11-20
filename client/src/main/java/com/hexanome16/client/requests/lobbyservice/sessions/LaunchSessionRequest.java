package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to launch a session in Lobby Service.
 */
public class LaunchSessionRequest {
  /**
   * Sends a request to launch a session in Lobby Service.
   *
   * @param sessionId   The id of the session to launch.
   * @param accessToken The access token of the user (must be admin or creator of the session).
   */
  public static void execute(long sessionId, String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/api/sessions/" + sessionId,
              "access_token=" + accessToken
          )).POST(HttpRequest.BodyPublishers.noBody())
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.discarding()).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
