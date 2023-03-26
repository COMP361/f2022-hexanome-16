package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The type Take two action.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = TakeOneAction.class)
public class TakeOneAction implements Action {
  @Getter
  private CustomHttpResponses.ActionType actionType =
      CustomHttpResponses.ActionType.LEVEL_ONE;

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_ONE);
  }
}
