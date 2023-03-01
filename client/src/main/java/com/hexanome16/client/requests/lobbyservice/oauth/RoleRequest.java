package com.hexanome16.client.requests.lobbyservice.oauth;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;
import kong.unirest.JsonNode;

/**
 * This class provides methods to get the role of the user.
 */
public class RoleRequest {
  private RoleRequest() {
    super();
  }

  /**
   * Sends a request to get the role of the user.
   *
   * @param accessToken The access token of the user.
   * @return The role of the user (ROLE_ADMIN, ROLE_PLAYER, ROLE_SERVICE).
   */
  public static String execute(String accessToken) {
    JsonNode response = RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.LS,
        "/api/users/role", Map.of("access_token", accessToken), JsonNode.class));
    return response == null ? null : response.getObject().getString("authority");
  }
}
