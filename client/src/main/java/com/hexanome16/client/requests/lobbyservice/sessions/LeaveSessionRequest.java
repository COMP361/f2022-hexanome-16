package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to leave a session in Lobby Service.
 */
public class LeaveSessionRequest {
  private LeaveSessionRequest() {
    super();
  }

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
          .uri(UrlUtils.createLobbyServiceUri(
              "/api/sessions/" + sessionId + "/players/" + player,
              "access_token=" + UrlUtils.encodeUriComponent(accessToken)
          )).DELETE()
          .build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
          .thenApply(HttpResponse::statusCode)
          .get();
      if (statusCode == 401) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
        execute(sessionId, player, AuthUtils.getAuth().getAccessToken());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
