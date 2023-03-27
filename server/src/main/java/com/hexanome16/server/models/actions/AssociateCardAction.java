package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * The type Associate Bag Card.
 */
@Data
@NoArgsConstructor
@JsonDeserialize(as = AssociateCardAction.class)
public class AssociateCardAction implements Action {
  @Getter
  private ServerLevelCard card;
  @Getter
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.ASSOCIATE_BAG;

  /**
   * Instantiates a new Associate Card action.
   *
   * @param card bag card who will be associated.
   * @throws JsonProcessingException if Json fails.
   */
  public AssociateCardAction(ServerLevelCard card)
      throws JsonProcessingException {
    this.card = card;
  }

  @Override
  public ResponseEntity<String> getActionDetails() {
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.ASSOCIATE_BAG_CARD,
        null, null);
  }

}
