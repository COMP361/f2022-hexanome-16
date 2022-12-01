package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.CardJson;
import com.hexanome16.server.dto.NobleJson;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Game class that holds all the information.
 */
@Getter
public class Game {
  private final Map<Level, Deck> decks = new HashMap<>();
  private final Map<Level, Deck> onBoardDecks = new HashMap<>();

  private final long sessionId;

  private final Player[] players;
  private final String creator;
  private final String savegame;
  @Setter
  private int currentPlayerIndex = 0;
  private Deck nobleDeck = new Deck();

  private Deck onBoardNobles = new Deck();
  private final GameBank gameBank;

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId session id
   * @param players   the players
   * @param creator   the creator
   * @param savegame  the savegame
   * @throws IOException exception
   */
  @JsonCreator
  public Game(long sessionId, Player[] players, String creator, String savegame)
      throws IOException {
    this.sessionId = sessionId;
    this.players = players;
    this.creator = creator;
    this.savegame = savegame;
    gameBank = new GameBank();
    createDecks();
    createOnBoardDecks();
  }


  /**
   * Gets current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return getPlayers()[getCurrentPlayerIndex()];
  }

  private void createDecks() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    List<CardJson> cardJsonList;
    try {
      cardJsonList =
          objectMapper.readValue(new File(
                  "/app/cards.json"),
              new TypeReference<List<CardJson>>() {
              });
    } catch (Exception e) {
      cardJsonList =
          objectMapper.readValue(new File(
                  "./src/main/resources/cards.json"),
              new TypeReference<List<CardJson>>() {
              });
    }
    createBaseLevelOneDeck(cardJsonList);
    createBaseLevelTwoDeck(cardJsonList);
    createBaseLevelThreeDeck(cardJsonList);

    List<NobleJson> nobleJsonList;
    try {
      nobleJsonList =
          objectMapper.readValue(new File(
                  "/app/nobles.json"),
              new TypeReference<List<NobleJson>>() {
              });
    } catch (Exception e) {
      File file = new File(".");
      nobleJsonList =
          objectMapper.readValue(new File(
                  "./src/main/resources/nobles.json"),
              new TypeReference<List<NobleJson>>() {
              });
    }
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

  /**
   * Gets deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck getDeck(Level level) {
    return decks.get(level);
  }

  /**
   * Gets on board deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck getOnBoardDeck(Level level) {
    return onBoardDecks.get(level);
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

  // TODO: TEST CASE

  /**
   * Checks if is player's turn.
   *
   * @param player player we want to check.
   * @return true if is player's turn, false otherwise
   */
  public boolean isPlayersTurn(Player player) {
    return findPlayerIndex(player) == currentPlayerIndex;
  }

  // TODO: TEST CASE

  /**
   * Ends current player's turn and starts next player's turn.
   */
  public void endCurrentPlayersTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
  }

  // TODO: TEST CASE

  /**
   * increments game bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incGameBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                          int diamondAmount, int onyxAmount, int goldAmount) {
    getGameBank().incBank(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);
  }


  // TODO: TEST CASE

  /**
   * Increase game bank and decrease player bank by specified amount. (works the opposite for
   * negative number)
   *
   * @param player         player whose funds will get decreased.
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incGameBankFromPlayer(Player player, int rubyAmount, int emeraldAmount,
                                    int sapphireAmount, int diamondAmount, int onyxAmount,
                                    int goldAmount) {
    player.incPlayerBank(-rubyAmount, -emeraldAmount, -sapphireAmount,
        -diamondAmount, -onyxAmount, -goldAmount);

    incGameBank(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);
  }

  // TODO: TEST CASE

  /**
   * Checks if game bank has at least x amount of each gem type.
   *
   * @param rubyAmount     minimum amount or rubies player should have
   * @param emeraldAmount  minimum amount or emerald player should have
   * @param sapphireAmount minimum amount or sapphire player should have
   * @param diamondAmount  minimum amount or diamond player should have
   * @param onyxAmount     minimum amount or onyx player should have
   * @param goldAmount     minimum amount or gold player should have
   * @return true if bank has at least input amounts of each gem type, false otherwise.
   */
  public boolean gameBankHasAtLeast(int rubyAmount, int emeraldAmount, int sapphireAmount,
                                    int diamondAmount, int onyxAmount, int goldAmount) {
    return getGameBank().hasAtLeast(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);
  }


  /**
   * Game bank to purchase map.
   *
   * @return the purchase map
   */
  public PurchaseMap gameBankToPurchaseMap() {
    return getGameBank().toPurchaseMap();
  }

  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////

  // TODO: TEST CASE
  private int findPlayerIndex(Player player) {
    int i = 0;
    for (Player e : getPlayers()) {
      if (e == player) {
        return i;
      }
      i++;
    }
    return -1;
  }
}
