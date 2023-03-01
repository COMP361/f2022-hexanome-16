package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;

/**
 * This class provides methods to launch a session in Lobby Service.
 */
public class LaunchSessionRequest {
  private LaunchSessionRequest() {
    super();
  }

  /**
   * Sends a request to launch a session in Lobby Service.
   *
   * @param sessionId   The id of the session to launch.
   * @param accessToken The access token of the user (must be admin or creator of the session).
   */
  public static void execute(long sessionId, String accessToken) {
    RequestClient.sendRequest(new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/sessions/" + sessionId, Map.of("access_token", accessToken), Void.class));
  }
}
