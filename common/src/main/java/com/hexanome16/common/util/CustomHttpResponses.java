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
   * OK.
   */
  OK("Ok", HTTP_OK),

  /**
   * Used for Server side errors that aren't necessarily caused by client.
   * (Object mapper stuff for example)
   */
  SERVER_SIDE_ERROR("There was an error on the server, please try again later",
      HTTP_INTERNAL_ERROR);

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
}
