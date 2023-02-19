package com.hexanome16.server.util;

import com.hexanome16.server.models.Level;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

/**
 * Enum of Error Responses to use in API calls.
 */
public enum CustomHttpResponses {
  /**
   * Used for invalid level sent to request.
   * for {@link com.hexanome16.server.models.LevelCard} level.
   */
  BAD_LEVEL_INFO(String.format("Level passed is not a valid level, please use one of %s",
      Arrays.toString(Level.values())), HttpStatus.BAD_REQUEST),

  /**
   * Used for invalid session id.
   */
  INVALID_SESSION_ID("SessionID not associated with any game in current server",
      HttpStatus.BAD_REQUEST),

  /**
   * Used for when it is not players turn.
   */
  NOT_PLAYERS_TURN("Not your turn yet", HttpStatus.BAD_REQUEST),

  /**
   * OK.
   */
  OK("Ok", HttpStatus.OK),

  /**
   * Used for Server side errors that aren't necessarily caused by client.
   * (Object mapper stuff for example)
   *
   * <p>Good case for Custom Message in {@link CustomResponseFactory#getCustomErrorResponse}
   */
  SERVER_SIDE_ERROR("There was an error on the server, please try again later",
      HttpStatus.INTERNAL_SERVER_ERROR);

  private final String body;
  private final HttpStatus status;

  CustomHttpResponses(String body, HttpStatus status) {
    this.body = body;
    this.status = status;
  }

  /**
   * Get body in Error Response.
   *
   * @return body of Error Response
   */
  public String getBody() {
    return body;
  }

  /**
   * Get Status in Error Response.
   *
   * @return status of Error Response
   */
  public HttpStatus getStatus() {
    return status;
  }
}
