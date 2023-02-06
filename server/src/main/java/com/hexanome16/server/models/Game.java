package com.hexanome16.server.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.BagJson;
import com.hexanome16.server.dto.CardJson;
import com.hexanome16.server.dto.CascadeTwoJson;
import com.hexanome16.server.dto.DoubleJson;
import com.hexanome16.server.dto.NobleJson;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.winconditions.WinCondition;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Game class that holds all the information.
 */
@Getter
@ToString
public class Game {
  private final Map<Level, Deck<LevelCard>> levelDecks = new HashMap<>();

  private final Map<Level, Deck<LevelCard>> redDecks = new HashMap<>();
  private final Map<Level, Deck<LevelCard>> onBoardDecks = new HashMap<>();

  private final long sessionId;

  private final Player[] players;
  private final String creator;
  private final String savegame;
  private final GameBank gameBank;
  private final WinCondition winCondition;
  @Setter
  private int currentPlayerIndex = 0;
  private Deck<Noble> nobleDeck = new Deck<>();
  private Deck<Noble> onBoardNobles = new Deck<>();

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId    session id
   * @param players      a non-null list of players
   * @param creator      the creator
   * @param savegame     the savegame
   * @param winCondition the win condition
   * @throws java.io.IOException exception
   */
  public Game(long sessionId, @NonNull Player[] players, String creator, String savegame,
              WinCondition winCondition)
      throws IOException {
    this.sessionId = sessionId;
    this.players = players.clone();
    this.creator = creator;
    this.savegame = savegame;
    this.winCondition = winCondition;
    gameBank = new GameBank();
    createDecks();
    createOnBoardDecks();
    createOnBoardRedDecks();
  }

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId session id
   * @param payload   the payload
   * @throws java.io.IOException exception
   */
  public Game(long sessionId, SessionJson payload) throws IOException {
    this(sessionId, payload.getPlayers(), payload.getCreator(), payload.getSavegame(),
        payload.getWinCondition());
  }


  /**
   * Gets current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return getPlayers()[getCurrentPlayerIndex()];
  }

  /**
   * Get copy of array of players.
   *
   * @return a cloned copy of the internal array of players.
   */
  public Player[] getPlayers() {
    return players.clone();
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

    List<BagJson> bagJsonList;
    try {
      bagJsonList =
          objectMapper.readValue(new File(
                  "/app/bag.json"),
              new TypeReference<List<BagJson>>() {
              });
    } catch (Exception e) {
      File file = new File(".");
      bagJsonList =
          objectMapper.readValue(new File(
                  "./src/main/resources/bag.json"),
              new TypeReference<List<BagJson>>() {
              });
    }
    createBagDeck(bagJsonList);

    createGoldDeck();

    List<DoubleJson> doubleJsonList;
    try {
      doubleJsonList =
          objectMapper.readValue(new File(
                  "/app/double.json"),
              new TypeReference<List<DoubleJson>>() {
              });
    } catch (Exception e) {
      File file = new File(".");
      doubleJsonList =
          objectMapper.readValue(new File(
                  "./src/main/resources/double.json"),
              new TypeReference<List<DoubleJson>>() {
              });
    }
    createDoubleDeck(doubleJsonList);

    createNobleReserveDeck();

    createBagCascadeDeck();

    createSacrificeDeck();

    List<CascadeTwoJson> cascadeTwoJsonList;
    try {
      cascadeTwoJsonList =
          objectMapper.readValue(new File(
                  "/app/cascade_two.json"),
              new TypeReference<List<CascadeTwoJson>>() {
              });
    } catch (Exception e) {
      File file = new File(".");
      cascadeTwoJsonList =
          objectMapper.readValue(new File(
                  "./src/main/resources/cascade_two.json"),
              new TypeReference<List<CascadeTwoJson>>() {
              });
    }
    createCascadeTwoDeck(cascadeTwoJsonList);
  }

