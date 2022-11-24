package com.hexanome16.client.screens.game;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class GameRequest
{
  public static void newGame(){
    HttpClient client = RequestClient.getClient();
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/",
              "sessionId=" + "???"
          )).POST(HttpRequest.BodyPublishers.noBody())
          .build();
      int statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::statusCode).get();
      if (statusCode == 200) {
        //start game??? fetch game info???
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      // do nothing??
    }
  }
}
