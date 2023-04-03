package com.hexanome16.server.models.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.CardInfo;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.cards.ServerLevelCard;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * All the tests related to the stand-alone action classes.
 */
public class ActionsTest {

  private final ObjectMapper om = new ObjectMapper();


  /**
   * Tests associate card action is deserialized properly.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks AssociateCard is deserialized properly")
  public void testAssociateCardAction() {
    AssociateCardAction action = new AssociateCardAction();
    String serialized = om.writeValueAsString(action);
    AssociateCardAction deserializedAction = om.readValue(serialized, AssociateCardAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests associate card action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks AssociateCard is deserialized properly 2")
  public void testAssociateCardAction2() {
    AssociateCardAction action = new AssociateCardAction(
        new ServerLevelCard(123, 12, "card",
            new PriceMap(0, 0, 0, 0, 0),
            Level.ONE, LevelCard.BonusType.BAG,
            new PurchaseMap(1, 1, 1,
                1, 1, 1))
    );
    String serialized = om.writeValueAsString(action);
    AssociateCardAction deserializedAction = om.readValue(serialized, AssociateCardAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests ChooseCity action is deserialized properly.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ChooseCity is deserialized properly")
  public void testChooseCityAction() {
    ChooseCityAction action = new ChooseCityAction(new City[]{});
    String serialized = om.writeValueAsString(action);
    ChooseCityAction deserializedAction = om.readValue(serialized, ChooseCityAction.class);
    assertEquals(action, deserializedAction);

  }


  /**
   * Tests ChooseCity action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ChooseCity is deserialized properly 2")
  public void testChooseCityAction2() {
    ChooseCityAction action = new ChooseCityAction(new City[]{
        new City(new CardInfo(123, 12, "city1", new PriceMap())),
        new City(new CardInfo(1234, 213, "city2",
            new PriceMap(0, 0, 0, 0, 0)))
    });
    String serialized = om.writeValueAsString(action);
    ChooseCityAction deserializedAction = om.readValue(serialized, ChooseCityAction.class);
    assertEquals(action, deserializedAction);

  }

  /**
   * Tests ChooseNoble action is deserialized properly.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ChooseNoble is deserialized properly")
  public void testChooseNobleAction() {
    Action action = new ChooseNobleAction(new Noble[]{});
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, ChooseNobleAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests ChooseNoble action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ChooseNoble is deserialized properly 2")
  public void testChooseNobleAction2() {
    Action action = new ChooseNobleAction(new Noble[]{
        new Noble(new CardInfo(123, 12, "noble2", new PriceMap())),
        new Noble(new CardInfo(1234, 213, "noble2",
            new PriceMap(0, 0, 0, 0, 0)))
    });
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, ChooseNobleAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests DiscardToken action is deserialized properly.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks DiscardToken is deserialized properly")
  public void testDiscardTokenAction() {
    Action action = new DiscardTokenAction(new Gem[]{});
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, DiscardTokenAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests DiscardToken action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks DiscardToken is deserialized properly 2")
  public void testDiscardTokenAction2() {
    Action action = new DiscardTokenAction(new Gem[]{Gem.GOLD, Gem.EMERALD});
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, DiscardTokenAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests ReserveNoble action is deserialized properly.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ReserveNoble is deserialized properly")
  public void testReserveNobleAction() {
    Action action = new ReserveNobleAction(new Noble[]{});
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, ReserveNobleAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests ReserveNoble action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks ReserveNoble is deserialized properly 2")
  public void testReserveNobleAction2() {
    Action action = new ReserveNobleAction(new Noble[]{
        new Noble(new CardInfo(123, 12, "noble2", new PriceMap())),
        new Noble(new CardInfo(1234, 213, "noble2",
            new PriceMap(0, 0, 0, 0, 0)))});
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, ReserveNobleAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests TakeOne action is deserialized properly .
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks TakeOne is deserialized properly")
  public void testTakeOneAction() {
    Action action = new TakeOneAction();
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, TakeOneAction.class);
    assertEquals(action, deserializedAction);
  }

  /**
   * Tests TakeTwo action is deserialized properly .
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks TakeTwo is deserialized properly")
  public void testTakeTwoAction() {
    Action action = new TakeTwoAction();
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, TakeTwoAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests TakeToken action is deserialized properly .
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks TakeToken is deserialized properly")
  public void testTakeTokenAction() {
    Action action = new TakeTokenAction();
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, TakeTokenAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests TakeToken action is deserialized properly 2.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks TakeToken is deserialized properly 2")
  public void testTakeTokenAction2() {
    Action action = new TakeTokenAction(Optional.empty());
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, TakeTokenAction.class);
    assertEquals(action, deserializedAction);
  }


  /**
   * Tests TakeToken action is deserialized properly 3.
   */
  @SneakyThrows
  @Test
  @DisplayName("Checks TakeToken is deserialized properly 3")
  public void testTakeTokenAction3() {
    Action action = new TakeTokenAction(Optional.of(Gem.DIAMOND));
    String serialized = om.writeValueAsString(action);
    Action deserializedAction = om.readValue(serialized, TakeTokenAction.class);
    assertEquals(action, deserializedAction);
  }


}
