package com.hexanome16.client.screens.game;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameRequest
{
  public static String initDeck(long sessionId, Level level){
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/getCards/" + sessionId,
              "level=" + level.name()
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public static String newCard(long sessionId, Level level){
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextCard/" + sessionId,
              "level=" + level.name()
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public static String newNoble(long sessionId){
    try {
      HttpClient client = RequestClient.getClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextNoble/" + sessionId,
              ""
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      return response.thenApply(HttpResponse::body).get();
    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }
}
