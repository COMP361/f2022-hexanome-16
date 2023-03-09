package com.hexanome16.client.requests;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.client.requests.lobbyservice.oauth.TokenRequest;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.util.CustomHttpResponses;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.util.Pair;
import kong.unirest.core.GetRequest;
import kong.unirest.core.Header;
import kong.unirest.core.Headers;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class provides an HTTP client to send requests to the backend/Lobby Service.
 */
public class RequestClient {
  /**
   * ObjectMapper instance with custom settings for serialization.
   */
  public static final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
          JsonInclude.Include.NON_NULL)
      .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
  /**
   * Response timeout for regular requests.
   */
  public static final int TIMEOUT = 5;
  /**
   * Response timeout for long polling requests.
   */
  public static final int LONG_POLL_TIMEOUT = 60;

  private RequestClient() {
    super();
    Unirest.config()
        .setObjectMapper(new JacksonObjectMapper(objectMapper))
        .setDefaultResponseEncoding("UTF-8")
        .connectTimeout(TIMEOUT * 1000);
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
   * Send a long polling request and returns response with associated hash.
   *
   * @param <T>     The type of the response.
   * @param request The request to send.
   * @return (response hash code, response body as T) pair
   */
  public static <T> Pair<String, T> longPollWithHash(Request<T> request) {
    Pair<String, String> response = longPollStringWithHash(request);
    return new Pair<>(response.getKey(), response.getValue() == null
        || response.getValue().isBlank() ? null :
        mapObject(response.getValue(), request.getResponseClass()));
  }

  private static Pair<String, String> longPollStringWithHash(Request<?> request) {
    String response = longPollString(request);
    String hash = DigestUtils.md5Hex(response);
    return new Pair<>(hash, response);
  }

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
                  } else {
                    res.set(e.getParsingError().isEmpty() ? e.getBody()
                        : e.getParsingError().get().toString());
                  }
                  gotResponse.set(true);
                }
                case HTTP_CLIENT_TIMEOUT -> {
                  // Do nothing, just try again.
                }
                default -> {
                  res.set(e.getParsingError().isEmpty() ? e.getBody()
                      : e.getParsingError().get().toString());
                  gotResponse.set(true);
                }
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
    return response == null || response.isBlank() ? null :
        mapObject(response, request.getResponseClass());
  }

  /**
   * Sends a request and returns the response as a string.
   *
   * @param request The request to send.
   * @return The response body as String.
   */
  public static Pair<Headers, String> sendRequestHeadersString(Request<?> request) {
    AtomicReference<String> res = new AtomicReference<>(null);
    AtomicReference<Headers> headers = new AtomicReference<>(null);
    HttpRequestWithBody req = Unirest.request(request.getMethod().name(),
        request.getDest().getUrl() + request.getPath());
    if (request.getQueryParams() != null) {
      req.queryString(request.getQueryParams());
    }
    if (request.getHeaders() != null) {
      req.headers(request.getHeaders());
    }
    CompletableFuture<HttpResponse<String>> future;
    if (request.getBody() != null) {
      req.contentType("application/json");
      future = req.body(request.getBody()).asStringAsync();
    } else {
      future = req.asStringAsync();
    }
    try {
      future.get(TIMEOUT, TimeUnit.SECONDS)
          .ifSuccess(response -> {
            res.set(response.getBody());
            headers.set(response.getHeaders());
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
              case HTTP_NOT_FOUND -> res.set(CustomHttpResponses.INVALID_SESSION_ID.getBody());
              default -> res.set(e.getParsingError().isEmpty() ? e.getBody()
                  : e.getParsingError().get().toString());
            }
          });
      return new Pair<>(headers.get(), res.get());
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      return null;
    }
  }

  /**
   * Sends a request and returns the response as a string.
   *
   * @param request The request to send.
   * @return The response body as String.
   */
  public static String sendRequestString(Request<?> request) {
    Pair<Headers, String> myPair = sendRequestHeadersString(request);
    assert myPair != null;
    return myPair.getValue();
  }

}
