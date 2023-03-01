package com.hexanome16.client.requests;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.util.Pair;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class provides an HTTP client to send requests to the backend/Lobby Service.
 */
public class RequestClient {
  public static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
      JsonInclude.Include.NON_NULL)
      .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
  public static final int TIMEOUT = 5;
  public static final int LONG_POLL_TIMEOUT = 60;

  private RequestClient() {
    super();
    Unirest.config().concurrency(1, 1)
        .setObjectMapper(new JacksonObjectMapper(objectMapper))
        .setDefaultResponseEncoding("UTF-8")
        .connectTimeout(TIMEOUT * 1000)
        .addShutdownHook(true);
  }

  /**
   * Maps a string to an object of the given class.
   *
   * @param <T>      type of the object
   * @param response input reader
   * @param classT   class of the object
   * @return object of the given class
   */
  public static <T> T mapObject(String response, Class<T> classT) {
    try {
      return objectMapper.readValue(response, classT);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Same as {@link #longPoll(Request)} but also returns response hash.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @return (response hash code, response body as T) pair
   */
  public static <T> Pair<String, T> longPollWithHash(Request<T> request) {
    Pair<String, String> response = longPollStringWithHash(request);
    return new Pair<>(response.getKey(), response.getValue() == null ? null :
        mapObject(response.getValue(), request.getResponseClass()));
  }

  /**
   * Same as {@link #longPollString(Request)} but also returns response hash.
   *
   * @param request The request to send.
   * @return (response hash code, response body as String) pair
   */
  public static Pair<String, String> longPollStringWithHash(Request<?> request) {
    String response = longPollString(request);
    String hash = DigestUtils.md5Hex(response);
    return new Pair<>(hash, response);
  }

  /**
   * Sends a request using long polling.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @return The response body as T.
   */
  public static <T> T longPoll(Request<T> request) {
    String response = longPollString(request);
    return response == null ? null : mapObject(response, request.getResponseClass());
  }

  /**
   * Same as {@link #longPoll(Request)} but specifically for String.
   *
   * @param req The request to send.
   * @return The response body as String.
   */
  private static String longPollString(Request<?> req) {
    AtomicReference<String> res = new AtomicReference<>(null);
    final AtomicBoolean gotResponse = new AtomicBoolean(false);
    if (req.getMethod() != RequestMethod.GET) {
      throw new RuntimeException("Long polling only supports GET requests.");
    }
    GetRequest request = Unirest.get(req.getDest().getUrl() + req.getPath());
    if (req.getQueryParams() != null) {
      request.queryString(req.getQueryParams());
    }
    if (req.getHeaders() != null) {
      request.headers(req.getHeaders());
    }
    while (!gotResponse.get()) {
      try {
        request.asStringAsync()
            .get(LONG_POLL_TIMEOUT, TimeUnit.SECONDS)
            .ifSuccess(response -> {
              res.set(response.getBody());
              gotResponse.set(true);
            })
            .ifFailure(e -> {
              switch (e.getStatus()) {
                case HTTP_BAD_REQUEST, HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
                  if (AuthUtils.getAuth() != null && req.getQueryParams() != null
                      && req.getQueryParams().containsKey("access_token")) {
                    TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
                    req.getQueryParams().put("access_token", AuthUtils.getAuth().getAccessToken());
                    res.set(longPollString(req));
                  }
                  gotResponse.set(true);
                }
                case HTTP_CLIENT_TIMEOUT -> {
                  // Do nothing, just try again.
                }
                default -> res.set(e.getParsingError().isEmpty() ? e.getBody()
                    : e.getParsingError().get().toString());
              }
            });
      } catch (TimeoutException | InterruptedException | ExecutionException e) {
        gotResponse.set(false);
      }
    }
    return res.get();
  }

  /**
   * Sends a request.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @return The response body as T.
   */
  public static <T> T sendRequest(Request<T> request) {
    String response = sendRequestString(request);
    return response == null ? null : mapObject(response, request.getResponseClass());
  }

  /**
   * Sends a request and returns the response as a string.
   *
   * @param request The request to send.
   * @return The response body as String.
   */
  public static String sendRequestString(Request<?> request) {
    AtomicReference<String> res = new AtomicReference<>(null);
    HttpRequestWithBody req = Unirest.request(request.getMethod().name(),
        request.getDest().getUrl() + request.getPath());
    if (request.getQueryParams() != null) {
      req.queryString(request.getQueryParams());
    }
    if (request.getHeaders() != null) {
      req.headers(request.getHeaders());
    }
    if (request.getBody() != null) {
      req.contentType("application/json");
      req.body(request.getBody());
    }
    try {
      req.asStringAsync()
          .get(TIMEOUT, TimeUnit.SECONDS)
          .ifSuccess(response -> {
            res.set(response.getBody());
            request.setStatus(response.getStatus());
          })
          .ifFailure(e -> {
            request.setStatus(e.getStatus());
            switch (e.getStatus()) {
              case HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> {
                if (AuthUtils.getAuth() != null && request.getQueryParams() != null
                    && request.getQueryParams().containsKey("access_token")) {
                  TokenRequest.execute(AuthUtils.getAuth().getRefreshToken());
                  request.getQueryParams()
                      .put("access_token", AuthUtils.getAuth().getAccessToken());
                  res.set(sendRequestString(request));
                }
              }
              default -> res.set(e.getParsingError().isEmpty() ? e.getBody()
                  : e.getParsingError().get().toString());
            }
          });
      return res.get();
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      return null;
    }
  }
}
