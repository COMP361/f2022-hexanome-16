package com.hexanome16.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.CardJson;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game class that holds all the information.
 */
public class Game {
  private final Map<Level, Deck> decks = new HashMap<Level, Deck>();

  private final long sessionId;

  public Game(long sessionId) throws IOException {
    this.sessionId = sessionId;
    createDecks();
  }

  /**
   * debugging purpose.
   *
   * @param args main method args
   * @throws JsonProcessingException e
   */
  public static void main(String[] args) throws IOException {
    Game game = new Game(0);
    ObjectMapper objectMapper = new ObjectMapper();
    System.out.println(objectMapper.writeValueAsString(game.getDeck(Level.ONE).nextCard()));
  }

  private void createDecks() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<CardJson> cardJsonList =
        objectMapper.readValue(new File(
                "/app/cards.json"),
            new TypeReference<List<CardJson>>() {
            });
    createBaseLevelOneDeck(cardJsonList);
  }

  private void createBaseLevelOneDeck(List<CardJson> cardJsonList) {
    Deck deck = new Deck(Level.ONE);
    for (int i = 0; i < 40; i++) {
      CardJson cardJson = cardJsonList.get(i);
      Level level = null;
      switch (cardJson.getLevel()) {
        default:
        case "ONE":
          level = Level.ONE;
          break;
        case "TWO":
          level = Level.TWO;
          break;
        case "THREE":
          level = Level.THREE;
          break;
      }
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(i, cardJson.getPrestigePoint(), "level_one" + i, new TokenPrice(priceMap),
              level);
      deck.addCard(card);
    }
    deck.shuffle();
    decks.put(Level.ONE, deck);
  }

  public Deck getDeck(Level level) {
    return decks.get(level);
  }
}
