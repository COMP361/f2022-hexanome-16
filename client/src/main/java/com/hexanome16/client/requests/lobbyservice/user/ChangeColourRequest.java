package com.hexanome16.client.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createLobbyServiceUri(
              "/api/users/" + user + "/colour",
              "access_token=" + accessToken
          )).header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload(colour))))
          .build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::statusCode).get();
      if (statusCode == 200) {
        AuthUtils.getPlayer().setPreferredColour(colour);
      } else if (statusCode == 401) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
        execute(AuthUtils.getAuth().getAccessToken(), user, colour);
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class Payload {
    String colour;

    public Payload(String colour) {
      this.colour = colour;
    }
  }
}
