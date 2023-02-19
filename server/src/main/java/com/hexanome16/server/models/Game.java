package com.hexanome16.server.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.NoblesHash;
import com.hexanome16.server.dto.SessionJson;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.util.BroadcastMap;
import dto.BagJson;
import dto.CardJson;
import dto.CascadeTwoJson;
import dto.DoubleJson;
import dto.NobleJson;
import dto.PlayerJson;
import dto.WinJson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import models.Level;
import models.price.Gem;
import models.price.PriceMap;
import models.price.PurchaseMap;

/**
 * Game class that holds all the information.
 */
@Getter
@ToString
public class Game {
  private final BroadcastMap broadcastContentManagerMap = new BroadcastMap();
  private final Map<Level, Deck<LevelCard>> levelDecks = new HashMap<>();

  private final Map<Level, Deck<LevelCard>> redDecks = new HashMap<>();
  private final Map<Level, Deck<LevelCard>> onBoardDecks = new HashMap<>();
  private final int levelCardsTotal = 90;

  private final long sessionId;

  private final Player[] players;
  private final String creator;
  private final String savegame;
  private final GameBank gameBank;
  private final WinCondition winCondition;
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
   * @throws java.io.IOException object mapper IO exception
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
    createBroadcastContentManagerMap();
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

  private void createBroadcastContentManagerMap() {
    try {
      for (Level level : Level.values()) {
        BroadcastContentManager<DeckHash> broadcastContentManager =
            new BroadcastContentManager<>(new DeckHash(this, level));
        broadcastContentManagerMap.put(level.name(), broadcastContentManager);
      }
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          new BroadcastContentManager<>(
              new PlayerJson(getCurrentPlayer().getName()));
      BroadcastContentManager<WinJson> broadcastContentManagerWinners =
          new BroadcastContentManager<>(new WinJson());
      BroadcastContentManager<NoblesHash> broadcastContentManagerNoble =
          new BroadcastContentManager<>(new NoblesHash(this));
      broadcastContentManagerMap.put("player", broadcastContentManagerPlayer);
      broadcastContentManagerMap.put("winners", broadcastContentManagerWinners);
      broadcastContentManagerMap.put("noble", broadcastContentManagerNoble);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return players[currentPlayerIndex];
  }

  /**
   * Get copy of array of players.
   *
   * @return a cloned copy of the internal array of players.
   */
  public Player[] getPlayers() {
    return players.clone();
  }

  /**
   * Start next player's turn.
   */
  public void goToNextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
  }

  private void createDecks() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    CardJson[] cardJsonList;
    try {
      cardJsonList = objectMapper.readValue(new File("/app/cards.json"), CardJson[].class);
    } catch (Exception e) {
      cardJsonList =
          objectMapper.readValue(new File("./src/main/resources/cards.json"), CardJson[].class);
    }
    createBaseLevelDecks(cardJsonList);

    NobleJson[] nobleJsonList;
    try {
      nobleJsonList = objectMapper.readValue(new File("/app/nobles.json"), NobleJson[].class);
    } catch (Exception e) {
      nobleJsonList =
          objectMapper.readValue(new File("./src/main/resources/nobles.json"), NobleJson[].class);
    }
    createNobleDeck(nobleJsonList);

    BagJson[] bagJsonList;
    try {
      bagJsonList = objectMapper.readValue(new File("/app/bag.json"), BagJson[].class);
    } catch (Exception e) {
      bagJsonList =
          objectMapper.readValue(new File("./src/main/resources/bag.json"), BagJson[].class);
    }
    createBagDeck(bagJsonList);

    createGoldDeck();

    DoubleJson[] doubleJsonList;
    try {
      doubleJsonList = objectMapper.readValue(new File("/app/double.json"), DoubleJson[].class);
    } catch (Exception e) {
      doubleJsonList =
          objectMapper.readValue(new File("./src/main/resources/double.json"), DoubleJson[].class);
    }
    createDoubleDeck(doubleJsonList);

    createNobleReserveDeck();

    createBagCascadeDeck();

    createSacrificeDeck();