  private void createBaseLevelOneDeck(List<CardJson> cardJsonList) {
    Deck<LevelCard> deck = new Deck<>();
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
    levelDecks.put(Level.ONE, deck);
  }

  private void createBaseLevelTwoDeck(List<CardJson> cardJsonList) {
    Deck<LevelCard> deck = new Deck<>();
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
    levelDecks.put(Level.TWO, deck);
  }

  private void createBaseLevelThreeDeck(List<CardJson> cardJsonList) {
    Deck<LevelCard> deck = new Deck<>();
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
    levelDecks.put(Level.THREE, deck);
  }

  private void createNobleDeck(List<NobleJson> nobleJsonList) {
    Deck<Noble> deck = new Deck<>();
    for (int i = 0; i < 10; i++) {
      NobleJson nobleJson = nobleJsonList.get(i);
      PriceMap priceMap = new PriceMap(nobleJson.getRubyAmount(), nobleJson.getEmeraldAmount(),
          nobleJson.getSapphireAmount(),
          nobleJson.getDiamondAmount(), nobleJson.getOnyxAmount());
      Noble noble = new Noble(i, 3, "noble" + i, new TokenPrice(priceMap));
      deck.addCard(noble);
    }
    deck.shuffle();
    this.nobleDeck = deck;
  }

  private void createBagDeck(List<BagJson> bagJsonList) {
    Deck<LevelCard> deck = new Deck<>();
    for (int i = 0; i < 4; i++) {
      BagJson bagJson = bagJsonList.get(i);
      PriceMap priceMap = new PriceMap(bagJson.getRubyAmount(), bagJson.getEmeraldAmount(),
          bagJson.getSapphireAmount(),
          bagJson.getDiamondAmount(), bagJson.getOnyxAmount());
      LevelCard bag = new LevelCard(bagJson.getId(), 0,
          "bag" + bagJson.getId(),
          new TokenPrice(priceMap),
          Level.REDONE);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDONE, deck);
  }

  private void createGoldDeck() {
    Deck<LevelCard> deck = redDecks.get(Level.REDONE);
    int[][] prices =
      {{3, 0, 0, 0, 0}, {0, 3, 0, 0, 0}, {0, 0, 3, 0, 0}, {0, 0, 0, 3, 0}, {0, 0, 0, 0, 3}};
    for (int i = 0; i < 4; i++) {
      PriceMap priceMap =
          new PriceMap(prices[i][0], prices[i][1], prices[i][2], prices[i][3], prices[i][4]);
      LevelCard gold = new LevelCard(i, 0,
          "gold" + i,
          new TokenPrice(priceMap),
          Level.REDONE);
      deck.addCard(gold);
    }
    deck.shuffle();
    redDecks.put(Level.REDONE, deck);
  }

