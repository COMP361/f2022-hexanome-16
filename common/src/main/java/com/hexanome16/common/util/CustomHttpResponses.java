package com.hexanome16.common.util;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Enum of Error Responses to use in API calls.
 */
@Getter
public enum CustomHttpResponses implements BroadcastContent {
  /**
   * Used for invalid level sent to request.
   * for {@link LevelCard} level.
   */
  BAD_LEVEL_INFO(String.format("Level passed is not a valid level, please use one of %s",
      Arrays.toString(Level.values())), HTTP_BAD_REQUEST),

  /**
   * Used for invalid session id.
   */
  INVALID_SESSION_ID("SessionID not associated with any game in current server",
      HTTP_NOT_FOUND),
  /**
   * Used for invalid access token.
   */
  INVALID_ACCESS_TOKEN("No player with corresponding access token in game with sessionId provided",
      HTTP_UNAUTHORIZED),

  /**
   * Used for when it is not players turn.
   */
  NOT_PLAYERS_TURN("Not your turn yet", HTTP_BAD_REQUEST),
  /**
   * Used for when searching for player by username.
   */
  PLAYER_NOT_IN_GAME("No player with given username found in game", HTTP_BAD_REQUEST),

  /**
   * Used for invalid card md5 hash.
   */
  BAD_CARD_HASH("Card hash is not valid", HTTP_BAD_REQUEST),
  /**
   * Used for when player does not have enough funds for purchase.
   */
  INSUFFICIENT_FUNDS("You do not have enough funds to buy this card", HTTP_BAD_REQUEST),
  /**
   * Used for when proposed deal does not work for current card.
   */
  INVALID_PROPOSED_DEAL(
      "The deal you've proposed is incompatible with the card you're trying to purchase",
      HTTP_BAD_REQUEST),

  /**
   * Used for when player does not have enough bonuses to be visited by the noble.
   */
  INSUFFICIENT_BONUSES_FOR_VISIT("You do not have sufficient bonuses to be visited by this noble",
      HTTP_BAD_REQUEST),
  /**
   * OK.
   */
  OK("Ok", HTTP_OK),

  /**
   * Used for Server side errors that aren't necessarily caused by client.
   * (Object mapper stuff for example)
   */
  SERVER_SIDE_ERROR("There was an error on the server, please try again later",
      HTTP_INTERNAL_ERROR),
  /**
   * Used in associate bag, need to have bonuses already in
   * inventory to associate a bag.
   */
  NO_BONUS_TO_ASSOCIATE("Player inventory is empty and so cannot associate bag card",
      HTTP_BAD_REQUEST),
  /**
   * Used in associate Bag, Gem cannot be gold,
   * or null and needs to be in inventory.
   */
  BAD_GEM_TO_ASSOCIATE_TO_BAG("Chosen Gem to associate to bag is bad",
      HTTP_BAD_REQUEST),
  /**
   * Used when an action is requested but not at the top of the action queue.
   */
  ILLEGAL_ACTION("Not allowed to perform action", HTTP_BAD_REQUEST),

  /**
   * Used for indicating that player must choose a noble.
   * <p>
   * Only use with CustomResponse to pass in list of nobles in body.
   * </p>
   */
  CHOOSE_NOBLE("Insert custom body containing list of nobles", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.NOBLE.getMessage()))),
  /**
   * Used for indicating that player must choose a city.
   * <p>
   * Only use with CustomResponse to pass in list of cities in body.
   * </p>
   */
  CHOOSE_CITY("Insert custom body containing list of cities", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.CITY.getMessage()))),
  /**
   * Used for indicating that player must take a level two card.
   * <p>
   * No need for a body so just use with getResponse
   * </p>
   */
  TAKE_LEVEL_TWO("Player must take a level two card", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.LEVEL_TWO.getMessage()))),
  /**
   * Used for indicating that player must take a level one card.
   * <p>
   * No need for a body so just use with getResponse
   * </p>
   */
  TAKE_LEVEL_ONE("Player must take a level one card", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.LEVEL_ONE.getMessage()))),
  /**
   * Used for indicating that player must discard a token.
   * <p>
   * Only use with CustomResponse to pass in list of tokens in body.
   * </p>
   */
  DISCARD_TOKEN("Insert custom body with tokens to choose from", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.DISCARD.getMessage()))),

  TAKE_TOKEN("Insert custom body containing a gem that cannot be chosen", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.TAKE.getMessage()))),

  /**
   * Used for indicating that player must Associate a bag card.
   * <p>
   * Only use with CustomResponse to pass in list of Gem types in body.
   * </p>
   */
  ASSOCIATE_BAG_CARD("Please associate your bag card to a bonus type"
      + "(the ones for which we have a bonus card in our inventory)", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.ASSOCIATE_BAG.getMessage()))),
  /**
   * Used for indicating that player doesn't have to perform any additional actions.
   */
  END_OF_TURN("No Further Actions needed", HTTP_OK,
      Map.of(ActionType.ACTION_TYPE, List.of(ActionType.END_TURN.getMessage())));

  private final String body;
  private final Map<String, List<String>> headers;
  private final int status;

  CustomHttpResponses(String body, int status) {
    this.body = body;
    this.status = status;
    this.headers = null;
  }

  CustomHttpResponses(String body, int status, Map<String, List<String>> headers) {
    this.body = body;
    this.status = status;
    this.headers = headers;
  }

  @Override
  public boolean isEmpty() {
    return (body == null || body.isBlank()) && status == 0;
  }

  /**
   * Class for ActionTypes, holds strings used all over the code.
   */
  public enum ActionType {
    NOBLE("choose-noble"), CITY("choose-city"), LEVEL_TWO("take-level-two"),
    DISCARD("discard-token"), TAKE("take-token"), ASSOCIATE_BAG("associate-bag"),
    LEVEL_ONE("take-level-one"), END_TURN("done");

    /**
     * Action Type string.
     */
    public static final String ACTION_TYPE = "action-type";
    private final String message;

    ActionType(String message) {
      this.message = message;
    }

    /**
     * Gets the message.
     *
     * @return message.
     */
    public String getMessage() {
      return message;
    }
  }
}
