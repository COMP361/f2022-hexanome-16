package com.hexanome16.server.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.common.dto.cards.CardJson;
import com.hexanome16.common.dto.cards.DevelopmentCardJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Game class that holds all the information.
 */
@Getter
@ToString
public class Game {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<Level, Deck<ServerLevelCard>> levelDecks;
  private final Map<Level, Deck<ServerLevelCard>> redDecks;
  private final Map<Level, Deck<ServerLevelCard>> onBoardDecks;
  private final int levelCardsTotal = 90;
  private final long sessionId;
  private final ServerPlayer[] players;
  private final String creator;
  private final String savegame;
  private final GameBank gameBank;
  private final WinCondition[] winConditions;
  private final Map<String, ServerLevelCard> remainingCards;
  /**
   * Remaining nobles to be acquired.
   */
  private final Map<String, ServerNoble> remainingNobles;
  private final Map<RouteType, TradePost> tradePosts;
  private BroadcastMap broadcastContentManagerMap;
  private int currentPlayerIndex = 0;
  /**
   * Deck of all possible nobles.
   */
  private Deck<ServerNoble> nobleDeck;
  /**
   * Selected nobles for this game (5 of the 10 possible).
   */
  private Deck<ServerNoble> onBoardNobles;

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId     session id
   * @param players       a non-null list of players
   * @param creator       the creator
   * @param savegame      the savegame
   * @param winConditions the win conditions
   */
  @SneakyThrows
  Game(long sessionId, @NonNull ServerPlayer[] players, String creator, String savegame,
       WinCondition[] winConditions, boolean isTradeRoute, boolean isCities) {
    this.sessionId = sessionId;
    this.players = players.clone();
    this.creator = creator;
    this.savegame = savegame;
    this.winConditions = winConditions;
    this.gameBank = new GameBank();
    this.levelDecks = createLevelMap();
    this.redDecks = createRedMap();
    this.onBoardDecks = createBoardMap();
    this.nobleDeck = new Deck<>();
    this.onBoardNobles = new Deck<>();
    this.remainingCards = new HashMap<>();
    this.remainingNobles = new HashMap<>();
    createDecks();
    createOnBoardDecks();
    createOnBoardRedDecks();
    this.tradePosts = new HashMap<>();
    if (isTradeRoute) {
      for (RouteType route : RouteType.values()) {
        tradePosts.put(route, new TradePost(route));
      }
    }
  }

  /**
   * Game constructor, create a new with a unique session id.
   *
   * @param sessionId session id
   * @param payload   the payload
   */
  Game(long sessionId, SessionJson payload) {
    this(sessionId, IntStream.range(0, payload.getPlayers().length)
            .mapToObj(i -> new ServerPlayer(payload.getPlayers()[i].getName(),
                payload.getPlayers()[i].getPreferredColour(), i)).toArray(ServerPlayer[]::new),
        payload.getCreator(), payload.getSavegame(),
        new WinCondition[] {WinCondition.fromServerName(payload.getGame())},
        payload.getGame().contains("TradeRoutes"), payload.getGame().contains("Cities"));
  }

  /**
   * Creates a new game instance from a session payload.
   *
   * @param sessionId session id
   * @param payload   the payload
   * @return the game
   */
  @SneakyThrows
  public static Game create(long sessionId, SessionJson payload) {
    System.out.println(payload);
    Game game = new Game(sessionId, payload);
    game.init();
    return game;
  }

  /**
   * Creates a new game instance.
   *
   * @param sessionId     session id
   * @param players       a non-null list of players
   * @param creator       the creator
   * @param savegame      the savegame
   * @param winConditions the win conditions
   * @param isTradeRoute  if trade route expansion
   * @param isCities      if cities expansion
   * @return the game
   */
  @SneakyThrows
  public static Game create(long sessionId, ServerPlayer[] players, String creator, String savegame,
                            WinCondition[] winConditions, boolean isTradeRoute, boolean isCities) {
    Game game =
        new Game(sessionId, players, creator, savegame, winConditions, isTradeRoute, isCities);
    game.init();
    return game;
  }

  @SneakyThrows
  private void init() {
    this.broadcastContentManagerMap = new BroadcastMap(this);
  }

  private Map<Level, Deck<ServerLevelCard>> createLevelMap() {
    HashMap<Level, Deck<ServerLevelCard>> levelMap = new HashMap<>();
    levelMap.put(Level.ONE, new Deck<>());
    levelMap.put(Level.TWO, new Deck<>());
    levelMap.put(Level.THREE, new Deck<>());
    return levelMap;
  }

  private Map<Level, Deck<ServerLevelCard>> createRedMap() {
    HashMap<Level, Deck<ServerLevelCard>> levelMap = new HashMap<>();
    levelMap.put(Level.REDONE, new Deck<>());
    levelMap.put(Level.REDTWO, new Deck<>());
    levelMap.put(Level.REDTHREE, new Deck<>());
    return levelMap;
  }

