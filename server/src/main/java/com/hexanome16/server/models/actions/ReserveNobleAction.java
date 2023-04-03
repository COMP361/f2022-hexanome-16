package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;


/**
 * The type choose noble to reserve action.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = ReserveNobleAction.class)
public class ReserveNobleAction implements Action {
  private Noble[] nobles;
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.NOBLE_RESERVE;

  /**
   * Instantiates a new Choose noble to reserve action.
   *
   * @param nobles the nobles that the player must choose from
   */
  public ReserveNobleAction(Noble[] nobles) {
    this.nobles = nobles;
  }

  @Override
  public ResponseEntity<String> getActionDetails() throws JsonProcessingException {
    String nobleJson =  ObjectMapperUtils.getObjectMapper().writeValueAsString(nobles);
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.RESERVE_NOBLE,
        nobleJson, null);
  }

}
