package com.hexanome16.server.models.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.cards.CardJson;
import com.hexanome16.common.dto.cards.DevelopmentCardJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * This class provide helper methods to initialize the game.
 */
public class GameInitHelpers {
  private final String cardsJsonPath;

  private final Game game;

  /**
   * Constructor.
   *
   * @param game the game to initialize
   */
  @SneakyThrows
  public GameInitHelpers(Game game) {
    this.game = game;
    Resource resource = new ClassPathResource("application.properties");
    Properties props = PropertiesLoaderUtils.loadProperties(resource);
    cardsJsonPath = props.getProperty("path.cards");
  }

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final int levelCardsTotal = 90;

  static Map<Level, Deck<ServerLevelCard>> createLevelMap() {
    HashMap<Level, Deck<ServerLevelCard>> levelMap = new HashMap<>();
    levelMap.put(Level.ONE, new Deck<>());
    levelMap.put(Level.TWO, new Deck<>());
    levelMap.put(Level.THREE, new Deck<>());
    return levelMap;
  }

  static Map<Level, Deck<ServerLevelCard>> createRedMap() {
    HashMap<Level, Deck<ServerLevelCard>> levelMap = new HashMap<>();
    levelMap.put(Level.REDONE, new Deck<>());
    levelMap.put(Level.REDTWO, new Deck<>());
    levelMap.put(Level.REDTHREE, new Deck<>());
    return levelMap;
  }

  static Map<Level, Deck<ServerLevelCard>> createBoardMap() {
    Map<Level, Deck<ServerLevelCard>> levelMap = createLevelMap();
    levelMap.putAll(createRedMap());
    return levelMap;
  }