  private Map<Level, Deck<ServerLevelCard>> createBoardMap() {
    Map<Level, Deck<ServerLevelCard>> levelMap = createLevelMap();
    levelMap.putAll(createRedMap());
    return levelMap;
  }

  /**
   * Gets current player.
   *
   * @return the current player
   */
  public ServerPlayer getCurrentPlayer() {
    return players[currentPlayerIndex];
  }

  /**
   * Get copy of array of players.
   *
   * @return a cloned copy of the internal array of players.
   */
  public ServerPlayer[] getPlayers() {
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
    createBaseLevelDecks();
    createNobleDeck();
    createBagDeck();
    createGoldDeck();
    createDoubleDeck();
    createNobleReserveDeck();
    createBagCascadeDeck();
    createSacrificeDeck();
    createCascadeTwoDeck();
  }

  @SneakyThrows
  private void createBaseLevelDecks() {
    DevelopmentCardJson[] cardJsonList;
    try {
      cardJsonList = objectMapper.readValue(new File("/app/cards.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      cardJsonList =
          objectMapper.readValue(new File("./src/main/resources/cards.json"),
              DevelopmentCardJson[].class);
    }
    levelDecks.put(Level.ONE, new Deck<>());
    levelDecks.put(Level.TWO, new Deck<>());
    levelDecks.put(Level.THREE, new Deck<>());
    for (int i = 0; i < levelCardsTotal; i++) {
      DevelopmentCardJson cardJson = cardJsonList[i];
      String textureLevel = i < 40 ? "level_one" : i < 70 ? "level_two" : "level_three";
      Level level = i < 40 ? Level.ONE : i < 70 ? Level.TWO : Level.THREE;
      Gem gem = Gem.valueOf(cardJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard card = new ServerLevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
          textureLevel + cardJson.getId(), cardJson.getPrice(), level, gemBonus);
      levelDecks.get(level).addCard(card);
      if (level != Level.ONE) {
        levelDecks.get(level).shuffle();
      }
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
    levelDecks.get(Level.ONE).reverse();
  }

  @SneakyThrows
  private void createNobleDeck() {
    CardJson[] nobleJsonList;
    try {
      nobleJsonList = objectMapper.readValue(new File("/app/nobles.json"), CardJson[].class);
    } catch (Exception e) {
      nobleJsonList =
          objectMapper.readValue(new File("./src/main/resources/nobles.json"), CardJson[].class);
    }
    Deck<ServerNoble> deck = new Deck<>();
    for (CardJson nobleJson : nobleJsonList) {
      ServerNoble noble = new ServerNoble(nobleJson.getId(), nobleJson.getPrestigePoint(),
          "noble" + nobleJson.getId(), nobleJson.getPrice());
      deck.addCard(noble);
      remainingNobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)), noble);
    }
    deck.shuffle();
    this.nobleDeck = deck;
  }

