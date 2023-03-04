package com.hexanome16.client.requests.lobbyservice.oauth;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import java.util.Map;

/**
 * This class provides methods to log out the user.
 */
public class LogoutRequest {
  private LogoutRequest() {
    super();
  }

  /**
   * Sends a request to log out the user and erase user token information.
   */
  public static void execute() {
    RequestClient.sendRequest(new Request<>(RequestMethod.DELETE, RequestDest.LS, "/oauth/active",
        Map.of("access_token", AuthUtils.getAuth().getAccessToken()), Void.class));
  }
}
