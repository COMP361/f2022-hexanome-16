package com.hexanome16.client.requests.lobbyservice.user;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.sessions.User;
import java.util.Map;

/**
 * This class contains a request that registers a user in LS.
 */
public class RegisterUserRequest {
  private RegisterUserRequest() {
    super();
  }

  /**
   * Executes the request.
   *
   * @param username The username of the user.
   * @param password The password of the user.
   * @param colour  The preferred colour of the user.
   * @param role   The role of the user.
   */
  public static void execute(String username, String password, String colour, String role) {
    RequestClient.sendRequestString(new Request<>(RequestMethod.PUT, RequestDest.LS,
        "/api/users/" + username,
        Map.of("access_token", AuthUtils.getAuth().getAccessToken()),
        new User(username, password, colour, role),
        Void.class));
  }
}