  @SneakyThrows
  private void createBagDeck() {
    DevelopmentCardJson[] bagJsonList;
    try {
      bagJsonList = objectMapper.readValue(new File("/app/bag.json"), DevelopmentCardJson[].class);
    } catch (Exception e) {
      bagJsonList = objectMapper.readValue(new File("./src/main/resources/bag.json"),
          DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = new Deck<>();
    for (DevelopmentCardJson bagJson : bagJsonList) {
      Gem gem = Gem.valueOf(bagJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard bag = new ServerLevelCard(bagJson.getId(), 0,
          "bag" + bagJson.getId(),
          bagJson.getPrice(),
          Level.REDONE, gemBonus);
      deck.addCard(bag);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDONE, deck);
  }

  @SneakyThrows
  @SuppressWarnings("checkstyle:Indentation")
  private void createGoldDeck() {
    DevelopmentCardJson[] goldJsonList;
    try {
      goldJsonList = objectMapper.readValue(new File("/app/gold.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      goldJsonList = objectMapper.readValue(new File("./src/main/resources/gold.json"),
          DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = redDecks.get(Level.REDONE);
    for (DevelopmentCardJson goldJson : goldJsonList) {
      Gem gem = Gem.valueOf(goldJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 2));
      ServerLevelCard gold = new ServerLevelCard(goldJson.getId(), goldJson.getPrestigePoint(),
          "gold" + goldJson.getId(),
          goldJson.getPrice(),
          Level.REDONE, gemBonus);
      deck.addCard(gold);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(gold)), gold);
    }
    deck.shuffle();
    redDecks.put(Level.REDONE, deck);
  }

  @SneakyThrows
  private void createDoubleDeck() {
    DevelopmentCardJson[] doubleJsonList;
    try {
      doubleJsonList = objectMapper.readValue(new File("/app/double.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      doubleJsonList =
          objectMapper.readValue(new File("./src/main/resources/double.json"),
              DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = new Deck<>();
    for (DevelopmentCardJson doubleJson : doubleJsonList) {
      Gem gem = Gem.valueOf(doubleJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 2));
      ServerLevelCard bag = new ServerLevelCard(doubleJson.getId(), 0,
          "double" + doubleJson.getId(),
          doubleJson.getPrice(),
          Level.REDTWO, gemBonus);
      deck.addCard(bag);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  @SneakyThrows
  private void createNobleReserveDeck() {
    DevelopmentCardJson[] nobleReserveList;
    try {
      nobleReserveList = objectMapper.readValue(new File("/app/noble_reserve.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      nobleReserveList = objectMapper.readValue(new File("./src/main/resources/noble_reserve.json"),
          DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = redDecks.get(Level.REDTWO);
    for (DevelopmentCardJson nobleReserveJson : nobleReserveList) {
      Gem gem = Gem.valueOf(nobleReserveJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard nobleReserve = new ServerLevelCard(nobleReserveJson.getId(),
          nobleReserveJson.getPrestigePoint(), "noble_reserve" + nobleReserveJson.getId(),
          nobleReserveJson.getPrice(), Level.REDTWO, gemBonus);
      deck.addCard(nobleReserve);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(nobleReserve)),
          nobleReserve);
    }
    deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  @SneakyThrows
  private void createBagCascadeDeck() {
    DevelopmentCardJson[] bagCascadeList;
    try {
      bagCascadeList = objectMapper.readValue(new File("/app/bag_cascade.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      bagCascadeList = objectMapper.readValue(new File("./src/main/resources/bag_cascade.json"),
          DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = redDecks.get(Level.REDTWO);
    for (DevelopmentCardJson bagCascadeJson : bagCascadeList) {
      Gem gem = Gem.valueOf(bagCascadeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard bagCascade = new ServerLevelCard(bagCascadeJson.getId(),
          bagCascadeJson.getPrestigePoint(), "bag_cascade" + bagCascadeJson.getId(),
          bagCascadeJson.getPrice(), Level.REDTWO, gemBonus);
      deck.addCard(bagCascade);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bagCascade)),
          bagCascade);
    }
    //deck.shuffle();
    redDecks.put(Level.REDTWO, deck);
  }

  @SneakyThrows
  private void createSacrificeDeck() {
    DevelopmentCardJson[] sacrificeList;
    try {
      sacrificeList = objectMapper.readValue(new File("/app/sacrifice.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      sacrificeList = objectMapper.readValue(new File("./src/main/resources/sacrifice.json"),
          DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = new Deck<>();
    for (DevelopmentCardJson sacrificeJson : sacrificeList) {
      Gem gem = Gem.valueOf(sacrificeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard sacrifice = new ServerLevelCard(sacrificeJson.getId(),
          sacrificeJson.getPrestigePoint(), "sacrifice" + sacrificeJson.getId(),
          sacrificeJson.getPrice(), Level.REDTHREE, gemBonus);
      deck.addCard(sacrifice);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(sacrifice)),
          sacrifice);
    }
    deck.shuffle();
    redDecks.put(Level.REDTHREE, deck);
  }

  @SneakyThrows
  private void createCascadeTwoDeck() {
    DevelopmentCardJson[] cascadeTwoJsonList;
    try {
      cascadeTwoJsonList =
          objectMapper.readValue(new File("/app/cascade_two.json"), DevelopmentCardJson[].class);
    } catch (Exception e) {
      cascadeTwoJsonList =
          objectMapper.readValue(new File("./src/main/resources/cascade_two.json"),
              DevelopmentCardJson[].class);
    }
    Deck<ServerLevelCard> deck = redDecks.get(Level.REDTHREE);
    for (DevelopmentCardJson cascadeTwoJson : cascadeTwoJsonList) {
      Gem gem = Gem.valueOf(cascadeTwoJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard cascadeTwo = new ServerLevelCard(cascadeTwoJson.getId(),
          cascadeTwoJson.getPrestigePoint(), "cascade_two" + cascadeTwoJson.getId(),
          cascadeTwoJson.getPrice(), Level.REDTHREE, LevelCard.BonusType.CASCADING_TWO, gemBonus);
      deck.addCard(cascadeTwo);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(cascadeTwo)),
          cascadeTwo);
    }
    //deck.shuffle();
    redDecks.put(Level.REDTHREE, deck);
  }

  @SneakyThrows
  private void createOnBoardDecks() {
    Deck<ServerLevelCard> baseOneDeck = new Deck<>();
    Deck<ServerLevelCard> baseTwoDeck = new Deck<>();
    Deck<ServerLevelCard> baseThreeDeck = new Deck<>();

    // lay the cards face up on the game board
    for (int i = 0; i < 4; i++) {
      ServerLevelCard levelOne = levelDecks.get(Level.ONE).removeNextCard();
      levelOne.setFaceDown(false);
      baseOneDeck.addCard(levelOne);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)), levelOne);

      ServerLevelCard levelTwo = levelDecks.get(Level.TWO).removeNextCard();
      levelTwo.setFaceDown(false);
      baseTwoDeck.addCard(levelTwo);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)), levelTwo);

      ServerLevelCard levelThree = levelDecks.get(Level.THREE).removeNextCard();
      levelThree.setFaceDown(false);
      baseThreeDeck.addCard(levelThree);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }

    // make into data structures (hash map)
    this.onBoardDecks.put(Level.ONE, baseOneDeck);
    this.onBoardDecks.put(Level.TWO, baseTwoDeck);
    this.onBoardDecks.put(Level.THREE, baseThreeDeck);

    // same thing but with the nobles
    Deck<ServerNoble> nobleDeck = new Deck<>();
    for (int i = 0; i < 5; i++) {
      ServerNoble noble = this.nobleDeck.removeNextCard();
      nobleDeck.addCard(noble);
      remainingNobles.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)), noble);
    }
    this.onBoardNobles = nobleDeck;
  }

  @SneakyThrows
  private void createOnBoardRedDecks() {
    Deck<ServerLevelCard> redOneDeck = new Deck<>();
    Deck<ServerLevelCard> redTwoDeck = new Deck<>();
    Deck<ServerLevelCard> redThreeDeck = new Deck<>();
    for (int i = 0; i < 2; i++) {
      ServerLevelCard levelOne = redDecks.get(Level.REDONE).removeNextCard();
      levelOne.setFaceDown(false);
      redOneDeck.addCard(levelOne);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)), levelOne);

      ServerLevelCard levelTwo = redDecks.get(Level.REDTWO).removeNextCard();
      levelTwo.setFaceDown(false);
      redTwoDeck.addCard(levelTwo);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)), levelTwo);

