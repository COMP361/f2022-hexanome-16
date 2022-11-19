package com.hexanome16.client.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.types.user.User;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

/**
 * This class provides methods to get details about a user in Lobby Service.
 */
public class GetUserRequest {
  /**
   * Sends a request to get details about a user in Lobby Service.
   *
   * @param user        The username of the user to get details about.
   * @param accessToken The access token of the user.
   * @return The user details.
   */
  public static User execute(String user, String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/api/users/" + user,
              "access_token=" + accessToken,
              null,
              true
          )).header("Content-Type", "application/json")
          .header("Accept", "application/json")
          .GET()
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get();
      return new Gson().fromJson(response, User.class);
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }
}
