package com.hexanome16.server.models;

import com.hexanome16.common.util.CustomHttpResponses;
import org.springframework.http.ResponseEntity;

/**
 * Actions that can be performed.
 */
public interface Action {

  /**
   * Returns the response associated with the completion of an action.
   *
   * @return Information needed for action to perform.
   */
  ResponseEntity<String> getActionDetails();

  /**
   * Gets action type.
   *
   * @return the action type
   */
  CustomHttpResponses.ActionType getActionType();
}
