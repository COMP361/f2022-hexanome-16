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
  @SneakyThrows
  public static void execute(String accessToken, String user, String colour) {
    RequestClient.request(RequestMethod.POST, RequestDest.LS, "/api/users/{user}/colour")
        .routeParam("user", user)
        .queryString("access_token", accessToken)
        .body(new JSONObject().put("colour", colour).toString())
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifFailure(e -> {
          switch (e.getStatus()) {
            case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              execute(AuthUtils.getAuth().getAccessToken(), user, colour);
            }
            default -> { /* Do nothing */ }
          }
        });
  }
}
