package com.hexanome16.server.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.CardJson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game class that holds all the information.
 */
public class Game {
  private final List<Deck> decks = new ArrayList<Deck>();

  public Game() {
    createDecks();
  }

  public static void main(String[] args) {
  }

  private void createDecks() {
    List<CardJson> cardJsonList = null;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      cardJsonList = objectMapper.readValue(new File("./server/src/main/resources/cards.json"),
          new TypeReference<List<CardJson>>() {
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    decks.add(deck);
  }
}
