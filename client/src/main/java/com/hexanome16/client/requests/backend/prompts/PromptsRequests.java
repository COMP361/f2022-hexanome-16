package com.hexanome16.client.requests.backend.prompts;

import com.hexanome16.client.requests.Request;
import com.hexanome16.client.requests.RequestClient;
import com.hexanome16.client.requests.RequestDest;
import com.hexanome16.client.requests.RequestMethod;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.util.Pair;
import kong.unirest.core.Headers;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;

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
   * Gets the list of all the cards of level two on the board for game with session id.
   *
   * @param sessionId sessionId of game.
   * @return Array of all level 2 cards on board.
   */
  public static LevelCard[] getLevelTwoCardsOnBoard(long sessionId) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/board/cards/levelTwo", null,
        LevelCard[].class));
  }


  /**
   * Gets the list of all the cards of level one on the board for game with session id.
   *
   * @param sessionId sessionId of game.
   * @return Array of all level 1 cards on board.
   */
  public static LevelCard[] getLevelOneCardsOnBoard(long sessionId) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/board/cards/levelOne", null,
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
  public static DeckJson getReservedCards(long sessionId, String username, String accessToken) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET, RequestDest.SERVER,
        "/api/games/" + sessionId + "/inventory/reservedCards",
        Map.of("username", username, "access_token", accessToken), DeckJson.class));
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
   * @return Pair of the response from server, headers and string
   */
  public static Pair<Headers, String> buyCard(long sessionId,
                             String cardMd5,
                             String authToken,
                             PurchaseMap proposedDeal) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.PUT,
        RequestDest.SERVER, "/api/games/" + sessionId + "/cards/" + cardMd5,
        Map.of("access_token", authToken), proposedDeal, String.class));
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
        "/api/games/" + sessionId + "/cards/" + cardMd5 + "/reservation",
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
   * Retrieves the bonuses available to associate to a bag card for the player from the server.
   *
   * @param sessionId session id.
   * @param auth auth of player whose available bonuses we are interested in.
   * @return BonusType string representations in an array.
   */
  public static String[] getPossibleBonuses(long sessionId, String auth) {
    return RequestClient.sendRequest(new Request<>(RequestMethod.GET,
                RequestDest.SERVER, "/api/games/" + sessionId + "/cards/bonuses",
        Map.of("access_token", auth),
        String[].class));
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId id of the game request is sent from.
   * @param authToken username of player trying to buy card.
   * @param bonusType Desired bonus Type.
   * @return server response.
   */
  @SneakyThrows
  public static Pair<Headers, String> takeTwo(long sessionId,
                             String authToken,
                             BonusType bonusType) {
    return RequestClient.sendRequestHeadersString(
        new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/twoTokens",
        Map.of("access_token", authToken, "tokenType", bonusType.name()), Void.class));
  }

  /**
   * Sends a request to the server to buy a card.
   *
   * @param sessionId      id of the game request is sent from.
   * @param authToken      username of player trying to buy card.
   * @param bonusTypeOne   First desired Bonus Type.
   * @param bonusTypeTwo   Second desired Bonus Type.
   * @param bonusTypeThree Third desired Bonus Type.
   * @return server response.
   */
  @SneakyThrows
  public static Pair<Headers, String> takeThree(long sessionId,
                               String authToken,
                               BonusType bonusTypeOne,
                               BonusType bonusTypeTwo,
                               BonusType bonusTypeThree) {
    return RequestClient.sendRequestHeadersString(
        new Request<>(RequestMethod.PUT, RequestDest.SERVER,
        "/api/games/" + sessionId + "/threeTokens",
        Map.of("access_token", authToken, "tokenTypeOne", bonusTypeOne.name(),
            "tokenTypeTwo", bonusTypeTwo.name(), "tokenTypeThree", bonusTypeThree.name()),
        Void.class));
  }

  /**
   * Sends a request to take a level two card for free.
   *
   * @param sessionId id of the game request is sent from.
   * @param accessToken access token to allow action.
   * @param chosenCardHash desired card's Hash.
   * @return server response.
   */
  public static Pair<Headers, String> takeLevelTwo(long sessionId, String accessToken,
                                                   String chosenCardHash) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.PUT,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/board/cards/levelTwo",
        Map.of("access_token", accessToken, "chosenCard", chosenCardHash),
        Void.class));
  }

  /**
   * Sends a request to take a level one card for free.
   *
   * @param sessionId id of the game request is sent from.
   * @param accessToken access token to allow action.
   * @param hash desired card's Hash.
   * @return server response.
   */
  public static Pair<Headers, String> takeLevelOne(long sessionId,
                                                   String accessToken, String hash) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.PUT,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/board/cards/levelOne",
        Map.of("access_token", accessToken, "chosenCard", hash),
        Void.class));
  }

  /**
   * Sends a request to claim a noble.
   *
   * @param sessionId session id
   * @param accessToken access token
   * @param nobleId noble id
   * @return server response.
   */
  public static Pair<Headers, String> claimNoble(long sessionId, String accessToken,
                                                   String nobleId) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.PUT,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/nobles/" + nobleId,
        Map.of("access_token", accessToken),
        String.class));
  }

  /**
   * Retrieves the action that needs to be performed.
   *
   * @param sessionId session Id.
   * @param username username of the player whose action we're trying to retrieve
   * @param accessToken access token.
   * @return server Response.
   */
  public static Pair<Headers, String> getActionForPlayer(long sessionId,
                                                         String username, String accessToken) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.GET,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/players/" + username + "/actions",
        Map.of("access_token", accessToken), Void.class));
  }

  /**
   * Makes Discard one action choice response to server.
   *
   * @param sessionId session id
   * @param accessToken auth of player
   * @param chosenBonus bonus type chosen.
   * @return server response.
   */
  public static Pair<Headers, String> discardOne(long sessionId, String accessToken,
                                                 BonusType chosenBonus) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.DELETE,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/tokens",
        Map.of("access_token", accessToken, "tokenType",
            chosenBonus.name()), Void.class));
  }

  /**
   * Makes Associate bag action choice response to server.
   *
   * @param sessionId session id
   * @param accessToken auth of player
   * @param chosenBonus bonus type chosen.
   * @return server response.
   */
  public static Pair<Headers, String> associateBag(long sessionId, String accessToken,
                                                   BonusType chosenBonus) {
    return RequestClient.sendRequestHeadersString(new Request<>(RequestMethod.PUT,
        RequestDest.SERVER,
        "/api/games/" + sessionId + "/cards/bagcards",
        Map.of("access_token", accessToken, "tokenType",
            chosenBonus.name()), Void.class));
  }


}
