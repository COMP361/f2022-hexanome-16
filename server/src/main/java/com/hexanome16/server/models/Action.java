package com.hexanome16.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

/**
 * Actions that can be performed.
 */
public interface Action {

  /**
   * Returns the response associated with the completion of an action.
   *
   * @return Information needed for action to perform.
   * @throws JsonProcessingException if the action cannot be parsed.
   */
  ResponseEntity<String> getActionDetails() throws JsonProcessingException;
}
