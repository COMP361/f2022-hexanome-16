package com.hexanome16.requests.lobbyservice.user;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class provides methods to change the password of the user.
 */
public class ChangePasswordRequest {
  /**
   * Sends a request to change the password of the user.
   *
   * @param accessToken The access token of the user.
   * @param user The user for whom to change the password.
   * @param oldPassword The old password of the user.
   * @param newPassword The new password of the user.
   */
  public static void execute(String accessToken, String user, String oldPassword,
                             String newPassword) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(
              "http://127.0.0.1:4242/api/users/" + user + "?access_token=" + accessToken))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(
              new Gson().toJson(new Payload(oldPassword, newPassword))))
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  private static class Payload {
    String oldPassword;
    String nextPassword;

    public Payload(String oldPassword, String newPassword) {
      this.oldPassword = oldPassword;
      this.nextPassword = newPassword;
    }
  }
}
