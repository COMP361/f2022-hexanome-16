package com.hexanome16.client.requests.lobbyservice.sessions;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import dto.SessionJson;
import java.util.Map;
import javafx.util.Pair;

/**
 * This class provides methods to get details about a session in Lobby Service.
 */
public class SessionDetailsRequest {
  private SessionDetailsRequest() {
    super();
  }

  /**
   * Sends a request to get details about a session in Lobby Service.
   *
   * @param sessionId The id of the session to get details about.
   * @param hash      A hashcode used for long polling (check session details have changed)
   * @return The session details.
   */
  public static Pair<String, SessionJson> execute(long sessionId, String hash) {
    return RequestClient.longPollWithHash(new Request<>(RequestMethod.GET, RequestDest.LS,
        "/api/sessions/" + sessionId, Map.of("hash", hash), SessionJson.class));
  }
}
