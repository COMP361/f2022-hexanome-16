package com.hexanome16.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A deck class made for long polling.
 */
public class DeckHash implements BroadcastContent {
  public static Map<String, DevelopmentCard> allCards = new HashMap<String, DevelopmentCard>();
  private Map<String, DevelopmentCard> cards = new HashMap<String, DevelopmentCard>();

  public DeckHash() {}

  /**
   * Create deck MD5.
   *
   * @param game current game
   * @param level deck level
   * @throws JsonProcessingException exception
   */
  public DeckHash(Game game, Level level) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    for (DevelopmentCard card : game.getOnBoardDeck(level)
        .getCardList()) {
      cards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      allCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
  }

  public Map<String, DevelopmentCard> getCards() {
    return cards;
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
