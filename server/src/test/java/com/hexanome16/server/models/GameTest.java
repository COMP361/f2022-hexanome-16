package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.ReflectionUtils;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.bank.PlayerBank;
import com.hexanome16.server.models.winconditions.WinCondition;
import com.hexanome16.server.util.broadcastmap.BroadcastMap;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Game}.
 */
public class GameTest {

  ServerPlayer imad = new ServerPlayer("imad", "white", 0);
  ServerPlayer tristan = new ServerPlayer("tristan", "blue", 1);
  private Game game;

  /**
   * Initiates every test.
   *
   * @throws IOException from game creation
   */
  @BeforeEach
  public void init() throws IOException {
    game = Game.create(12345,
        new ServerPlayer[] {imad, tristan}, "imad", "", new WinCondition[] {WinCondition.BASE},
        false, false);
  }

  /**
   * Test get deck.
   */
  @Test
  public void testGetDeck() {
    assertNotNull(game.getLevelDeck(Level.ONE));
  }

  /**
   * Test get on board deck.
   */
  @Test
  public void testGetOnBoardDeck() {
    assertNotNull(game.getOnBoardDeck(Level.ONE));
  }

  /**
   * Test get current player.
   */
  @Test
  public void testCurrentPlayerInitializesAtZero() {
    assertEquals(0, game.getCurrentPlayerIndex());
  }

  /**
   * Test creation of BroadcastContentManagerMap.
   */
  @Test
  public void testCreateBroadcastContentManagerMap() {
    Field field;
    try {
      field = Game.class.getDeclaredField("broadcastContentManagerMap");

      field.setAccessible(true);

      var map = ((BroadcastMap) field.get(game));
      assertNotNull(map);
      assertNotNull(map.getManager(BroadcastMapKey.PLAYERS));
      assertNotNull(map.getManager(BroadcastMapKey.WINNERS));
      assertNotNull(map.getManager(BroadcastMapKey.NOBLES));
      for (Level level : Level.values()) {
        assertNotNull(map.getManager(BroadcastMapKey.fromLevel(level)));
      }
    } catch (NoSuchFieldException e) {
      fail("gameMap not in GameManagerService");
    } catch (IllegalAccessException e) {
      fail("set accessible did not work");
    }
  }

  /**
   * Test BroadcastContentManagerMap getter.
   */
  @Test
  public void testGetBroadcastContentManagerMap() {
    assertNotNull(game.getBroadcastContentManagerMap());
    Field field = ReflectionUtils.getFieldAndSetPublic(game, "broadcastContentManagerMap");
    if (field == null) {
      fail("Field is missing");
    }
    try {
      var map = ((BroadcastMap) field.get(game));
      assertNotNull(map);
      assertNotNull(map.getManager(BroadcastMapKey.PLAYERS));
      assertNotNull(map.getManager(BroadcastMapKey.WINNERS));
      assertNotNull(map.getManager(BroadcastMapKey.NOBLES));
      for (Level level : Level.values()) {
        assertNotNull(map.getManager(BroadcastMapKey.fromLevel(level)));
      }
    } catch (IllegalAccessException e) {
      fail("set accessible did not work");
    }
  }

  /**
   * Test player array gets cloned.
   *
   * @throws IOException object mapper exception
   */
  @Test
  public void testPlayerArrayGetsCloned() throws IOException {
    ServerPlayer[] players = new ServerPlayer[] {imad, tristan};
    game = Game.create(12345,
        players, "imad", "", new WinCondition[] {WinCondition.BASE}, false, false);
    var gamePlayers = game.getPlayers();
    assertNotEquals(players, gamePlayers);
    players[0] = null;
    assertNotNull(gamePlayers[0]);
  }

  /**
   * Test go to next player.
   */
  @Test
  public void testGoToNextPlayer() {
    assertEquals(0, game.getCurrentPlayerIndex());
    assertEquals(imad, game.getCurrentPlayer());
    assertNotEquals(tristan, game.getCurrentPlayer());
    game.goToNextPlayer();
    assertEquals(1, game.getCurrentPlayerIndex());
    assertEquals(tristan, game.getCurrentPlayer());
    assertNotEquals(imad, game.getCurrentPlayer());
    game.goToNextPlayer();
    assertEquals(0, game.getCurrentPlayerIndex());
    assertEquals(imad, game.getCurrentPlayer());
    assertNotEquals(tristan, game.getCurrentPlayer());
  }

