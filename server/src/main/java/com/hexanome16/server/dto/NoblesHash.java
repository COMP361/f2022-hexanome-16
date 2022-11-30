package com.hexanome16.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.DevelopmentCard;
import com.hexanome16.server.models.Game;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A noble class made for long polling.
 */
@Data
@NoArgsConstructor
public class NoblesHash implements BroadcastContent {
  public static Map<String, DevelopmentCard> allNobles = new HashMap<String, DevelopmentCard>();

  private final Map<String, DevelopmentCard> nobles = new HashMap<String, DevelopmentCard>();

  /**
   * Create noble MD5.
   *
   * @param game current game
   * @throws JsonProcessingException exception
   */
  public NoblesHash(Game game) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    for (DevelopmentCard card : game.getOnBoardNobles().getCardList()) {
      nobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
      allNobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
  }

  @Override
  public boolean isEmpty() {
    return nobles.isEmpty();
  }
}
