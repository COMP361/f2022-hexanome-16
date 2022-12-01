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
              "access_token=" + accessToken
          )).header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(
              new Gson().toJson(new Payload(oldPassword, newPassword))
          )).build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::statusCode).get();
      if (statusCode >= 400 && statusCode <= 403) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
        execute(AuthUtils.getAuth().getAccessToken(), user, oldPassword, newPassword);
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class Payload {
    /**
     * The player's old password.
     */
    String oldPassword;
    /**
     * The player's new password.
     */
    String nextPassword;

    /**
     * Instantiates a new Payload.
     *
     * @param oldPassword the old password
     * @param newPassword the new password
     */
    public Payload(String oldPassword, String newPassword) {
      this.oldPassword = oldPassword;
      this.nextPassword = newPassword;
    }
  }
}
