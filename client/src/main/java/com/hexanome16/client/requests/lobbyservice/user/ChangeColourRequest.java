package com.hexanome16.client.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
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
          .uri(UrlUtils.createUri(
              "/api/users/" + user + "/colour",
              "access_token=" + accessToken,
              null,
              true
          )).header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload(colour))))
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
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
