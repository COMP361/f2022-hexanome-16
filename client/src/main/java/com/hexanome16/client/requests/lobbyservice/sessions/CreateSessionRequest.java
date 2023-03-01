package com.hexanome16.client.requests.lobbyservice.sessions;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import dto.SessionJson;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kong.unirest.Unirest;

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
   * @param game        The game server associated with the session.
   * @param savegame    The savegame associated with the session (can be empty).
   * @return The id of the created session.
   */
  public static Long execute(String accessToken, String creator, String game, String savegame) {
    /*Request<Long> request = new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/sessions", Map.of("access_token", accessToken),
        new SessionJson(null, creator, savegame, game), Long.class);
    AtomicReference<String> result = new AtomicReference<>();
    try {
      Unirest.request(request.getMethod().name(),
              request.getDest().getUrl() + request.getPath())
          .header("Content-Type", "application/json")
          .queryString("access_token", accessToken)
          .body(request.getBody())
          .asStringAsync()
          .get(TIMEOUT, TimeUnit.SECONDS)
          .ifSuccess(response -> result.set(response.getBody()))
          .ifFailure(response -> {
            throw new RuntimeException("Unknown error");
          });
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new RuntimeException(e);
    }
    return Long.parseLong(result.get());*/
    return RequestClient.sendRequest(new Request<>(RequestMethod.POST, RequestDest.LS,
        "/api/sessions", Map.of("access_token", accessToken),
        new SessionJson(null, creator, savegame, game), Long.class));
  }
}
