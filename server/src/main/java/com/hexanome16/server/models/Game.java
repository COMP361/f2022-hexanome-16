package com.hexanome16.server.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.CardJson;
import com.hexanome16.server.dto.NobleJson;
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
  private final Map<Level, Deck> onBoardDecks = new HashMap<Level, Deck>();

  private final long sessionId;
  private Deck nobleDeck = new Deck();

  private Deck onBoardNobles = new Deck();

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId session id
   * @throws IOException exception
   */
  public Game(long sessionId) throws IOException {
    this.sessionId = sessionId;
    createDecks();
    createOnBoardDecks();
  }

  private void createDecks() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<CardJson> cardJsonList =
        objectMapper.readValue(new File(
                "/app/cards.json"),
            new TypeReference<List<CardJson>>() {
            });
    createBaseLevelOneDeck(cardJsonList);
    createBaseLevelTwoDeck(cardJsonList);
    createBaseLevelThreeDeck(cardJsonList);

    List<NobleJson> nobleJsonList =
        objectMapper.readValue(new File(
                "/app/nobles.json"),
            new TypeReference<List<NobleJson>>() {
            });
    createNobleDeck(nobleJsonList);
  }

  private void createBaseLevelOneDeck(List<CardJson> cardJsonList) {
    Deck deck = new Deck();
    for (int i = 0; i < 40; i++) {
      CardJson cardJson = cardJsonList.get(i);
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(i, cardJson.getPrestigePoint(), "level_one" + i, new TokenPrice(priceMap),
              Level.ONE);
      deck.addCard(card);
    }
    deck.shuffle();
    decks.put(Level.ONE, deck);
  }

  private void createBaseLevelTwoDeck(List<CardJson> cardJsonList) {
    Deck deck = new Deck();
    for (int i = 40; i < 70; i++) {
      CardJson cardJson = cardJsonList.get(i - 40);
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(i, cardJson.getPrestigePoint(), "level_two" + (i - 40),
              new TokenPrice(priceMap),
              Level.TWO);
      deck.addCard(card);
    }
    deck.shuffle();
    decks.put(Level.TWO, deck);
  }

  private void createBaseLevelThreeDeck(List<CardJson> cardJsonList) {
    Deck deck = new Deck();
    for (int i = 70; i < 90; i++) {
      CardJson cardJson = cardJsonList.get(i - 70);
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(i, cardJson.getPrestigePoint(), "level_three" + (i - 70),
              new TokenPrice(priceMap),
              Level.THREE);
      deck.addCard(card);
    }
    deck.shuffle();
    decks.put(Level.THREE, deck);
  }

  private void createNobleDeck(List<NobleJson> nobleJsonList) {
    Deck deck = new Deck();
    for (int i = 0; i < 10; i++) {
      NobleJson nobleJson = nobleJsonList.get(i);
      PriceMap priceMap = new PriceMap(nobleJson.getRubyAmount(), nobleJson.getEmeraldAmount(),
          nobleJson.getSapphireAmount(),
          nobleJson.getDiamondAmount(), nobleJson.getOnyxAmount());
      Noble noble = new Noble(i, 3, "noble" + i, new TokenPrice(priceMap));
      deck.addCard(noble);
      deck.shuffle();
      this.nobleDeck = deck;
    }
  }

  private void createOnBoardDecks() {
    Deck baseOneDeck = new Deck();
    Deck baseTwoDeck = new Deck();
    Deck baseThreeDeck = new Deck();
    for (int i = 0; i < 4; i++) {
      baseOneDeck.addCard(decks.get(Level.ONE).nextCard());
      baseTwoDeck.addCard(decks.get(Level.TWO).nextCard());
      baseThreeDeck.addCard(decks.get(Level.THREE).nextCard());
    }
    this.onBoardDecks.put(Level.ONE, baseOneDeck);
    this.onBoardDecks.put(Level.TWO, baseTwoDeck);
    this.onBoardDecks.put(Level.THREE, baseThreeDeck);
    Deck nobleDeck = new Deck();
    for (int i = 0; i < 5; i++) {
      nobleDeck.addCard(this.nobleDeck.nextCard());
    }
    this.onBoardNobles = nobleDeck;
  }

  public Deck getDeck(Level level) {
    return decks.get(level);
  }

  public Deck getNobleDeck() {
    return this.nobleDeck;
  }

  public Deck getOnBoardDeck(Level level) {
    return onBoardDecks.get(level);
  }

  public Deck getOnBoardNobles() {
    return this.onBoardNobles;
  }

}