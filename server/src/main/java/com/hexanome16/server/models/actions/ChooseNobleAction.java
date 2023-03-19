package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The type Choose noble action.
 */
@Data
@NoArgsConstructor
public class ChooseNobleAction implements Action {
  @Getter
  private Noble[] nobles;
  private String nobleJson;
  @Getter
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.NOBLE;

  /**
   * Instantiates a new Choose noble action.
   *
   * @param nobles the nobles that the player must choose from
   * @throws JsonProcessingException thrown if nobles cannot be parsed
   */
  public ChooseNobleAction(Noble[] nobles) throws JsonProcessingException {
    this.nobles = nobles;
    ObjectMapper objectMapper = new ObjectMapper();
    this.nobleJson = objectMapper.writeValueAsString(nobles);
  }

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.CHOOSE_NOBLE,
        nobleJson, null);
  }
}
