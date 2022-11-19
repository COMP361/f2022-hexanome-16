package com.hexanome16.client.requests.lobbyservice.oauth;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

/**
 * This class provides methods to get the role of the user.
 */
public class RoleRequest {
  /**
   * Sends a request to get the role of the user.
   *
   * @param accessToken The access token of the user.
   * @return The role of the user (ROLE_ADMIN, ROLE_PLAYER, ROLE_SERVICE).
   */
  public static String execute(String accessToken) {
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createUri(
              "/oauth/role",
              "access_token=" + accessToken,
              null,
              true
          )).GET()
          .build();
      String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
      return new Gson().fromJson(response, Response[].class)[0].authority;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    System.out.println(RoleRequest.execute("CtOq1Zt62iOclaikOcXU7zdfuV8="));
  }

  private static class Response {
    String authority;
  }
}