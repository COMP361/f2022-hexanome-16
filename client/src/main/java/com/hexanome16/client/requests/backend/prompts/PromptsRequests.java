package com.hexanome16.client.requests.backend.prompts;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.screens.game.PurchaseMap;
import com.hexanome16.client.utils.UrlUtils;
import java.net.URI;
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
   * Helper function to convert a request to String from a URI.
   */
  private static String uriToRequest(URI uri) {
    try {
      HttpClient client = RequestClient.getClient();
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
   * Get cards of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static String getCards(long sessionId, String username) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/inventory/cards",
            "username=" + username
    );
    // return the request
    return uriToRequest(uri);
  }

  /**
   * Get nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static String getNobles(long sessionId, String username) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/inventory/nobles",
            "username=" + username
    );
    // return the request
    return uriToRequest(uri);
  }

  /**
   * Get reserved cards of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @param accessToken access Token.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static String getReservedCards(long sessionId, String username, String accessToken) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/inventory/reservedCards",
            "username=" + username + "&accessToken=" + accessToken
    );
    // return the request
    return uriToRequest(uri);
  }

  /**
   * Get reserved nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static String getReservedNobles(long sessionId, String username) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/inventory/reservedNobles",
            "username=" + username
    );
    // return the request
    return uriToRequest(uri);
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId    id of the game request is sent from.
   * @param cardMd5      Hash value of the card we're sending.
   * @param authToken    username of player trying to buy card.
   * @param proposedDeal deal proposed by the player.
   */
  public static void buyCard(long sessionId,
                             String cardMd5,
                             String authToken,
                             PurchaseMap proposedDeal) {
    try {

      HttpClient client = RequestClient.getClient();

      HttpRequest request = HttpRequest.newBuilder()
              .uri(UrlUtils.createGameServerUri(
                      "/api/games/" + sessionId + "/" + cardMd5,
                      requestParam(authToken, proposedDeal)
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

  /**
   * Gets player bank of player with username "username" in session with session id "sessionId".
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   */
  public static String getPlayerBank(long sessionId, String username) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/playerBank",
            "username=" + username
    );
    // return the request
    return uriToRequest(uri);
  }

  /**
   * Retrieves the game bank info after the purchase is done. (or attempted)
   *
   * @param sessionId Session ID of the game whose bank we want to retrieve.
   * @return PurchaseMap representation of the Bank's funds as a String
   */
  public static String getNewGameBankInfo(long sessionId) {
    // create a URI
    URI uri = UrlUtils.createGameServerUri(
            "/api/games/" + sessionId + "/gameBank", ""
    );
    // return the request
    return uriToRequest(uri);
  }

  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////
  private static String requestParam(String authToken, PurchaseMap proposedDeal) {
    StringJoiner requestParam = new StringJoiner("&");
    requestParam.add("authenticationToken=" + authToken);
    requestParam.add("rubyAmount=" + proposedDeal.getRubyAmount());
    requestParam.add("emeraldAmount=" + proposedDeal.getEmeraldAmount());
    requestParam.add("sapphireAmount=" + proposedDeal.getSapphireAmount());
    requestParam.add("diamondAmount=" + proposedDeal.getDiamondAmount());
    requestParam.add("onyxAmount=" + proposedDeal.getOnyxAmount());
    requestParam.add("goldAmount=" + proposedDeal.getGoldAmount());

    return requestParam.toString();
  }


}
