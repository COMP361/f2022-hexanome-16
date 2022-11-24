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
 * This class provides methods to change the password of the user.
 */
public class ChangePasswordRequest {
  private ChangePasswordRequest() {
    super();
  }

  /**
   * Sends a request to change the password of the user.
   *
   * @param accessToken The access token of the user.
   * @param user        The user for whom to change the password.
   * @param oldPassword The old password of the user.
   * @param newPassword The new password of the user.
   */
  public static void execute(String accessToken, String user, String oldPassword,
                             String newPassword) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createLobbyServiceUri(
              "/api/users/" + user,
              "access_token=" + UrlUtils.encodeUriComponent(accessToken)
          )).header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(
              new Gson().toJson(new Payload(oldPassword, newPassword))
          )).build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::statusCode).get();
      if (statusCode == 401) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
        execute(AuthUtils.getAuth().getAccessToken(), user, oldPassword, newPassword);
      }
    } catch (ExecutionException | InterruptedException e) {
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
