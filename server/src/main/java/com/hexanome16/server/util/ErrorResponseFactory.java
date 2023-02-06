package com.hexanome16.server.util;

import org.springframework.http.ResponseEntity;

/**
 * Static factory for accessing pre-generated Error Responses.
 */
public class ErrorResponseFactory {
  /**
   * Factory for creating ResponseEntity for any of our Error Responses.
   *
   * <p>Prefer using this when possible,
   * if really out of the ordinary and unique
   * use {@link #getCustomErrorResponse}
   *
   * @param errorType enum ErrorResponse to put in ResponseEntity
   * @return the response entity
   */
  public static ResponseEntity<String> getErrorResponse(ErrorResponses errorType) {
    return new ResponseEntity<>(errorType.getBody(), errorType.getStatus());
  }

  /**
   * Factory for creating ResponseEntity for any of our Error Responses.
   *
   * <p>Prioritize using {@link #getErrorResponse} when possible,
   * if really out of the ordinary and unique
   * use this
   *
   * @param errorType enum ErrorResponse to put in ResponseEntity
   * @param body      custom body to add instead of the errorType body
   * @return the response entity
   */
  public static ResponseEntity<String> getCustomErrorResponse(ErrorResponses errorType,
                                                              String body) {
    return new ResponseEntity<>(body, errorType.getStatus());
  }
}
