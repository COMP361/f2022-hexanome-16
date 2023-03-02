package com.hexanome16.client.requests.lobbyservice.user;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;
import kong.unirest.core.json.JSONObject;

/**
 * This class provides methods to change the colour of the user.
 */
public class ChangeColourRequest {
  private ChangeColourRequest() {
    super();
  }

  /**
   * Sends a request to change the colour of the user.
   *
   * @param accessToken The access token of the user.
   * @param user        The user for whom to change the colour.
   * @param colour      The new colour of the user.
   */
  public static void execute(String accessToken, String user, String colour) {
    RequestClient.sendRequest(new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/users/" + user + "/colour", Map.of("access_token", accessToken),
        new JSONObject().put("colour", colour).toString(), Void.class));
  }
}
