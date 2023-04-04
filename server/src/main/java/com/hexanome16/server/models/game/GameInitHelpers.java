package com.hexanome16.server.models.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.dto.cards.CardJson;
import com.hexanome16.common.dto.cards.DevelopmentCardJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.services.winconditions.WinCondition;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * This class provide helper methods to initialize the game.
 */
public class GameInitHelpers {
  private final String cardsJsonPath;

  private final Game game;
  private Deck<ServerNoble> nobleDeck;
  private Deck<ServerCity> citiesDeck;

  /**
   * Constructor.
   *
   * @param game the game to initialize
   */
  @SneakyThrows
  public GameInitHelpers(Game game) {
    this.game = game;
    cardsJsonPath = PropertiesLoaderUtils.loadProperties(
        new ClassPathResource("application.properties")).getProperty("path.cards");
    this.nobleDeck = new Deck<>();
  }

  private static final ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
  private static final int levelCardsTotal = 90;

  static Map<Level, Deck<ServerLevelCard>> createLevelMap() {
    HashMap<Level, Deck<ServerLevelCard>> levelMap = new HashMap<>();
    for (Level level : Level.values()) {
      levelMap.put(level, new Deck<>());
    }
    return levelMap;
  }

  static Map<Level, Deck<ServerLevelCard>> createBoardMap() {
    return createLevelMap();
  }

