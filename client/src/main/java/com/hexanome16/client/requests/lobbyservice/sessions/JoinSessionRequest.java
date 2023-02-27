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
 * This class provides methods to join a session in Lobby Service.
 */
public class JoinSessionRequest {
  private JoinSessionRequest() {
    super();
  }

  /**
   * Sends a request to join a session in Lobby Service.
   *
   * @param sessionId   The id of the session to join.
   * @param player      The user to add to the session.
   * @param accessToken The access token of the user.
   */
  @SneakyThrows
  public static void execute(long sessionId, String player, String accessToken) {
    RequestClient.request(
            RequestMethod.PUT, RequestDest.LS, "/api/sessions/{sessionId}/players/{player}")
        .routeParam("sessionId", String.valueOf(sessionId))
        .routeParam("player", player)
        .queryString("access_token", accessToken)
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifFailure(e -> {
          switch (e.getStatus()) {
            case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              execute(sessionId, player, AuthUtils.getAuth().getAccessToken());
            }
            default -> { /* Do nothing */ }
          }
        });
  }
}
