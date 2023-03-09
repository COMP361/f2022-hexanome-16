package com.hexanome16.server.models;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * The type Take two action.
 */
public class TakeTwoAction implements Action {
  @Getter
  private final CustomHttpResponses.ActionType actionType =
      CustomHttpResponses.ActionType.LEVEL_TWO;

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO);
  }
}
