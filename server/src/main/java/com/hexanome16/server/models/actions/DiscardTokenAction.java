package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.Arrays;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;

/**
 * Action representing discarding tokens.
 */
public class DiscardTokenAction implements Action {

  private String[] gemsToChooseFrom;
  private String gemsToChooseFromAsString;
  @Getter
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.DISCARD;

  /**
   * Constructor for discard token action.
   *
   * @param gems gems to choose from.
   */
  @SneakyThrows
  public DiscardTokenAction(Gem[] gems) {
    gemsToChooseFrom = Arrays.stream(gems).map(Gem::getBonusType).toArray(String[]::new);
    ObjectMapper objectMapper = new ObjectMapper();
    gemsToChooseFromAsString = objectMapper.writeValueAsString(gemsToChooseFrom);
  }

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.DISCARD_TOKEN,
        gemsToChooseFromAsString, null);
  }

}
