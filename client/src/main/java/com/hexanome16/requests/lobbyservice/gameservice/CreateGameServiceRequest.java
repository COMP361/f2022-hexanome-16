package com.hexanome16.requests.lobbyservice.gameservice;

import com.google.gson.Gson;
import com.hexanome16.requests.RequestClient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class CreateGameServiceRequest {
  public static void execute(String access_token) {
    String url = "http://localhost:4242/api/gameservices/Splendor?access_token=" + access_token;
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Payload())))
          .build();
      client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class Payload {
    String location;
    String name;
    Integer maxSessionPlayers;
    Integer minSessionPlayers;
    String displayName;
    String webSupport;

    public Payload() {
      location = "http://127.0.0.1:4243/SplendorService";
      name = "Splendor";
      maxSessionPlayers = 4;
      minSessionPlayers = 2;
      displayName = "Splendor";
      webSupport = "true";
    }

    public Payload(String location, String name, Integer maxSessionPlayers,
                   Integer minSessionPlayers, String displayName, String webSupport) {
      this.location = location;
      this.name = name;
      this.maxSessionPlayers = maxSessionPlayers;
      this.minSessionPlayers = minSessionPlayers;
      this.displayName = displayName;
      this.webSupport = webSupport;
    }
  }
}
