package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import dto.SessionJson;
import java.util.Map;

/**
 * This class provides methods to create a session in Lobby Service.
 */
public class CreateSessionRequest {
  private CreateSessionRequest() {
    super();
  }

  /**
   * Sends a request to create a session in Lobby Service.
   *
   * @param accessToken The access token of the user.
   * @param creator     The creator of the session.
   * @param game        The game server associated with the session.
   * @param savegame    The savegame associated with the session (can be empty).
   * @return The id of the created session.
   */
  public static Long execute(String accessToken, String creator, String game, String savegame) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/sessions", Map.of("access_token", accessToken),
        new SessionJson(null, creator, savegame, game), Long.class));
  }
}
