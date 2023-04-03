package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.util.CustomHttpResponses;
import org.springframework.http.ResponseEntity;

/**
 * Actions that can be performed.
 */
@JsonDeserialize(using = ActionDeserializer.class)
public interface Action {

  /**
   * Returns the response associated with the completion of an action.
   *
   * @return Information needed for action to perform.
   * @throws JsonProcessingException the json processing exception
   */
  @JsonIgnore
  ResponseEntity<String> getActionDetails() throws JsonProcessingException;

  /**
   * Gets action type.
   *
   * @return the action type
   */
  CustomHttpResponses.ActionType getActionType();
}
