package com.hexanome16.client.requests.lobbyservice.user;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import models.sessions.User;

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
  @SneakyThrows
  public static void execute(String user, String accessToken) {
    RequestClient.request(RequestMethod.GET, RequestDest.LS, "/api/users/{user}")
        .routeParam("user", user)
        .queryString("access_token", accessToken)
        .asObjectAsync(User.class)
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(response -> AuthUtils.setPlayer(response.getBody()))
        .ifFailure(e -> AuthUtils.setPlayer(null));
  }
}
