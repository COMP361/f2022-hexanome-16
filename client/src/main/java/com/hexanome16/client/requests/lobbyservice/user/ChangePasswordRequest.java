package com.hexanome16.client.requests.lobbyservice.user;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.Map;
import kong.unirest.json.JSONObject;
import lombok.SneakyThrows;

/**
 * This class provides methods to change the password of the user.
 */
public class ChangePasswordRequest {
  private ChangePasswordRequest() {
    super();
  }

  /**
   * Sends a request to change the password of the user.
   *
   * @param accessToken The access token of the user.
   * @param user        The user for whom to change the password.
   * @param oldPassword The old password of the user.
   * @param newPassword The new password of the user.
   */
  @SneakyThrows
  public static void execute(String accessToken, String user, String oldPassword,
                             String newPassword) {
    RequestClient.sendRequest(new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/users/" + user, Map.of("access_token", accessToken),
        new JSONObject().put("oldPassword", oldPassword).put("nextPassword", newPassword)
            .toString(), Void.class));
  }
}