      ServerLevelCard levelThree = redDecks.get(Level.REDTHREE).removeNextCard();
      levelThree.setFaceDown(false);
      redThreeDeck.addCard(levelThree);
      remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }
    this.onBoardDecks.put(Level.REDONE, redOneDeck);
    this.onBoardDecks.put(Level.REDTWO, redTwoDeck);
    this.onBoardDecks.put(Level.REDTHREE, redThreeDeck);
  }

  /**
   * Gets card by hash.
   *
   * @param hash hash of the card
   * @return the card
   */
  public ServerLevelCard getCardByHash(String hash) {
    return remainingCards.get(hash);
  }

  /**
   * Gets noble by hash.
   *
   * @param hash hash of the noble
   * @return the noble
   */
  public ServerNoble getNobleByHash(String hash) {
    return remainingNobles.get(hash);
  }

  /**
   * Gets deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<ServerLevelCard> getLevelDeck(Level level) {
    return switch (level) {
      case ONE, TWO, THREE -> levelDecks.get(level);
      case REDONE, REDTWO, REDTHREE -> redDecks.get(level);
    };
  }

  /**
   * Gets on board deck.
   *
   * @param level deck level
   * @return the deck
   */
  public Deck<ServerLevelCard> getOnBoardDeck(Level level) {
    return onBoardDecks.get(level);
  }


  /**
   * Adds a new card from deck to game board.
   *
   * @param level level of the deck
   */
  @SneakyThrows
  public void addOnBoardCard(Level level) {
    ServerLevelCard card = this.getLevelDeck(level).removeNextCard();
    card.setFaceDown(false);
    this.onBoardDecks.get(level).addCard(card);
    remainingCards.put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
  }

  /**
   * Removes a card from game board.
   *
   * @param card card to be removed
   * @return if the card was in the deck.
   */
  public boolean removeOnBoardCard(ServerLevelCard card) {
    return onBoardDecks.get(card.getLevel()).removeCard(card);
  }


  /**
   * Checks if is player's turn.
   *
   * @param player player we want to check.
   * @return true if is player's turn, false otherwise
   */
  public boolean isNotPlayersTurn(ServerPlayer player) {
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
  public void incGameBankFromPlayer(ServerPlayer player, int rubyAmount, int emeraldAmount,
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
  public void giveTwoOf(Gem gem, ServerPlayer player) {

    Map<Gem, Integer> gemIntegerMapGame = new HashMap<>();
    gemIntegerMapGame.put(gem, 2);
    gameBank.removeGemsFromBank(new PurchaseMap(gemIntegerMapGame));
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
                          ServerPlayer player) {
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

  private int findPlayerIndex(ServerPlayer player) {
    int i = 0;
    for (ServerPlayer e : getPlayers()) {
      if (e == player) {
        return i;
      }
      i++;
    }
    return -1;
  }


}
