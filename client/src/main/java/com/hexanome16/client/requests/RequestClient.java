package com.hexanome16.client.requests;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.util.Pair;
import kong.unirest.HttpMethod;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class provides an HTTP client to send requests to the backend/Lobby Service.
 */
public class RequestClient {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  public static final int TIMEOUT = 60;

  private RequestClient() {
    super();
    Unirest.config()
        .setDefaultHeader("Accept", "application/json")
        .connectTimeout(TIMEOUT * 1000)
        .setDefaultResponseEncoding("UTF-8")
        .addShutdownHook(true);
  }

  /**
   * Maps a string to an object of the given class.
   *
   * @param reader input reader
   * @param classT class of the object
   * @param <T> type of the object
   * @return object of the given class
   */
  @SneakyThrows
  public static <T> T mapObject(InputStreamReader reader, Class<T> classT) {
    return objectMapper.readValue(reader, classT);
  }

  /**
   * Same as {@link #longPoll(HttpRequestWithBody, Class)} but also returns the response hash.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @param classT  The class of the response.
   * @return (response hash code, response body as T) pair
   */
  @SneakyThrows
  public static <T extends BroadcastContent> Pair<String, T> longPollWithHash(
      HttpRequestWithBody request, Class<T> classT) {
    T response = longPoll(request, classT);
    return new Pair<>(DigestUtils.md5Hex(objectMapper.writeValueAsString(response)), response);
  }

  /**
   * Sends a request using long polling.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @param classT  The class of the response.
   * @return The response body as T.
   */
  @SneakyThrows
  public static <T extends BroadcastContent> T longPoll(HttpRequestWithBody request,
                                                        Class<T> classT) {
    if (request.getHttpMethod() != HttpMethod.GET) {
      throw new IllegalArgumentException("Long polling only works with GET requests.");
    }
    AtomicReference<T> res = new AtomicReference<>(null);
    AtomicBoolean gotResponse = new AtomicBoolean(false);
    while (!gotResponse.get()) {
      request.asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), classT))
          .get(TIMEOUT, TimeUnit.SECONDS)
          .ifSuccess(response -> {
            res.set(response.getBody());
            gotResponse.set(true);
          })
          .ifFailure(e -> {
            switch (e.getStatus()) {
              case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
                TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
                request.queryString("access_token", AuthUtils.getAuth().getAccessToken());
                res.set(longPoll(request, classT));
              }
              case HTTP_CLIENT_TIMEOUT -> gotResponse.set(false);
              default -> throw new RuntimeException("Unexpected response code: " + e.getStatus());
            }
          });
    }
    return res.get();
  }

  /**
   * Creates a request for the specified destination.
   *
   * @param method The HTTP method to use for the request.
   * @param dest   The destination for the request.
   * @param path   The path to use for the request.
   * @return The request object.
   */
  public static HttpRequestWithBody request(RequestMethod method, RequestDest dest, String path) {
    return Unirest.request(method.name(), dest.getUrl() + path);
  }
}
