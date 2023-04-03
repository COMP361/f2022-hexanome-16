package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.Arrays;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;

/**
 * Action representing discarding tokens.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = DiscardTokenAction.class)
public class DiscardTokenAction implements Action {
  private Gem[] gemsToChooseFrom;
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.DISCARD;

  /**
   * Constructor for discard token action.
   *
   * @param gems gems to choose from.
   */
  public DiscardTokenAction(Gem[] gems) {
    gemsToChooseFrom = gems;

  }

  @Override
  public ResponseEntity<String> getActionDetails() throws JsonProcessingException {
    String gemsToChooseFromAsString = ObjectMapperUtils.getObjectMapper()
        .writeValueAsString(Arrays
            .stream(gemsToChooseFrom).map(Gem::getBonusType).toArray(String[]::new));
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.DISCARD_TOKEN,
        gemsToChooseFromAsString, null);
  }

}
