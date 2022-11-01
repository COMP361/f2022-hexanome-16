package com.hexanome16.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import com.hexanome16.types.lobby.user.User;
import java.net.URI;
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
   * @param player The username of the user to get details about.
   * @param accessToken The access token of the user.
   * @return The user details.
   */
  public static User execute(String player, String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      String url = "http://127.0.0.1:4242/api/users/" + player + "?access_token=" + accessToken;
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Content-Type", "application/json")
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
