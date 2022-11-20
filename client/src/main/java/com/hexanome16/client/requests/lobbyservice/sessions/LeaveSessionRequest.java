package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to leave a session in Lobby Service.
 */
public class LeaveSessionRequest {
  /**
   * Sends a request to leave a session in Lobby Service.
   *
   * @param sessionId   The id of the session to leave.
   * @param player      The user to remove from the session (must exist in the session).
   * @param accessToken The access token of the user.
   */
  public static void execute(long sessionId, String player, String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/api/sessions/" + sessionId + "/players/" + player,
              "access_token=" + accessToken
          )).DELETE()
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.discarding()).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
