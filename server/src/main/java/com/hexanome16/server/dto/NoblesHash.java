package com.hexanome16.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Deck;
import com.hexanome16.server.models.Noble;
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
  /**
   * The constant allNobles.
   */
  public static Map<String, Noble> allNobles = new HashMap<>();

  private final Map<String, Noble> nobles = new HashMap<>();

  /**
   * Create noble MD5.
   *
   * @param nobleDeck the noble deck
   * @throws com.fasterxml.jackson.core.JsonProcessingException exception
   */
  public NoblesHash(Deck<Noble> nobleDeck) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    for (Noble noble : nobleDeck.getCardList()) {
      nobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)), noble);
      allNobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)), noble);
    }
  }

  @Override
  public boolean isEmpty() {
    return nobles.isEmpty();
  }
}
