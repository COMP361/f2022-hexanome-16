package com.hexanome16.client.requests.backend.prompts;

import static com.hexanome16.client.requests.RequestClient.TIMEOUT;
import static com.hexanome16.client.requests.RequestClient.mapObject;

import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import models.Level;
import models.LevelCard;
import models.Noble;
import models.price.PurchaseMap;

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
  @SneakyThrows
  public static LevelCard[] getCards(long sessionId, String username) {
    AtomicReference<LevelCard[]> cards = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/inventory/cards")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("username", username)
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), LevelCard[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> cards.set(res.getBody()));
    return cards.get();
  }

  /**
   * Get nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  @SneakyThrows
  public static Noble[] getNobles(long sessionId, String username) {
    AtomicReference<Noble[]> nobles = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/inventory/nobles")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("username", username)
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), Noble[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> nobles.set(res.getBody()));
    return nobles.get();
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
  @SneakyThrows
  public static LevelCard[] getReservedCards(long sessionId, String username, String accessToken) {
    AtomicReference<LevelCard[]> cards = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/inventory/reservedCards")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("username", username)
        .queryString("accessToken", accessToken)
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), LevelCard[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> cards.set(res.getBody()));
    return cards.get();
  }

  /**
   * Get reserved nobles of the player with provided username and session id.
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   * @author Elea
   */
  @SneakyThrows
  public static Noble[] getReservedNobles(long sessionId, String username) {
    AtomicReference<Noble[]> nobles = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/inventory/reservedNobles")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("username", username)
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), Noble[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> nobles.set(res.getBody()));
    return nobles.get();
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId    id of the game request is sent from.
   * @param cardMd5      Hash value of the card we're sending.
   * @param authToken    username of player trying to buy card.
   * @param proposedDeal deal proposed by the player.
   */
  @SneakyThrows
  public static void buyCard(long sessionId,
                             String cardMd5,
                             String authToken,
                             PurchaseMap proposedDeal) {
    RequestClient.request(RequestMethod.PUT, RequestDest.SERVER,
            "/api/games/{sessionId}/{cardMd5}")
        .routeParam("sessionId", String.valueOf(sessionId))
        .routeParam("cardMd5", cardMd5)
        .queryString("authenticationToken", authToken)
        .body(proposedDeal)
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS);
  }

  /**
   * Send a request to reserve the card.
   *
   * @param sessionId game session id
   * @param cardMd5   card hash
   * @param authToken user authentication token
   */
  @SneakyThrows
  public static void reserveCard(long sessionId,
                                 String cardMd5,
                                 String authToken) {
    RequestClient.request(RequestMethod.PUT, RequestDest.SERVER,
            "/api/games/{sessionId}/{cardMd5}/reservation")
        .routeParam("sessionId", String.valueOf(sessionId))
        .routeParam("cardMd5", cardMd5)
        .queryString("authenticationToken", authToken)
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS);
  }

  /**
   * Send a request to reserve a face down card.
   *
   * @param sessionId game session id
   * @param level     level of the face down card
   * @param authToken user authentication token
   */
  @SneakyThrows
  public static void reserveCard(long sessionId,
                                 Level level,
                                 String authToken) {
    RequestClient.request(RequestMethod.PUT, RequestDest.SERVER,
            "/api/games/{sessionId}/deck/reservation")
        .routeParam("sessionId", String.valueOf(sessionId))
        .routeParam("level", level.name())
        .queryString("authenticationToken", authToken)
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS);
  }

  /**
   * Gets player bank of player with username "username" in session with session id "sessionId".
   *
   * @param sessionId Session ID.
   * @param username  username of player.
   * @return PurchaseMap representation of the player's funds as a String
   */
  @SneakyThrows
  public static PurchaseMap getPlayerBank(long sessionId, String username) {
    AtomicReference<PurchaseMap> bank = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/playerBank")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("username", username)
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), PurchaseMap.class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> bank.set(res.getBody()));
    return bank.get();
  }

  /**
   * Retrieves the game bank info after the purchase is done. (or attempted)
   *
   * @param sessionId Session ID of the game whose bank we want to retrieve.
   * @return PurchaseMap representation of the Bank's funds as a String
   */
  @SneakyThrows
  public static PurchaseMap getGameBankInfo(long sessionId) {
    AtomicReference<PurchaseMap> gameBank = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/gameBank")
        .routeParam("sessionId", String.valueOf(sessionId))
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), PurchaseMap.class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> gameBank.set(res.getBody()));
    return gameBank.get();
  }


  /**
   * Retrieves the bonuses available to take two of from the server.
   *
   * @param sessionId session ID of the game whose tokens info we want to retrieve.
   * @return An array List of the possible bonus types.
   */
  @SneakyThrows
  public static ArrayList<BonusType> getAvailableTwoBonuses(long sessionId) {
    AtomicReference<String[]> bonuses = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/twoTokens")
        .routeParam("sessionId", String.valueOf(sessionId))
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), String[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> bonuses.set(res.getBody()));
    return Arrays.stream(bonuses.get()).filter(Objects::nonNull).map(BonusType::fromString)
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
    AtomicReference<String[]> bonuses = new AtomicReference<>(null);
    RequestClient.request(RequestMethod.GET, RequestDest.SERVER,
            "/api/games/{sessionId}/threeTokens")
        .routeParam("sessionId", String.valueOf(sessionId))
        .asObjectAsync(rawResponse -> mapObject(rawResponse.getContentReader(), String[].class))
        .get(TIMEOUT, TimeUnit.SECONDS)
        .ifSuccess(res -> bonuses.set(res.getBody()));
    return Arrays.stream(bonuses.get()).filter(Objects::nonNull).map(BonusType::fromString)
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
    RequestClient.request(RequestMethod.PUT, RequestDest.SERVER,
            "/api/games/{sessionId}/twoTokens")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("authenticationToken", authToken)
        .queryString("tokenType", bonusType.name())
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS);
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
    RequestClient.request(RequestMethod.PUT, RequestDest.SERVER,
            "/api/games/{sessionId}/threeTokens")
        .routeParam("sessionId", String.valueOf(sessionId))
        .queryString("authenticationToken", authToken)
        .queryString("tokenTypeOne", bonusTypeOne.name())
        .queryString("tokenTypeTwo", bonusTypeTwo.name())
        .queryString("tokenTypeThree", bonusTypeThree.name())
        .asEmptyAsync()
        .get(TIMEOUT, TimeUnit.SECONDS);
  }

}
