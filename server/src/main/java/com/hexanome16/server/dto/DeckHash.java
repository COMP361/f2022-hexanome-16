package com.hexanome16.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerLevelCard;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.Map;
import models.Level;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A deck class made for long polling.
 */
public class DeckHash implements BroadcastContent {
  /**
   * Map of all cards.
   */
  private static final Map<String, ServerLevelCard> allCards = new HashMap<>();
  private final Map<String, ServerLevelCard> cards = new HashMap<>();

  /**
   * Create deck MD5.
   *
   * @param game  current game
   * @param level deck level
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  public DeckHash(Game game, Level level) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    for (ServerLevelCard card : game.getOnBoardDeck(level).getCardList()) {
      cards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      allCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
  }

  /**
   * Get card associated with md5 hash.
   *
   * @param md5Hash hash of card
   * @return level card associated with md5 hash or null if hash is not in map
   */
  public static ServerLevelCard getCardFromDeck(String md5Hash) {
    //TODO: make sure this doesn't introduce problems (reserving/buying same card twice)
    return allCards.get(md5Hash);
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