    CascadeTwoJson[] cascadeTwoJsonList;
    try {
      cascadeTwoJsonList =
          objectMapper.readValue(new File("/app/cascade_two.json"), CascadeTwoJson[].class);
    } catch (Exception e) {
      cascadeTwoJsonList =
          objectMapper.readValue(new File("./src/main/resources/cascade_two.json"),
              CascadeTwoJson[].class);
    }
    createCascadeTwoDeck(cascadeTwoJsonList);
  }

  private void createBaseLevelDecks(CardJson[] cardJsonList) {
    levelDecks.put(Level.ONE, new Deck<>());
    levelDecks.put(Level.TWO, new Deck<>());
    levelDecks.put(Level.THREE, new Deck<>());
    for (int i = 0; i < levelCardsTotal; i++) {
      CardJson cardJson = cardJsonList[i];
      String textureLevel = i < 40 ? "level_one" : i < 70 ? "level_two" : "level_three";
      Level level = i < 40 ? Level.ONE : i < 70 ? Level.TWO : Level.THREE;
      LevelCard card = new LevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
          textureLevel + cardJson.getId(), cardJson.getPrice(), level);
      levelDecks.get(level).addCard(card);
    }
  }

  private void createNobleDeck(NobleJson[] nobleJsonList) {
    Deck<Noble> deck = new Deck<>();
    for (int i = 0; i < 10; i++) {
      NobleJson nobleJson = nobleJsonList[i];
      Noble noble = new Noble(i, 3, "noble" + i, nobleJson.getPrice());
      deck.addCard(noble);
    }
    deck.shuffle();
    this.nobleDeck = deck;
  }

  private void createBagDeck(BagJson[] bagJsonList) {
    Deck<LevelCard> deck = new Deck<>();
    for (int i = 0; i < 4; i++) {
      BagJson bagJson = bagJsonList[i];
      LevelCard bag = new LevelCard(bagJson.getId(), 0,
          "bag" + bagJson.getId(),
          bagJson.getPrice(),
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
          priceMap,
          Level.REDONE);
      deck.addCard(gold);
    }
    deck.shuffle();
    redDecks.put(Level.REDONE, deck);
  }

  private void createDoubleDeck(DoubleJson[] doubleJsonList) {
    Deck<LevelCard> deck = new Deck<>();
    for (int i = 0; i < 4; i++) {
      DoubleJson doubleJson = doubleJsonList[i];
      LevelCard bag = new LevelCard(doubleJson.getId(), 0,
          "double" + doubleJson.getId(),
          doubleJson.getPrice(),
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
          priceMap,
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
          priceMap,
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
      PriceMap priceMap = new PriceMap(0, 0, 0, 0, 0);
      priceMap.addGems(gems[i], 1);
      LevelCard bag = new LevelCard(i, 3,
          "sacrifice" + i,
          priceMap,
          Level.REDTHREE);
      deck.addCard(bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTHREE, deck);
  }

  private void createCascadeTwoDeck(CascadeTwoJson[] cascadeTwoJsonList) {
    Deck<LevelCard> deck = redDecks.get(Level.REDTHREE);
    for (int i = 0; i < 4; i++) {
      CascadeTwoJson cascadeTwoJson = cascadeTwoJsonList[i];
      LevelCard bag = new LevelCard(cascadeTwoJson.getId(), 0,
          "cascade_two" + cascadeTwoJson.getId(),
          cascadeTwoJson.getPrice(),
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


  /**
   * Checks if is player's turn.
   *
   * @param player player we want to check.
   * @return true if is player's turn, false otherwise
   */
  public boolean isNotPlayersTurn(Player player) {
    return findPlayerIndex(player) != currentPlayerIndex;
  }

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
    getGameBank().addGemsToBank(new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Increments the game bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to increment
   *                    the game bank.
   */
  public void incGameBank(PurchaseMap purchaseMap) {
    getGameBank().addGemsToBank(purchaseMap);
  }

  /**
   * Decrements game bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to decrease ruby stack by.
   * @param emeraldAmount  amount to decrease emerald stack by.
   * @param sapphireAmount amount to decrease sapphire stack by.
   * @param diamondAmount  amount to decrease diamond stack by.
   * @param onyxAmount     amount to decrease onyx stack by.
   * @param goldAmount     amount to decrease gold stack by.
   */
  public void decGameBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                          int diamondAmount, int onyxAmount, int goldAmount) {
    getGameBank().removeGemsFromBank(new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Decrements the game bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to decrement
   *                    the game bank.
   */
  public void decGameBank(PurchaseMap purchaseMap) {
    getGameBank().removeGemsFromBank(purchaseMap);
  }

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
    player.getBank().removeGemsFromBank(new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount));

    incGameBank(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);
  }


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
    return getGameBank().toPurchaseMap().canBeUsedToBuy(
        new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
            diamondAmount, onyxAmount, goldAmount));
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
    gemIntegerMapGame.put(gem, 2);
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
    gemIntegerMapGame.put(desiredGemOne, 1);
    gemIntegerMapGame.put(desiredGemTwo, 1);
    gemIntegerMapGame.put(desiredGemThree, 1);
    gameBank.removeGemsFromBank(new PurchaseMap(gemIntegerMapGame));

    // Give to player bank
    Map<Gem, Integer> gemIntegerMapPlayer = new HashMap<>();
    gemIntegerMapPlayer.put(desiredGemOne, 1);
    gemIntegerMapPlayer.put(desiredGemTwo, 1);
    gemIntegerMapPlayer.put(desiredGemThree, 1);
    player.incPlayerBank(new PurchaseMap(gemIntegerMapPlayer));
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
