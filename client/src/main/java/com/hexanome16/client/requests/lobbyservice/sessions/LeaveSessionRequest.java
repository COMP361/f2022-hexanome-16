package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;

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
    RequestClient.sendRequest(new Request<>(RequestMethod.DELETE, RequestDest.LS,
        "/api/sessions/" + sessionId + "/players/" + player,
        Map.of("access_token", accessToken), Void.class));
  }
}
