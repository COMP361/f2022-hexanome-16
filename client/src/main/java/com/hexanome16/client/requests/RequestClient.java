package com.hexanome16.client.requests;

import com.hexanome16.client.requests.lobbyservice.oauth.AuthRequest;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class provides an HTTP client to send requests to the backend/Lobby Service.
 */
public class RequestClient {

  private static final HttpClient client = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofDays(1))
      .build();

  private RequestClient() {
    super();
  }

  /**
   * Gets client.
   *
   * @return the client
   */
  public static HttpClient getClient() {
    return client;
  }

  /**
   * Sends a request using long polling.
   *
   * @param request The request to send.
   * @return (response hash code, response body as string) pair
   */
  public static Pair<String, String> longPoll(HttpRequest request) {
    String response = "";
    AtomicInteger returnCode = new AtomicInteger(408);
    while (returnCode.get() == 408) {
      try {
        CompletableFuture<HttpResponse<String>> res =
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response = res.thenApply(HttpResponse::statusCode)
            .thenCombine(res.thenApply(HttpResponse::body), (statusCode, body) -> {
              if (statusCode == 200) {
                returnCode.set(200);
              }
              return body;
            }).get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        System.out.println("Interrupted long polling");
      }
      if (returnCode.get() >= 400 && returnCode.get() <= 403) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
      }
    }
    return new Pair<>(DigestUtils.md5Hex(response), response);
  }

  /**
   * Long polling but no need to hash result.
   *
   * @param request http request to send
   * @return response as string
   */
  public static String longPollAlt(HttpRequest request) {
    String response = "";
    AtomicInteger returnCode = new AtomicInteger(408);
    while (returnCode.get() == 408) {
      try {
        CompletableFuture<HttpResponse<String>> res =
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response = res.thenApply(HttpResponse::statusCode)
            .thenCombine(res.thenApply(HttpResponse::body), (statusCode, body) -> {
              if (statusCode == 200) {
                returnCode.set(200);
              }
              return body;
            }).get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        System.out.println("Interrupted long polling");
      }
      if (returnCode.get() >= 400 && returnCode.get() <= 403) {
        TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
      }
    }
    return response;
  }
}
