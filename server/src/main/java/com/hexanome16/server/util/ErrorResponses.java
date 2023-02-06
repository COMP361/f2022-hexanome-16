package com.hexanome16.server.util;

import com.hexanome16.server.models.Level;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

/**
 * Enum of Error Responses to use in API calls.
 */
public enum ErrorResponses {
  /**
   * Used for invalid level sent to request
   * for {@link com.hexanome16.server.models.LevelCard} level.
   */
  BAD_LEVEL_INFO(String.format("Level passed is not a valid level, please use one of %s",
      Arrays.toString(Level.values())), HttpStatus.BAD_REQUEST);

  private final String body;
  private final HttpStatus status;

  ErrorResponses(String body, HttpStatus status) {
    this.body = body;
    this.status = status;
  }

  /**
   * Get body in Error Response
   *
   * @return body of Error Response
   */
  public String getBody() {
    return body;
  }

  /**
   * Get Status in Error Response
   *
   * @return status of Error Response
   */
  public HttpStatus getStatus() {
    return status;
  }
}
