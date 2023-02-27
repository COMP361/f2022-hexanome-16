package com.hexanome16.client.requests.lobbyservice.oauth;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

/**
 * This class provides methods to log out the user.
 */
public class LogoutRequest {
  private LogoutRequest() {
    super();
  }

  /**
   * Sends a request to log out the user and erase user token information.
   */
  @SneakyThrows
  public static void execute() {
    RequestClient.request(RequestMethod.DELETE, RequestDest.LS, "/oauth/active")
        .queryString("access_token", AuthUtils.getAuth().getAccessToken())
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(response -> {
          AuthUtils.setAuth(null);
          AuthUtils.setPlayer(null);
        }).ifFailure(throwable -> {
          AuthUtils.setAuth(null);
          AuthUtils.setPlayer(null);
        });
  }
}
