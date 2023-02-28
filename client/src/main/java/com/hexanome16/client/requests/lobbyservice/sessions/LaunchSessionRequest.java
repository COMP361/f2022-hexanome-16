package com.hexanome16.client.requests.lobbyservice.sessions;

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
import lombok.SneakyThrows;

/**
 * This class provides methods to launch a session in Lobby Service.
 */
public class LaunchSessionRequest {
  private LaunchSessionRequest() {
    super();
  }

  /**
   * Sends a request to launch a session in Lobby Service.
   *
   * @param sessionId   The id of the session to launch.
   * @param accessToken The access token of the user (must be admin or creator of the session).
   */
  @SneakyThrows
  public static void execute(long sessionId, String accessToken) {
    RequestClient.request(RequestMethod.POST, RequestDest.LS, "/api/sessions/{sessionId}")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("access_token", accessToken)
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifFailure(e -> {
          switch (e.getStatus()) {
            case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              execute(sessionId, AuthUtils.getAuth().getAccessToken());
            }
            default -> { /* Do nothing */ }
          }
        });
  }
}
