package com.hexanome16.client.requests.backend.cards;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.screens.game.Level;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides methods to perform development card related requests to the game server.
 */
public class GameRequest {
  /**
   * Sends a request to initialize the deck.
   *
   * @param sessionId The id of the session.
   * @param level     The level of the card deck.
   * @return something idk
   */
  public static String initDeck(long sessionId, Level level) {
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/getCards/" + sessionId,
              "level=" + level.name() + "&access_token=" + AuthUtils.getAuth().getAccessToken()
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

  /**
   * Sends a request to draw a card.
   *
   * @param sessionId The id of the session.
   * @param level     The level of the card deck.
   * @return something idk
   */
  public static String newCard(long sessionId, Level level) {
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextCard/" + sessionId,
              "level=" + level.name() + "&access_token=" + AuthUtils.getAuth().getAccessToken()
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

  /**
   * Sends a request to get the next noble.
   *
   * @param sessionId The id of the session.
   * @return something idk
   */
  public static String newNoble(long sessionId) {
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextNoble/" + sessionId,
              "access_token=" + AuthUtils.getAuth().getAccessToken()
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
