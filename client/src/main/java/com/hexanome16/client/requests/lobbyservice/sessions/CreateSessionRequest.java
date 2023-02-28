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
import dto.SessionJson;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

/**
 * This class provides methods to create a session in Lobby Service.
 */
public class CreateSessionRequest {
  private CreateSessionRequest() {
    super();
  }

  /**
   * Sends a request to create a session in Lobby Service.
   *
   * @param accessToken The access token of the user.
   * @param creator     The creator of the session.
   * @param gameServer  The game server associated with the session.
   * @param savegame    The savegame associated with the session (can be empty).
   * @return The id of the created session.
   */
  @SneakyThrows
  public static Long execute(String accessToken, String creator, String gameServer,
                             String savegame) {
    AtomicLong sessionId = new AtomicLong();
    RequestClient.request(RequestMethod.POST, RequestDest.LS, "/api/sessions")
        .queryString("access_token", accessToken)
        .body(new SessionJson(null, creator, savegame, gameServer))
        .asStringAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(response -> sessionId.set(Long.parseLong(response.getBody()))).ifFailure(e -> {
          switch (e.getStatus()) {
            case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
              TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
              sessionId.set(
                  execute(AuthUtils.getAuth().getAccessToken(), creator, gameServer, savegame));
            }
            default -> { /* Do nothing */ }
          }
        });
    return sessionId.get();
  }
}