  void createDecks() {
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
  void createBaseLevelDecks() {
    DevelopmentCardJson[] cardJsonList;
    try {
      cardJsonList = objectMapper.readValue(new File(cardsJsonPath + "/cards.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load cards.json", e);
    }
    for (int i = 0; i < levelCardsTotal; i++) {
      DevelopmentCardJson cardJson = cardJsonList[i];
      String textureLevel = i < 40 ? "level_one" : i < 70 ? "level_two" : "level_three";
      Level level = i < 40 ? Level.ONE : i < 70 ? Level.TWO : Level.THREE;
      Gem gem = Gem.valueOf(cardJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard card = new ServerLevelCard(cardJson.getId(), cardJson.getPrestigePoint(),
          textureLevel + cardJson.getId(), cardJson.getPrice(), level, gemBonus);
      game.getLevelDecks().get(level).addCard(card);
      if (level != Level.ONE) {
        game.getLevelDecks().get(level).shuffle();
      }
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
    game.getLevelDecks().get(Level.ONE).reverse();
  }

  @SneakyThrows
  void createNobleDeck() {
    CardJson[] nobleJsonList;
    try {
      nobleJsonList = objectMapper.readValue(new File(cardsJsonPath + "/nobles.json"),
          CardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load nobles.json", e);
    }
    Deck<ServerNoble> deck = new Deck<>();
    for (CardJson nobleJson : nobleJsonList) {
      ServerNoble noble = new ServerNoble(nobleJson.getId(), nobleJson.getPrestigePoint(),
          "noble" + nobleJson.getId(), nobleJson.getPrice());
      deck.addCard(noble);
      game.getRemainingNobles().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)),
          noble);
    }
    deck.shuffle();
    game.nobleDeck = deck;
  }

  @SneakyThrows
  void createBagDeck() {
    DevelopmentCardJson[] bagJsonList;
    try {
      bagJsonList = objectMapper.readValue(new File(cardsJsonPath + "/bag.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load bag.json", e);
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
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    game.getRedDecks().put(Level.REDONE, deck);
  }

  @SneakyThrows
  void createGoldDeck() {
    DevelopmentCardJson[] goldJsonList;
    try {
      goldJsonList = objectMapper.readValue(new File(cardsJsonPath + "/gold.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load gold.json", e);
    }
    Deck<ServerLevelCard> deck = game.getRedDecks().get(Level.REDONE);
    for (DevelopmentCardJson goldJson : goldJsonList) {
      Gem gem = Gem.valueOf(goldJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 2));
      ServerLevelCard gold = new ServerLevelCard(goldJson.getId(), goldJson.getPrestigePoint(),
          "gold" + goldJson.getId(),
          goldJson.getPrice(),
          Level.REDONE, gemBonus);
      deck.addCard(gold);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(gold)), gold);
    }
    deck.shuffle();
    game.getRedDecks().put(Level.REDONE, deck);
  }

  @SneakyThrows
  void createDoubleDeck() {
    DevelopmentCardJson[] doubleJsonList;
    try {
      doubleJsonList = objectMapper.readValue(new File(cardsJsonPath + "/double.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load double.json", e);
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
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    game.getRedDecks().put(Level.REDTWO, deck);
  }

  @SneakyThrows
  void createNobleReserveDeck() {
    DevelopmentCardJson[] nobleReserveList;
    try {
      nobleReserveList = objectMapper.readValue(new File(cardsJsonPath + "/noble_reserve.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load noble_reserve.json", e);
    }
    Deck<ServerLevelCard> deck = game.getRedDecks().get(Level.REDTWO);
    for (DevelopmentCardJson nobleReserveJson : nobleReserveList) {
      Gem gem = Gem.valueOf(nobleReserveJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard nobleReserve = new ServerLevelCard(nobleReserveJson.getId(),
          nobleReserveJson.getPrestigePoint(), "noble_reserve" + nobleReserveJson.getId(),
          nobleReserveJson.getPrice(), Level.REDTWO, gemBonus);
      deck.addCard(nobleReserve);
      game.getRemainingCards().put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(nobleReserve)),
          nobleReserve);
    }
    deck.shuffle();
    game.getRedDecks().put(Level.REDTWO, deck);
  }

  @SneakyThrows
  void createBagCascadeDeck() {
    DevelopmentCardJson[] bagCascadeList;
    try {
      bagCascadeList = objectMapper.readValue(new File(cardsJsonPath + "/bag_cascade.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load bag_cascade.json", e);
    }
    Deck<ServerLevelCard> deck = game.getRedDecks().get(Level.REDTWO);
    for (DevelopmentCardJson bagCascadeJson : bagCascadeList) {
      Gem gem = Gem.valueOf(bagCascadeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard bagCascade = new ServerLevelCard(bagCascadeJson.getId(),
          bagCascadeJson.getPrestigePoint(), "bag_cascade" + bagCascadeJson.getId(),
          bagCascadeJson.getPrice(), Level.REDTWO, gemBonus);
      deck.addCard(bagCascade);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bagCascade)),
          bagCascade);
    }
    //deck.shuffle();
    game.getRedDecks().put(Level.REDTWO, deck);
  }

  @SneakyThrows
  void createSacrificeDeck() {
    DevelopmentCardJson[] sacrificeList;
    try {
      sacrificeList = objectMapper.readValue(new File(cardsJsonPath + "/sacrifice.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load sacrifice.json", e);
    }
    Deck<ServerLevelCard> deck = new Deck<>();
    for (DevelopmentCardJson sacrificeJson : sacrificeList) {
      Gem gem = Gem.valueOf(sacrificeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard sacrifice = new ServerLevelCard(sacrificeJson.getId(),
          sacrificeJson.getPrestigePoint(), "sacrifice" + sacrificeJson.getId(),
          sacrificeJson.getPrice(), Level.REDTHREE, gemBonus);
      deck.addCard(sacrifice);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(sacrifice)),
          sacrifice);
    }
    deck.shuffle();
    game.getRedDecks().put(Level.REDTHREE, deck);
  }

  @SneakyThrows
  void createCascadeTwoDeck() {
    DevelopmentCardJson[] cascadeTwoJsonList;
    try {
      cascadeTwoJsonList = objectMapper.readValue(new File(cardsJsonPath + "/cascade_two.json"),
          DevelopmentCardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load cascade_two.json", e);
    }
    Deck<ServerLevelCard> deck = game.getRedDecks().get(Level.REDTHREE);
    for (DevelopmentCardJson cascadeTwoJson : cascadeTwoJsonList) {
      Gem gem = Gem.valueOf(cascadeTwoJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard cascadeTwo = new ServerLevelCard(cascadeTwoJson.getId(),
          cascadeTwoJson.getPrestigePoint(), "cascade_two" + cascadeTwoJson.getId(),
          cascadeTwoJson.getPrice(), Level.REDTHREE, LevelCard.BonusType.CASCADING_TWO, gemBonus);
      deck.addCard(cascadeTwo);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(cascadeTwo)),
          cascadeTwo);
    }
    //deck.shuffle();
    game.getRedDecks().put(Level.REDTHREE, deck);
  }

  @SneakyThrows
  void createOnBoardDecks() {
    Deck<ServerLevelCard> baseOneDeck = new Deck<>();
    Deck<ServerLevelCard> baseTwoDeck = new Deck<>();
    Deck<ServerLevelCard> baseThreeDeck = new Deck<>();

    // lay the cards face up on the game board
    for (int i = 0; i < 4; i++) {
      ServerLevelCard levelOne = game.getLevelDecks().get(Level.ONE).removeNextCard();
      levelOne.setFaceDown(false);
      baseOneDeck.addCard(levelOne);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)),
          levelOne);

      ServerLevelCard levelTwo = game.getLevelDecks().get(Level.TWO).removeNextCard();
      levelTwo.setFaceDown(false);
      baseTwoDeck.addCard(levelTwo);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)),
          levelTwo);

      ServerLevelCard levelThree = game.getLevelDecks().get(Level.THREE).removeNextCard();
      levelThree.setFaceDown(false);
      baseThreeDeck.addCard(levelThree);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }

    // make into data structures (hash map)
    game.getOnBoardDecks().put(Level.ONE, baseOneDeck);
    game.getOnBoardDecks().put(Level.TWO, baseTwoDeck);
    game.getOnBoardDecks().put(Level.THREE, baseThreeDeck);

    // same thing but with the nobles
    Deck<ServerNoble> nobleDeck = new Deck<>();
    for (int i = 0; i < 5; i++) {
      ServerNoble noble = game.getNobleDeck().removeNextCard();
      nobleDeck.addCard(noble);
      game.getRemainingNobles().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(noble)),
          noble);
    }
    game.onBoardNobles = nobleDeck;
  }

  @SneakyThrows
  void createOnBoardRedDecks() {
    Deck<ServerLevelCard> redOneDeck = new Deck<>();
    Deck<ServerLevelCard> redTwoDeck = new Deck<>();
    Deck<ServerLevelCard> redThreeDeck = new Deck<>();
    for (int i = 0; i < 2; i++) {
      ServerLevelCard levelOne = game.getRedDecks().get(Level.REDONE).removeNextCard();
      levelOne.setFaceDown(false);
      redOneDeck.addCard(levelOne);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)),
          levelOne);

      ServerLevelCard levelTwo = game.getRedDecks().get(Level.REDTWO).removeNextCard();
      levelTwo.setFaceDown(false);
      redTwoDeck.addCard(levelTwo);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)),
          levelTwo);

      ServerLevelCard levelThree = game.getRedDecks().get(Level.REDTHREE).removeNextCard();
      levelThree.setFaceDown(false);
      redThreeDeck.addCard(levelThree);
      game.getRemainingCards().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }
    game.getOnBoardDecks().put(Level.REDONE, redOneDeck);
    game.getOnBoardDecks().put(Level.REDTWO, redTwoDeck);
    game.getOnBoardDecks().put(Level.REDTHREE, redThreeDeck);
  }
}
