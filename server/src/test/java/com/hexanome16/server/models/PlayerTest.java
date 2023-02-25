package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.bank.PlayerBank;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import models.price.PurchaseMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link Player}.
 */
public class PlayerTest {
  private Player costa;

  /**
   * Test initializer for all the tests.
   */
  @BeforeEach
  public void init() {
    costa = new Player("costa", "#000000");
  }

  /**
   * Testing incPlayerBank(int, int, int, int, int, int).
   */
  @Test
  public void testIncPlayerBank() {
    PlayerBank playerBank = new PlayerBank();
    assertEquals(costa.getBank(), playerBank);
    costa.incPlayerBank(-3, -2,
        0, 0, 0, 0);
    playerBank.removeGemsFromBank(new PurchaseMap(3, 2,
        0, 0, 0, 0));
    assertEquals(costa.getBank(), playerBank);
  }

  /**
   * Testing incPlayerBank(PurchaseMap purchaseMap).
   */
  @Test
  public void testIncPlayerBankAlt() {
    PlayerBank playerBank = new PlayerBank();
    assertEquals(costa.getBank(), playerBank);
    costa.incPlayerBank(new PurchaseMap(-3, -2,
        0, 0, 0, 0));
    playerBank.removeGemsFromBank(new PurchaseMap(3, 2,
        0, 0, 0, 0));
    assertEquals(costa.getBank(), playerBank);
  }

  /**
   * Testing hasAtLeast().
   */
  @Test
  public void testHasAtLeast() {
    assertTrue(costa.hasAtLeast(0, 0, 0, 0, 0, 0));
    costa.incPlayerBank(2, 2, 0, 0, 0, 0);
    assertTrue(costa.hasAtLeast(2, 2, 0, 0, 0, 0));
  }

  /**
   * Testing addNobleListToPerform(ArrayList).
   */
  @Test
  public void testAddNobleListToPerform() throws JsonProcessingException {
    costa.addNobleListToPerform(
        new ArrayList<>(
            List.of(
                new Noble(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1)),
                new Noble(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1))
                )
        ));
    Queue<Action> actions = costa.getActionQueue();
    assertFalse(actions.isEmpty());
    Action action = actions.poll();
    assertEquals("choose-noble",
        action.getActionDetails().getHeaders().get("action-type").get(0));
  }

  /**
   * Testing addCitiesToPerform(ArrayList).
   */
  @Test
  public void testAddCitiesToPerform() throws JsonProcessingException {
    costa.addCitiesToPerform(
        new ArrayList<>(
            List.of(
                new City(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1)),
                new City(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1))
            )
        ));
    Queue<Action> actions = costa.getActionQueue();
    assertFalse(actions.isEmpty());
    Action action = actions.poll();
    assertEquals("choose-city",
        action.getActionDetails().getHeaders().get("action-type").get(0));
  }

  /**
   * Testing addTakeTwoToPerform().
   */
  @Test
  public void testAddTakeTwoToPerform() throws JsonProcessingException {
    costa.addTakeTwoToPerform();
    Queue<Action> actions = costa.getActionQueue();
    assertFalse(actions.isEmpty());
    Action action = actions.poll();
    assertEquals("take-level-two",
        action.getActionDetails().getHeaders().get("action-type").get(0));
  }

}