  /**
   * Test add on board card.
   */
  @Test
  public void testAddOnBoardCard() {
    List<ServerNoble> cardList = game.getNobleDeck().getCardList();
    game.addOnBoardCard(Level.ONE);
    assertNotEquals(cardList.size() + 1, game.getOnBoardDeck(Level.ONE).getCardList().size());
  }

  /**
   * Test remove on board card.
   */
  @Test
  public void testRemoveOnBoardCard() {
    List<ServerLevelCard> cardList = game.getLevelDeck(Level.ONE).getCardList();
    ServerLevelCard card = cardList.get(0);
    game.removeOnBoardCard(card);
    assertFalse(game.getOnBoardDeck(Level.ONE).getCardList().contains(card));
  }

  /**
   * Testing incGameBank with Purchase Map.
   */
  @Test
  public void testIncGameBank() {
    assertEquals(game.getGameBank(), new GameBank());
    HashMap<Gem, Integer> myMap = new HashMap<>();
    myMap.put(Gem.RUBY, 2);
    game.decGameBank(new PurchaseMap(myMap));
    GameBank myGameBank = new GameBank();
    myGameBank.removeGemsFromBank(Gem.RUBY, 2);
    assertEquals(game.getGameBank(), myGameBank);
  }


  /**
   * Testing availableTwoTokenType(). The shenanigans in the equal is because it needs
   * to be a set in order for the order not to matter.
   */
  @Test
  public void testAvailableTwoTokensType() {
    ArrayList<Gem> availableGems =
        new ArrayList<>(List.of(Gem.RUBY, Gem.SAPPHIRE, Gem.ONYX, Gem.DIAMOND, Gem.EMERALD));


    assertEquals(Set.copyOf(availableGems), Set.copyOf(game.availableTwoTokensType()));
  }

  /**
   * Testing allowedTakeTwoOf.
   */
  @Test
  public void testAllowedTakeTwoOf() {
    assertTrue(game.allowedTakeTwoOf(Gem.RUBY));
    game.incGameBank(-7, 0,
        0, 0, 0, 0);
    assertFalse(game.allowedTakeTwoOf(Gem.RUBY));
  }

  /**
   * Testing giveTwoOf(Gem g, Player p).
   */
  @Test
  public void testGiveTwoOf() {
    PlayerBank myBank = new PlayerBank();
    assertEquals(imad.getBank(), myBank);
    myBank.addGemsToBank(Gem.RUBY, 2);
    game.giveTwoOf(Gem.RUBY, imad);
    GameBank gameBank = new GameBank();
    gameBank.removeGemsFromBank(Gem.RUBY, 2);
    assertEquals(imad.getBank(), myBank);
    assertEquals(game.getGameBank(), gameBank);
  }


  /**
   * Testing availableThreeTokenType(). The shenanigans in the equal is once again because it needs
   * to be a set in order for the order not to matter.
   */
  @Test
  public void testAvailableThreeTokensType() {
    ArrayList<Gem> availableGems =
        new ArrayList<>(List.of(Gem.RUBY, Gem.SAPPHIRE, Gem.ONYX, Gem.DIAMOND, Gem.EMERALD));


    assertEquals(Set.copyOf(availableGems), Set.copyOf(game.availableThreeTokensType()));
  }

  /**
   * Testing allowedTakeThreeOf(Gem g, Gem g2, Gem g3).
   */
  @Test
  public void testAllowedTakeThreeOf() {
    assertTrue(game.allowedTakeThreeOf(Gem.RUBY, Gem.SAPPHIRE, Gem.DIAMOND));
    assertFalse(game.allowedTakeThreeOf(Gem.RUBY, Gem.RUBY, Gem.DIAMOND));
    game.incGameBank(-7, 0, 0, 0, 0, 0);
    assertFalse(game.allowedTakeThreeOf(Gem.RUBY, Gem.EMERALD, Gem.DIAMOND));
  }

  /**
   * Testing giveThreeOf(Gem g1, Gem g2, Gem g3, Player p).
   */
  @Test
  public void testGiveThreeOf() {
    PlayerBank myBank = new PlayerBank();
    assertEquals(imad.getBank(), myBank);
    myBank.addGemsToBank(new PurchaseMap(1, 1,
        0, 1, 0, 0));
    game.giveThreeOf(Gem.RUBY, Gem.DIAMOND, Gem.EMERALD, imad);
    GameBank gameBank = new GameBank();
    gameBank.removeGemsFromBank(new PurchaseMap(1, 1, 0, 1, 0, 0));
    assertEquals(imad.getBank(), myBank);
    assertEquals(game.getGameBank(), gameBank);
  }


}
