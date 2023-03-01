package dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import models.Level;
import models.LevelCard;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A deck class made for long polling.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeckJson implements BroadcastContent {
  private Map<String, LevelCard> cards;
  private Level deckLevel;

  /**
   * Default constructor.
   *
   * @param level the level of the deck
   */
  public DeckJson(Level level) {
    cards = new HashMap<>();
    deckLevel = level;
  }

  /**
   * Create deck MD5.
   *
   * @param cardList the card list
   * @param level    the level of the deck
   */
  @SneakyThrows
  public DeckJson(List<? extends LevelCard> cardList, Level level) {
    deckLevel = level;
    cards = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    for (LevelCard card : cardList) {
      cards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
