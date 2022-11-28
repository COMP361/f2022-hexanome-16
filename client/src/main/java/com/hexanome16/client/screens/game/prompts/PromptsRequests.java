package com.hexanome16.client.screens.game.prompts;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.screens.game.PurchaseMap;
import com.hexanome16.client.utils.UrlUtils;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

/**
 * Class responsible for sending HTTP requests related to the prompts.
 */
public class PromptsRequests {

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId id of the game request is sent from.
   * @param cardMd5 Hash value of the card we're sending.
   * @param username username of player trying to buy card.
   * @param proposedDeal deal proposed by the player.
   */
  public static void buyCard(long  sessionId,
                               String cardMd5,
                               String username,
                               PurchaseMap proposedDeal) {
    try {

      HttpClient client = RequestClient.getClient();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(UrlUtils.createGameServerUri(
              "/api/game/" + sessionId + "/" + cardMd5,
              requestParam(username, proposedDeal)
          )).PUT(HttpRequest.BodyPublishers.noBody())
          .build();

      CompletableFuture<HttpResponse<String>> response =
          client.sendAsync(request, HttpResponse.BodyHandlers.ofString());


      System.out.println("BuyCard Called");

      System.out.println(response.get());

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static String requestParam(String username, PurchaseMap proposedDeal) {
    StringJoiner requestParam = new StringJoiner("&");
    requestParam.add("username=" + username);
    requestParam.add("rubyAmount=" + proposedDeal.getRubyAmount());
    requestParam.add("emeraldAmount=" + proposedDeal.getEmeraldAmount());
    requestParam.add("sapphireAmount=" + proposedDeal.getSapphireAmount());
    requestParam.add("diamondAmount=" + proposedDeal.getDiamondAmount());
    requestParam.add("onyxAmount=" + proposedDeal.getOnyxAmount());
    requestParam.add("goldAmount=" + proposedDeal.getGoldAmount());


    return requestParam.toString();
  }

}
