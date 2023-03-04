package com.hexanome16.client.requests.backend.prompts;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

/**
 * Class responsible for sending HTTP requests related to the prompts.
 */
public class PromptsRequests {
  /**
   * Get cards of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static LevelCard[] getCards(long sessionId, String username) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/inventory/cards", Map.of("username", username),
        LevelCard[].class));
  }

  /**
   * Get nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static Noble[] getNobles(long sessionId, String username) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/inventory/nobles", Map.of("username", username),
        Noble[].class));
  }

  /**
   * Get reserved cards of the player with provided username and session id.
   *
   * @param sessionId   Session ID.
   * @param username    username of player.
   * @param accessToken access Token.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static LevelCard[] getReservedCards(long sessionId, String username, String accessToken) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/inventory/reservedCards",
        Map.of("username", username, "accessToken", accessToken), LevelCard[].class));
  }

  /**
   * Get reserved nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  public static Noble[] getReservedNobles(long sessionId, String username) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/inventory/reservedNobles",
        Map.of("username", username), Noble[].class));
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
    RequestClient.sendRequest(new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/" + cardMd5, Map.of("access_token", authToken),
        proposedDeal, Void.class));
  }

  /**
   * Send a request to reserve the card.
   *
   * @param sessionId game session id
   * @param cardMd5   card hash
   * @param authToken user authentication token
   */
  public static void reserveCard(long sessionId,
                                 String cardMd5,
                                 String authToken) {
    RequestClient.sendRequest(new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/" + cardMd5 + "/reservation",
        Map.of("access_token", authToken), Void.class));
  }

  /**
   * Send a request to reserve a face down card.
   *
   * @param sessionId game session id
   * @param level     level of the face down card
   * @param authToken user authentication token
   */
  public static void reserveCard(long sessionId,
                                 Level level,
                                 String authToken) {
    RequestClient.sendRequest(new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/deck/reservation",
        Map.of("access_token", authToken, "level", level.name()), Void.class));
  }

  /**
   * Gets player bank of player with username "username" in session with session id "sessionId".
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   */
  public static PurchaseMap getPlayerBank(long sessionId, String username) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/playerBank", Map.of("username", username),
        PurchaseMap.class));
  }

  /**
   * Retrieves the game bank info after the purchase is done. (or attempted)
   *
   * @param sessionId Session ID of the game whose bank we want to retrieve.
   * @return PurchaseMap representation of the Bank's funds as a String
   */
  public static PurchaseMap getGameBankInfo(long sessionId) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/gameBank", PurchaseMap.class));
  }


  /**
   * Retrieves the bonuses available to take two of from the server.
   *
   * @param sessionId session ID of the game whose tokens info we want to retrieve.
   * @return An array List of the possible bonus types.
   */
  public static ArrayList<BonusType> getAvailableTwoBonuses(long sessionId) {
    return Arrays.stream(
            Objects.requireNonNull(RequestClient.sendRequest(new Request<>(RequestMethod.GET,
                RequestDest.SERVER, "/api/games/" + sessionId + "/twoTokens", String[].class))))
        .filter(Objects::nonNull).map(BonusType::fromString)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Retrieves the bonuses available to take three of from the server.
   *
   * @param sessionId session ID of the game whose tokens info we want to retrieve.
   * @return An array List of the possible bonus types.
   */
  @SneakyThrows
  public static ArrayList<BonusType> getAvailableThreeBonuses(long sessionId) {
    return Arrays.stream(
            Objects.requireNonNull(RequestClient.sendRequest(new Request<>(RequestMethod.GET,
                RequestDest.SERVER, "/api/games/" + sessionId + "/threeTokens", String[].class))))
        .filter(Objects::nonNull).map(BonusType::fromString)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId id of the game request is sent from.
   * @param authToken username of player trying to buy card.
   * @param bonusType Desired bonus Type.
   */
  @SneakyThrows
  public static void takeTwo(long sessionId,
                             String authToken,
                             BonusType bonusType) {
    RequestClient.sendRequestString(
        new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/twoTokens",
        Map.of("authenticationToken", authToken, "tokenType", bonusType.name()), Void.class));
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId      id of the game request is sent from.
   * @param authToken      username of player trying to buy card.
   * @param bonusTypeOne   First desired Bonus Type.
   * @param bonusTypeTwo   Second desired Bonus Type.
   * @param bonusTypeThree Third desired Bonus Type.
   */
  @SneakyThrows
  public static void takeThree(long sessionId,
                               String authToken,
                               BonusType bonusTypeOne,
                               BonusType bonusTypeTwo,
                               BonusType bonusTypeThree) {
    RequestClient.sendRequest(new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/threeTokens",
        Map.of("authenticationToken", authToken, "tokenTypeOne", bonusTypeOne.name(),
            "tokenTypeTwo", bonusTypeTwo.name(), "tokenTypeThree", bonusTypeThree.name()),
        Void.class));
  }

}
