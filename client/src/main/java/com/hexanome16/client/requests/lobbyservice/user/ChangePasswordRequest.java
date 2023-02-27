package com.hexanome16.client.requests.lobbyservice.user;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import java.util.concurrent.TimeUnit;
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
    RequestClient.request(RequestMethod.POST, RequestDest.LS, "/api/users/{user}")
        .routeParam("user", user)
        .queryString("access_token", accessToken)
        .body(new JSONObject().put("oldPassword", oldPassword)
            .put("nextPassword", newPassword).toString())
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifFailure(e -> {
          switch (e.getStatus()) {
            case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              execute(AuthUtils.getAuth().getAccessToken(), user, oldPassword, newPassword);
            }
            default -> { /* Do nothing */ }
          }
        });
  }
}
