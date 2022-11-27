package com.hexanome16.server.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.CardJson;
import com.hexanome16.server.dto.NobleJson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game class that holds all the information.
 */
public class Game {
  //store all the games here
  private static final Map<Long, Game> gameMap = new HashMap<>();
  private final Map<Level, Deck> decks = new HashMap<>();
  private final Map<Level, Deck> onBoardDecks = new HashMap<>();

  private final long sessionId;

  private final ArrayList<Player> participants = new ArrayList<>();
  private int currentPlayerIndex = 0;
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

  public static Map<Long, Game> getGameMap() {
    return gameMap;
  }

  public ArrayList<Player> getParticipants() {
    return participants;
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
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
          new LevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
              "level_one" + cardJson.getId(), new TokenPrice(priceMap),
              Level.ONE);
      deck.addCard(card);
    }
    deck.shuffle();
    decks.put(Level.ONE, deck);
  }

  private void createBaseLevelTwoDeck(List<CardJson> cardJsonList) {
    Deck deck = new Deck();
    for (int i = 40; i < 70; i++) {
      CardJson cardJson = cardJsonList.get(i);
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
              "level_two" + cardJson.getId(),
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
      CardJson cardJson = cardJsonList.get(i);
      PriceMap priceMap = new PriceMap(cardJson.getRubyAmount(), cardJson.getEmeraldAmount(),
          cardJson.getSapphireAmount(),
          cardJson.getDiamondAmount(), cardJson.getOnyxAmount());
      LevelCard card =
          new LevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
              "level_three" + cardJson.getId(),
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

  /**
   * Adds a new card from deck to game board.
   *
   * @param level level of the deck
   */
  public void addOnBoardCard(Level level) {
    this.onBoardDecks.get(level).addCard(this.decks.get(level).nextCard());
  }

  /**
   * Removes a card from game board.
   *
   * @param card card to be removed
   */
  public void removeOnBoardCard(LevelCard card) {
    this.onBoardDecks.get(card.getLevel()).removeCard(card);
  }
}
