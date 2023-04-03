package com.hexanome16.server.models.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.util.CustomResponseFactory;
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
  private ServerLevelCard card;
  private CustomHttpResponses.ActionType actionType = CustomHttpResponses.ActionType.ASSOCIATE_BAG;

  /**
   * Instantiates a new Associate Card action.
   *
   * @param card bag card who will be associated.
   */
  public AssociateCardAction(ServerLevelCard card) {
    this.card = card;
  }

  @Override
  public ResponseEntity<String> getActionDetails() throws JsonProcessingException {
    return CustomResponseFactory.getResponse(CustomHttpResponses.ASSOCIATE_BAG_CARD);
  }

}
