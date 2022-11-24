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
  public static void newCard(long sessionId){
    try {
      HttpClient client = RequestClient.getClient();
      System.out.println("called");
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/nextCard/" + sessionId,
              "level=ONE"
          )).GET()
          .build();
      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      response.thenApply(HttpResponse::body).thenAccept(System.out::println).join();
    }catch(Exception e){
      e.printStackTrace();
    }
    //if (statusCode == 200) {
    //start game??? fetch game info???
    //}
  }
}