  private void createDoubleDeck(List<DoubleJson> doubleJsonList) {
    Deck<LevelCard> deck = new Deck<>();
    for (int i = 0; i < 4; i++) {
      DoubleJson doubleJson = doubleJsonList.get(i);
      PriceMap priceMap = new PriceMap(doubleJson.getRubyAmount(), doubleJson.getEmeraldAmount(),
          doubleJson.getSapphireAmount(),
          doubleJson.getDiamondAmount(), doubleJson.getOnyxAmount());
      LevelCard bag = new LevelCard(doubleJson.getId(), 0,
          "double" + doubleJson.getId(),
          new TokenPrice(priceMap),
          Level.REDTWO);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  private void createNobleReserveDeck() {
    Deck<LevelCard> deck = redDecks.get(Level.REDTWO);
    int[][] prices = {{2, 2, 2, 0, 2}, {2, 0, 2, 2, 2}, {0, 2, 2, 2, 2}};
    for (int i = 0; i < 2; i++) {
      PriceMap priceMap =
          new PriceMap(prices[i][0], prices[i][1], prices[i][2], prices[i][3], prices[i][4]);
      LevelCard bag = new LevelCard(i, 1,
          "noble_reserve" + i,
          new TokenPrice(priceMap),
          Level.REDTWO);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  private void createBagCascadeDeck() {
    Deck<LevelCard> deck = redDecks.get(Level.REDTWO);
    int[][] prices = {{3, 4, 0, 0, 1}, {0, 0, 3, 4, 1}};
    for (int i = 0; i < 1; i++) {
      PriceMap priceMap =
          new PriceMap(prices[i][0], prices[i][1], prices[i][2], prices[i][3], prices[i][4]);
      LevelCard bag = new LevelCard(i, 0,
          "bag_cascade" + i,
          new TokenPrice(priceMap),
          Level.REDTWO);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  private void createSacrificeDeck() {
    Deck<LevelCard> deck = new Deck<>();
    Gem[] gems = {Gem.SAPPHIRE, Gem.RUBY, Gem.EMERALD, Gem.ONYX, Gem.DIAMOND};
    for (int i = 0; i < 4; i++) {
      CardPrice price = new CardPrice(gems[i]);
      LevelCard bag = new LevelCard(i, 3,
          "sacrifice" + i,
          price,
          Level.REDTHREE);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTHREE, deck);
  }

  private void createCascadeTwoDeck(List<CascadeTwoJson> cascadeTwoJsonList) {
    Deck<LevelCard> deck = redDecks.get(Level.REDTHREE);
    for (int i = 0; i < 4; i++) {
      CascadeTwoJson cascadeTwoJson = cascadeTwoJsonList.get(i);
      PriceMap priceMap =
          new PriceMap(cascadeTwoJson.getRubyAmount(), cascadeTwoJson.getEmeraldAmount(),
              cascadeTwoJson.getSapphireAmount(),
              cascadeTwoJson.getDiamondAmount(), cascadeTwoJson.getOnyxAmount());
      LevelCard bag = new LevelCard(cascadeTwoJson.getId(), 0,
          "cascade_two" + cascadeTwoJson.getId(),
          new TokenPrice(priceMap),
          Level.REDTHREE);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTHREE, deck);
  }

  private void createOnBoardDecks() {
    Deck<LevelCard> baseOneDeck = new Deck<>();
    Deck<LevelCard> baseTwoDeck = new Deck<>();
    Deck<LevelCard> baseThreeDeck = new Deck<>();

    // lay the cards face up on the game board
    for (int i = 0; i < 4; i++) {
      LevelCard levelOne = levelDecks.get(Level.ONE).nextCard();
      levelOne.setIsFaceDown(false);
      baseOneDeck.addCard(levelOne);

      LevelCard levelTwo = levelDecks.get(Level.TWO).nextCard();
      levelTwo.setIsFaceDown(false);
      baseTwoDeck.addCard(levelTwo);

      LevelCard levelThree = levelDecks.get(Level.THREE).nextCard();
      levelThree.setIsFaceDown(false);
      baseThreeDeck.addCard(levelThree);
    }

    // make into data structures (hash map)
    this.onBoardDecks.put(Level.ONE, baseOneDeck);
    this.onBoardDecks.put(Level.TWO, baseTwoDeck);
    this.onBoardDecks.put(Level.THREE, baseThreeDeck);

    // same thing but with the nobles
    Deck<Noble> nobleDeck = new Deck<>();
    for (int i = 0; i < 5; i++) {
      nobleDeck.addCard(this.nobleDeck.nextCard());
    }
    this.onBoardNobles = nobleDeck;
  }

  private void createOnBoardRedDecks() {
    Deck<LevelCard> redOneDeck = new Deck<>();
    Deck<LevelCard> redTwoDeck = new Deck<>();
    Deck<LevelCard> redThreeDeck = new Deck<>();
    for (int i = 0; i < 2; i++) {
      redOneDeck.addCard(redDecks.get(Level.REDONE).nextCard());
      redTwoDeck.addCard(redDecks.get(Level.REDTWO).nextCard());
      redThreeDeck.addCard(redDecks.get(Level.REDTHREE).nextCard());
    }
    this.onBoardDecks.put(Level.REDONE, redOneDeck);
    this.onBoardDecks.put(Level.REDTWO, redTwoDeck);
    this.onBoardDecks.put(Level.REDTHREE, redThreeDeck);
  }

  /**
   * Gets deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<LevelCard> getLevelDeck(Level level) {
    return levelDecks.get(level);
  }

  /**
   * Gets on board deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<LevelCard> getOnBoardDeck(Level level) {
    return onBoardDecks.get(level);
  }


  /**
   * Adds a new card from deck to game board.
   *
   * @param level level of the deck
   */
  public void addOnBoardCard(Level level) {
    LevelCard card = this.levelDecks.get(level).nextCard();
    card.setIsFaceDown(false);
    this.onBoardDecks.get(level).addCard(card);
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
  public boolean isNotPlayersTurn(Player player) {
    return findPlayerIndex(player) != currentPlayerIndex;
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

  /**
   * Increments the game bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to increment
   *                    the game bank.
   */
  public void incGameBank(PurchaseMap purchaseMap) {
    getGameBank().incBank(purchaseMap.getRubyAmount(), purchaseMap.getEmeraldAmount(),
        purchaseMap.getSapphireAmount(), purchaseMap.getDiamondAmount(),
        purchaseMap.getOnyxAmount(), purchaseMap.getGoldAmount());
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
   * Gets all the token types one can take 2 of. (Gold gems are also part of the list
   * (shouldn't really be the case but just saying))
   *
   * @return An array list of all such token types
   */
  public ArrayList<Gem> availableTwoTokensType() {
    return getGameBank().availableTwoTokensType();
  }

  /**
   * Returns true if one can take 2 tokens of a given gem type. False otherwise.
   *
   * @param gem gem we want to take 2 of.
   * @return True if one can take 2 tokens of a given gem type. False otherwise.
   */
  public boolean allowedTakeTwoOf(Gem gem) {
    return availableTwoTokensType().contains(gem);
  }

  /**
   * Gives 2 tokens of type gem to player.
   *
   * @param gem    Gem we want to give 2 of.
   * @param player player who will receive the gems.
   */
  public void giveTwoOf(Gem gem, Player player) {

    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(gem, -2);
    incGameBank(new PurchaseMap(gemIntegerMapGame));
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(gem, 2);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
  }


  /**
   * Gets all the token types one can take 2 of.
   *
   * @return An array list of all such token types
   */
  public ArrayList<Gem> availableThreeTokensType() {
    return getGameBank().availableThreeTokensType();
  }

  /**
   * Returns true if one can take 3 tokens of the given gem types. False otherwise.
   *
   * @param gem1 first gem type we want.
   * @param gem2 second gem type we want.
   * @param gem3 third gem type we want.
   * @return True if one can take 3 tokens of the given gem types. False otherwise.
   */
  public boolean allowedTakeThreeOf(Gem gem1, Gem gem2, Gem gem3) {
    ArrayList<Gem> available = availableThreeTokensType();
    return available.contains(gem1)
        && available.contains(gem2)
        && available.contains(gem3)
        && Gem.areDistinct(gem1, gem2, gem3);
  }

  /**
   * Gives 3 tokens of 3 different types to player.
   *
   * @param desiredGemOne   First gem we want to take one of.
   * @param desiredGemTwo   Second gem we want to take one of.
   * @param desiredGemThree Third gem we want to take one of.
   * @param player          player who will receive the gems.
   */
  public void giveThreeOf(Gem desiredGemOne, Gem desiredGemTwo, Gem desiredGemThree,
                          Player player) {
    // Remove from game bank
    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(desiredGemOne, -1);
    gemIntegerMapGame.put(desiredGemTwo, -1);
    gemIntegerMapGame.put(desiredGemThree, -1);
    incGameBank(new PurchaseMap(gemIntegerMapGame));

    // Give to player bank
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(desiredGemOne, 1);
    gemIntegerMapPlayer.put(desiredGemTwo, 1);
    gemIntegerMapPlayer.put(desiredGemThree, 1);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
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
