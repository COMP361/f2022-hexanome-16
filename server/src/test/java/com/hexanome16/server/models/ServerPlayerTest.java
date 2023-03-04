package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.bank.PlayerBank;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Test of {@link ServerPlayer}.
 */
public class ServerPlayerTest {
  private ServerPlayer costa;

  /**
   * Test initializer for all the tests.
   */
  @BeforeEach
  public void init() {
    costa = new ServerPlayer("costa", "#000000");
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
                new ServerNoble(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1)),
                new ServerNoble(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1))
            )
        ));
    Queue<Action> actions = costa.getActionQueue();
    assertFalse(actions.isEmpty());
    Action action = actions.poll();
    var response = action.getActionDetails();
    var headers = response.getHeaders();
    assertEquals("choose-noble", headers.get("action-type").get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isBlank());
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
    var response = action.getActionDetails();
    var headers = response.getHeaders();
    assertEquals("choose-city", headers.get("action-type").get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isBlank());
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
    var response = action.getActionDetails();
    var headers = response.getHeaders();
    assertEquals("take-level-two", headers.get("action-type").get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(CustomHttpResponses.TAKE_LEVEL_TWO.getBody(), response.getBody());
  }

}
