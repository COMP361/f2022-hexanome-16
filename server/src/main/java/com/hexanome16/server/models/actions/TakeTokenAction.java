package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;

/**
 * Class for the action to take another token.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = TakeTokenAction.class)
public class TakeTokenAction implements Action {
  private CustomHttpResponses.ActionType actionType =
      CustomHttpResponses.ActionType.TAKE;
  private String gemJson;

  /**
   * Constructor of the class, takes one type of gem as input.
   *
   * @param gem the gem that the player cannot take.
   */
  @SneakyThrows
  public TakeTokenAction(Optional<Gem> gem) {
    if (gem.isPresent()) {
      ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
      this.gemJson = objectMapper.writeValueAsString(gem.get().getBonusType());
    } else {
      this.gemJson = "";
    }
  }

  @Override
  public ResponseEntity<String> getActionDetails() throws JsonProcessingException {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.TAKE_TOKEN, gemJson, null);
  }

}
