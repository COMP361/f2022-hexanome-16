package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;


/**
 * The type choose noble to reserve action.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = ReserveNobleAction.class)
public class ReserveNobleAction implements Action {
  @Getter
  private Noble[] nobles;
  private String nobleJson;
  @Getter
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.NOBLE_RESERVE;

  /**
   * Instantiates a new Choose noble to reserve action.
   *
   * @param nobles the nobles that the player must choose from
   * @throws JsonProcessingException thrown if nobles cannot be parsed
   */
  public ReserveNobleAction(Noble[] nobles) throws JsonProcessingException {
    this.nobles = nobles;
    ObjectMapper objectMapper = new ObjectMapper();
    this.nobleJson = objectMapper.writeValueAsString(nobles);
  }

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.RESERVE_NOBLE,
        nobleJson, null);
  }

}
