package com.hexanome16.client.requests.backend.cards;

import com.google.gson.Gson;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.lobbyservice.sessions.ListSessionsRequest;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.types.sessions.Session;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javafx.util.Pair;

/**
 * This class provides methods to perform development card related requests to the game server.
 */
public class GameRequest {
  /**
   * Sends a request to initialize the deck.
   *
   * @param sessionId The id of the session.
   * @param level     The level of the card deck.
   * @return deck json
   */
  public static String initDeck(long sessionId, Level level) {
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/" + sessionId + "/deck/init",
              "level=" + level.name() + "&accessToken=" + AuthUtils.getAuth().getAccessToken()
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String updateDeck(long sessionId, Level level) {
    HttpClient client = RequestClient.getClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(UrlUtils.createGameServerUri(
            "/api/game/" + sessionId + "/deck",
            "level=" + level.name() + "&accessToken=" + AuthUtils.getAuth().getAccessToken()
        )).header("Content-Type", "application/json")
        .GET()
        .build();
    System.out.println("working?");
    String response = RequestClient.longPollAlt(request);
    System.out.println("working...");
    return response;
  }


  /**
   * Sends a request to draw a card.
   *
   * @param sessionId The id of the session.
   * @param level     The level of the card deck.
   * @return card json
   */
  public static String newCard(long sessionId, Level level) {
    try {
      HttpClient client = RequestClient.getClient();
      URI uri = UrlUtils.createGameServerUri(
          "/api/game/nextCard/" + sessionId,
          "level=" + level.name() + "&accessToken=" + AuthUtils.getAuth().getAccessToken()
      );
      System.out.println(uri);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Sends a request to get the next noble.
   *
   * @param sessionId The id of the session.
   * @return noble json
   */
  public static String newNoble(long sessionId) {
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextNoble/" + sessionId,
              "accessToken=" + AuthUtils.getAuth().getAccessToken()
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
