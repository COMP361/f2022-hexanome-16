package com.hexanome16.client.requests.lobbyservice.oauth;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;

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
  @SneakyThrows
  public static String execute(String accessToken) {
    AtomicReference<String> role = new AtomicReference<>();
    RequestClient.request(RequestMethod.GET, RequestDest.LS, "/oauth/role")
        .queryString("access_token", accessToken)
        .asJsonAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(response -> role.set(response.getBody().getObject().getString("authority")))
        .ifFailure(throwable -> role.set(null));
    return role.get();
  }
}
