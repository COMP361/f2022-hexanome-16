package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;



/**
 * Tests for {@link Game}.
 */
public class GameTest {

  Player imad = new Player("imad", "#FFFFFF");
  Player tristan =  new Player("tristan", "#FFFFFF");
  private final Game game = new Game(12345,
          new Player[]{imad, tristan}, "imad", "");

  /**
   * Instantiates a new Game test.
   *
   * @throws IOException the io exception
   */
  public GameTest() throws IOException {
  }

  /**
   * Test get deck.
   */
  @Test
  public void testGetDeck() {
    assertNotNull(game.getDeck(Level.ONE));
  }

  /**
   * Test get on board deck.
   */
  @Test
  public void testGetOnBoardDeck() {
    assertNotNull(game.getOnBoardDeck(Level.ONE));
  }

  /**
   * Test add on board card.
   */
  @Test
  public void testAddOnBoardCard() {
    List cardList = game.getNobleDeck().getCardList();
    game.addOnBoardCard(Level.ONE);
    assertNotEquals(cardList.size() + 1, game.getOnBoardDeck(Level.ONE).getCardList().size());
  }

  /**
   * Test remove on board card.
   */
  @Test
  public void testRemoveOnBoardCard() {
    List<DevelopmentCard> cardList = game.getDeck(Level.ONE).getCardList();
    DevelopmentCard card = cardList.get(0);
    game.removeOnBoardCard((LevelCard) card);
    assertFalse(game.getOnBoardDeck(Level.ONE).getCardList().contains(card));
  }

  /**
   * Testing incGameBank with Purchase Map.
   */
  @Test
  public void testIncGameBank() {
    assertEquals( game.getGameBank(), new GameBank());
    HashMap<Gem, Integer> myMap = new HashMap<Gem, Integer>();
    myMap.put(Gem.RUBY, -2);
    game.incGameBank(new PurchaseMap(myMap));
    GameBank myGameBank = new GameBank();
    myGameBank.incBank(-2,
            0, 0,
            0, 0, 0);
    assertEquals(game.getGameBank(), myGameBank);
  }


  /**
   * Testing availableTwoTokenType(). The shenanigans in the equal is because it needs
   * to be a set in order for the order not to matter. (Note that Gold tokens are part
   * of the returned list, IDK if that should be the case at all but for now it is what
   * it is :) )
   */
  @Test
  public void testAvailableTwoTokensType() {
    ArrayList<Gem> availableGems = game.availableTwoTokensType();
    assertEquals(Set.copyOf(availableGems), Set.copyOf(new ArrayList<>(List.of(Gem.RUBY, Gem.SAPPHIRE,
            Gem.DIAMOND, Gem.EMERALD, Gem.ONYX, Gem.GOLD))));
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

    assertEquals(imad.getBank(), new PlayerBank());
  }


}