  void createDecks() {
    createBaseLevelDecks();
    createNobleDeck();
    if (game.getWinCondition() == WinCondition.CITIES) {
      createCities();
    }
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
      game.getRemainingCards().get(level).addCard(card);
      game.getRemainingCards().get(level).shuffle();
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(card)), card);
    }
    game.getRemainingCards().get(Level.ONE).reverse();
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
    this.nobleDeck = deck;
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
      PurchaseMap gemBonus = new PurchaseMap(Map.of());
      ServerLevelCard bag = new ServerLevelCard(bagJson.getId(), 0,
          "bag" + bagJson.getId(),
          bagJson.getPrice(),
          Level.REDONE, LevelCard.BonusType.BAG, gemBonus);
      deck.addCard(bag);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDONE, deck);
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
    Deck<ServerLevelCard> deck = game.getRemainingCards().get(Level.REDONE);
    for (DevelopmentCardJson goldJson : goldJsonList) {
      Gem gem = Gem.valueOf(goldJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 2));
      ServerLevelCard gold = new ServerLevelCard(goldJson.getId(), goldJson.getPrestigePoint(),
          "gold" + goldJson.getId(),
          goldJson.getPrice(),
          Level.REDONE, LevelCard.BonusType.TWO_GOLD_TOKENS, gemBonus);
      deck.addCard(gold);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(gold)), gold);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDONE, deck);
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
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bag)), bag);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDTWO, deck);
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
    Deck<ServerLevelCard> deck = game.getRemainingCards().get(Level.REDTWO);
    for (DevelopmentCardJson nobleReserveJson : nobleReserveList) {
      Gem gem = Gem.valueOf(nobleReserveJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard nobleReserve = new ServerLevelCard(nobleReserveJson.getId(),
          nobleReserveJson.getPrestigePoint(), "noble_reserve" + nobleReserveJson.getId(),
          nobleReserveJson.getPrice(), Level.REDTWO, LevelCard.BonusType.RESERVE_NOBLE,
          gemBonus);
      deck.addCard(nobleReserve);
      game.getHashToCardMap().put(
          DigestUtils.md5Hex(objectMapper.writeValueAsString(nobleReserve)),
          nobleReserve);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDTWO, deck);
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
    Deck<ServerLevelCard> deck = game.getRemainingCards().get(Level.REDTWO);
    for (DevelopmentCardJson bagCascadeJson : bagCascadeList) {
      Gem gem = Gem.valueOf(bagCascadeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of());
      ServerLevelCard bagCascade = new ServerLevelCard(bagCascadeJson.getId(),
          bagCascadeJson.getPrestigePoint(), "bag_cascade" + bagCascadeJson.getId(),
          bagCascadeJson.getPrice(), Level.REDTWO,
          LevelCard.BonusType.CASCADING_ONE_BAG, gemBonus);
      deck.addCard(bagCascade);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bagCascade)),
          bagCascade);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDTWO, deck);
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
    Deck<ServerLevelCard> deck = game.getRemainingCards().get(Level.REDTHREE);
    for (DevelopmentCardJson sacrificeJson : sacrificeList) {
      Gem gem = Gem.valueOf(sacrificeJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard sacrifice = new ServerLevelCard(sacrificeJson.getId(),
          sacrificeJson.getPrestigePoint(), "sacrifice" + sacrificeJson.getId(),
          sacrificeJson.getPrice(), Level.REDTHREE, LevelCard.BonusType.SACRIFICE, gemBonus);
      deck.addCard(sacrifice);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(sacrifice)),
          sacrifice);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDTHREE, deck);
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
    Deck<ServerLevelCard> deck = game.getRemainingCards().get(Level.REDTHREE);
    for (DevelopmentCardJson cascadeTwoJson : cascadeTwoJsonList) {
      Gem gem = Gem.valueOf(cascadeTwoJson.getBonus());
      PurchaseMap gemBonus = new PurchaseMap(Map.of(gem, 1));
      ServerLevelCard cascadeTwo = new ServerLevelCard(cascadeTwoJson.getId(),
          cascadeTwoJson.getPrestigePoint(), "cascade_two" + cascadeTwoJson.getId(),
          cascadeTwoJson.getPrice(), Level.REDTHREE, LevelCard.BonusType.CASCADING_TWO, gemBonus);
      deck.addCard(cascadeTwo);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(cascadeTwo)),
          cascadeTwo);
    }
    deck.shuffle();
    game.getRemainingCards().put(Level.REDTHREE, deck);
  }

  @SneakyThrows
  void createOnBoardDecks() {
    Deck<ServerLevelCard> baseOneDeck = new Deck<>();
    Deck<ServerLevelCard> baseTwoDeck = new Deck<>();
    Deck<ServerLevelCard> baseThreeDeck = new Deck<>();

    // lay the cards face up on the game board
    for (int i = 0; i < 4; i++) {
      ServerLevelCard levelOne = game.getRemainingCards().get(Level.ONE).removeNextCard();
      levelOne.setFaceDown(false);
      baseOneDeck.addCard(levelOne);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)),
          levelOne);

      ServerLevelCard levelTwo = game.getRemainingCards().get(Level.TWO).removeNextCard();
      levelTwo.setFaceDown(false);
      baseTwoDeck.addCard(levelTwo);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)),
          levelTwo);

      ServerLevelCard levelThree = game.getRemainingCards().get(Level.THREE).removeNextCard();
      levelThree.setFaceDown(false);
      baseThreeDeck.addCard(levelThree);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }

    // make into data structures (hash map)
    game.getOnBoardDecks().put(Level.ONE, baseOneDeck);
    game.getOnBoardDecks().put(Level.TWO, baseTwoDeck);
    game.getOnBoardDecks().put(Level.THREE, baseThreeDeck);

    // same thing but with the nobles
    Deck<ServerNoble> nobleDeck = new Deck<>();
    for (int i = 0; i < 5; i++) {
      ServerNoble noble = this.nobleDeck.removeNextCard();
      nobleDeck.addCard(noble);
    }
    game.onBoardNobles = nobleDeck;

    if (game.getWinCondition() == WinCondition.CITIES) {
      Deck<ServerCity> cities = new Deck<>();
      for (int i = 0; i < 3; i++) {
        ServerCity city = this.citiesDeck.removeNextCard();
        cities.addCard(city);
      }
      game.onBoardCities = cities;
    }
  }

  @SneakyThrows
  void createOnBoardRedDecks() {
    Deck<ServerLevelCard> redOneDeck = new Deck<>();
    Deck<ServerLevelCard> redTwoDeck = new Deck<>();
    Deck<ServerLevelCard> redThreeDeck = new Deck<>();
    for (int i = 0; i < 2; i++) {
      ServerLevelCard levelOne = game.getRemainingCards().get(Level.REDONE).removeNextCard();
      levelOne.setFaceDown(false);
      redOneDeck.addCard(levelOne);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelOne)),
          levelOne);

      ServerLevelCard levelTwo = game.getRemainingCards().get(Level.REDTWO).removeNextCard();
      levelTwo.setFaceDown(false);
      redTwoDeck.addCard(levelTwo);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelTwo)),
          levelTwo);

      ServerLevelCard levelThree = game.getRemainingCards().get(Level.REDTHREE).removeNextCard();
      levelThree.setFaceDown(false);
      redThreeDeck.addCard(levelThree);
      game.getHashToCardMap().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(levelThree)),
          levelThree);
    }
    game.getOnBoardDecks().put(Level.REDONE, redOneDeck);
    game.getOnBoardDecks().put(Level.REDTWO, redTwoDeck);
    game.getOnBoardDecks().put(Level.REDTHREE, redThreeDeck);
  }

  @SneakyThrows
  void createCities() {
    CardJson[] cityJsonList;
    try {
      cityJsonList = objectMapper.readValue(new File(cardsJsonPath + "/cities.json"),
          CardJson[].class);
    } catch (Exception e) {
      throw new RuntimeException("Could not load cities.json", e);
    }
    Deck<ServerCity> deck = new Deck<>();
    for (CardJson cityJson : cityJsonList) {
      ServerCity city = new ServerCity(cityJson.getId(), cityJson.getPrestigePoint(),
          "city" + cityJson.getId(), cityJson.getPrice());
      deck.addCard(city);
      game.getRemainingCities().put(DigestUtils.md5Hex(objectMapper.writeValueAsString(city)),
          city);
    }
    deck.shuffle();
    citiesDeck = deck;
  }
}
