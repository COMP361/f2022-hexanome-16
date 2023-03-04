package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;

/**
 * This class provides methods to delete a session in Lobby Service.
 */
public class DeleteSessionRequest {
  private DeleteSessionRequest() {
    super();
  }

  /**
   * Sends a request to delete a session in Lobby Service.
   *
   * @param sessionId   The id of the session to delete.
   * @param accessToken The access token of the user (must be admin or creator of the session).
   */
  public static void execute(long sessionId, String accessToken) {
    RequestClient.sendRequest(new Request<>(RequestMethod.DELETE, RequestDest.LS,
        "/api/sessions/" + sessionId, Map.of("access_token", accessToken), null, Void.class));
  }
}
