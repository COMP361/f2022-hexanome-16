package com.hexanome16.server.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.CardInfo;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.bank.PlayerBank;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.cards.Visitable;
import com.hexanome16.server.models.inventory.Inventory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    costa = new ServerPlayer("costa", "#000000", 0);
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

    ResponseEntity<String> response = costa.peekTopAction().getActionDetails();
    var headers = response.getHeaders();
    assertEquals(CustomHttpResponses.ActionType.NOBLE.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
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
                new ServerCity(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1)),
                new ServerCity(123, 3, "idk",
                    new PurchaseMap(1, 1, 1, 1, 1, 1))
            )
        ));
    ResponseEntity<String> response = costa.peekTopAction().getActionDetails();
    var headers = response.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.CITY.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().isBlank());
  }

  /**
   * Testing addTakeTwoToPerform().
   */
  @Test
  public void testAddTakeTwoToPerform() {
    costa.addTakeTwoToPerform();
    ResponseEntity<String> actions = costa.peekTopAction().getActionDetails();
    var headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.LEVEL_TWO.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());
    assertEquals(CustomHttpResponses.TAKE_LEVEL_TWO.getBody(), actions.getBody());
  }

  /**
   * Testing addTakeOneToPerform().
   */
  @Test
  public void testAddTakeOneToPerform() {
    costa.addTakeOneToPerform();
    ResponseEntity<String> actions = costa.peekTopAction().getActionDetails();
    var headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.LEVEL_ONE.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());
    assertEquals(CustomHttpResponses.TAKE_LEVEL_ONE.getBody(), actions.getBody());
  }

  /**
   * Testing addAcquireCardToPerform().
   */
  @Test
  public void testAddAcquireCardToPerform() throws JsonProcessingException {
    ServerLevelCard card = Mockito.mock(ServerLevelCard.class);
    when(card.isBag()).thenReturn(true);
    costa.addAcquireCardToPerform(card);
    ResponseEntity<String> actions = costa.peekTopAction().getActionDetails();
    var headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.ASSOCIATE_BAG.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());
    assertEquals(CustomHttpResponses.ASSOCIATE_BAG_CARD.getBody(), actions.getBody());
  }

  /**
   * Testing addDiscardTokenAction().
   */
  @Test
  public void testAddDiscardTokenAction() {
    costa.addDiscardTokenToPerform();
    ResponseEntity<String> actions = costa.peekTopAction().getActionDetails();
    var headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.DISCARD.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());
  }

  /**
   * Testing addTakeTokenAction().
   */
  @Test
  public void testAddTakeTokenAction() {
    costa.addTakeTokenAction(Optional.empty());
    ResponseEntity<String> actions = costa.peekTopAction().getActionDetails();
    var headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.TAKE.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());

    costa.addTakeTokenAction(Optional.ofNullable(Gem.DIAMOND));
    actions = costa.peekTopAction().getActionDetails();
    headers = actions.getHeaders();
    assertEquals(
        CustomHttpResponses.ActionType.TAKE.getMessage(),
        Objects.requireNonNull(headers.get(CustomHttpResponses.ActionType.ACTION_TYPE)).get(0));
    assertEquals(HttpStatus.OK, actions.getStatusCode());
  }

  /**
   * Test can be visited by.
   */
  @Test
  public void testCanBeVisitedBy() {
    // Arrange
    Visitable visitable = Mockito.mock(Visitable.class);
    Inventory inventory = costa.getInventory();
    when(visitable.playerMeetsRequirements(inventory)).thenReturn(true);

    // Act
    var response = costa.canBeVisitedBy(visitable);

    // Assert
    assertTrue(response);
  }

  /**
   * Test can not be visited by.
   */
  @Test
  public void testCanBeVisitedByFail() {
    // Arrange
    Visitable visitable = Mockito.mock(Visitable.class);
    Inventory inventory = costa.getInventory();
    when(visitable.playerMeetsRequirements(inventory)).thenReturn(false);

    // Act
    var response = costa.canBeVisitedBy(visitable);

    // Assert
    assertFalse(response);
  }

  /**
   * Tests ownedGemBonuses().
   */
  @Test
  public void testOwnedGemBonuses() {
    Set<Gem> gems = costa.ownedGemBonuses();
    assertEquals(Set.of(), gems);
  }

  /**
   * Test hasAtLeastGoldenBonus().
   */
  @Test
  public void testHasAtLeastGoldenBonus() {
    assertTrue(costa.hasAtLeastGoldenBonus(0));
  }

  /**
   * Test topGoldCard().
   */
  @Test
  public void testTopGoldCard() {
    assertNull(costa.topGoldCard());
  }

  /**
   * Test removeCardFromInventory.
   */
  @Test
  public void testRemoveCardFromInventory() {
    var card = new ServerLevelCard();
    card.setCardInfo(new CardInfo(123, 12, "card",
        new PriceMap()));
    card.setGemBonus(new PurchaseMap());
    costa.getInventory().getOwnedCards().add(card);
    costa.removeCardFromInventory(card);
  }

}
