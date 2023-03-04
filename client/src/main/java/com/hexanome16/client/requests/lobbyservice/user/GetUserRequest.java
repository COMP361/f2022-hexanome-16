package com.hexanome16.client.requests.lobbyservice.user;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.sessions.User;
import java.util.Map;

/**
 * This class provides methods to get details about a user in Lobby Service.
 */
public class GetUserRequest {
  private GetUserRequest() {
    super();
  }

  /**
   * Sends a request and sets details about a user in Lobby Service.
   *
   * @param user        The username of the user to get details about.
   * @param accessToken The access token of the user.
   */
  public static void execute(String user, String accessToken) {
    AuthUtils.setPlayer(RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.LS,
        "/api/users/" + user, Map.of("access_token", accessToken), User.class)));
  }
}
