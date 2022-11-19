package com.hexanome16.client.requests;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * This class provides an HTTP client to send requests to the backend/Lobby Service.
 */
public class RequestClient {

  private static final HttpClient client = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  private RequestClient() {
    super();
  }

  public static HttpClient getClient() {
    return client;
  }
}
