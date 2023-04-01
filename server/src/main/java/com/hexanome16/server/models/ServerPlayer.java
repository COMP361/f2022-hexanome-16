package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.Player;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.actions.AssociateCardAction;
import com.hexanome16.server.models.actions.ChooseCityAction;
import com.hexanome16.server.models.actions.ChooseNobleAction;
import com.hexanome16.server.models.actions.DiscardTokenAction;
import com.hexanome16.server.models.actions.TakeOneAction;
import com.hexanome16.server.models.actions.TakeTokenAction;
import com.hexanome16.server.models.actions.TakeTwoAction;
import com.hexanome16.server.models.bank.PlayerBank;
import com.hexanome16.server.models.cards.Reservable;
import com.hexanome16.server.models.cards.ServerCity;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.Visitable;
import com.hexanome16.server.models.inventory.Inventory;
import com.hexanome16.server.models.inventory.InventoryAddable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Player class.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerPlayer extends Player {
  private Queue<Action> queueOfCascadingActionTypes;
  private Inventory inventory; // the player has an inventory, not a bank

  /**
   * Player Constructor.
   *
   * @param name            name of the player.
   * @param preferredColour preferred color of the player.
   * @param playerOrder     order of the player.
   */
  public ServerPlayer(String name, String preferredColour, int playerOrder) {
    super(name, preferredColour, playerOrder);
    this.inventory = new Inventory();
    this.queueOfCascadingActionTypes = new LinkedList<>();
  }




  /**
   * Gets bank.
   *
   * @return the bank
   */
  @JsonIgnore
  public PlayerBank getBank() {
    return this.inventory.getPlayerBank();
  }


  /**
   * Add this card to the player's inventory.
   *
   * @param inventoryAddable the development card to add
   * @return true on success
   */
  public boolean addCardToInventory(InventoryAddable inventoryAddable) {
    return inventoryAddable.addToInventory(this.inventory);
  }

  /**
   * Remove this reserved card from the player's inventory.
   *
   * @param inventoryAddable the development card to add
   * @return true on success
   */
  public boolean removeReservedCardFromInventory(InventoryAddable inventoryAddable) {
    return this.inventory.getReservedCards().remove(inventoryAddable);
  }

  /**
   * Returns true if you have at least golden cards amount of golden token bonus orient cards.
   *
   * @param goldenCardsAmount amount of golden cards.
   * @return true or false.
   */
  public boolean hasAtLeastGoldenBonus(int goldenCardsAmount) {
    return inventory.hasAtLeastGoldenBonus(goldenCardsAmount);
  }

  /**
   * Gets the top most gold card.
   *
   * @return top most gold card, null if no such card.
   */
  public ServerLevelCard topGoldCard() {
    return inventory.topGoldCard();
  }

  /**
   * Removes the card from the player inventory.
   *
   * @param card card we want to remove from the inventory.
   */
  public void removeCardFromInventory(ServerLevelCard card) {
    inventory.removeCard(card);
  }


  /**
   * increments Player bank by the amount specified by each parameter for each of their
   * corresponding gem types.
   *
   * @param rubyAmount     amount to increase ruby stack by.
   * @param emeraldAmount  amount to increase emerald stack by.
   * @param sapphireAmount amount to increase sapphire stack by.
   * @param diamondAmount  amount to increase diamond stack by.
   * @param onyxAmount     amount to increase onyx stack by.
   * @param goldAmount     amount to increase gold stack by.
   */
  public void incPlayerBank(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {
    getBank().addGemsToBank(new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount));

  }

  /**
   * Increments the player bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to increment
   *                    the player bank.
   */
  public void incPlayerBank(PurchaseMap purchaseMap) {
    getBank().addGemsToBank(purchaseMap);
  }

  /**
   * Returns true if player has at least specified amounts of each gem type in their bank, false
   * otherwise.
   *
   * @param rubyAmount     minimum amount or rubies player should have
   * @param emeraldAmount  minimum amount or emerald player should have
   * @param sapphireAmount minimum amount or sapphire player should have
   * @param diamondAmount  minimum amount or diamond player should have
   * @param onyxAmount     minimum amount or onyx player should have
   * @param goldAmount     minimum amount or gold player should have
   * @return true if player has at least input amounts of each gem type, false otherwise.
   */
  @JsonIgnore
  public boolean hasAtLeast(int rubyAmount, int emeraldAmount, int sapphireAmount,
                            int diamondAmount, int onyxAmount, int goldAmount) {
    return hasAtLeast(new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount));
  }

  /**
   * Returns true if player has at least specified amounts of each gem type in their bank, false
   * otherwise.
   *
   * @param purchaseMap specified amount for each gem.
   * @return True if it has enough, false otherwise.
   */
  @JsonIgnore
  public boolean hasAtLeast(PurchaseMap purchaseMap) {
    boolean response = true;
    for (Gem gem : Gem.values()) {
      response = response
          && getBank().toPurchaseMap().getGemCost(gem) >= purchaseMap.getGemCost(gem);
    }
    return response;
  }


  /**
   * Reserve this card.
   *
   * @param reservable the card to reserve
   * @return true on success
   */
  public boolean reserveCard(Reservable reservable) {
    return reservable.reserveCard(this.inventory);
  }

  /**
   * Delete inventory.
   */
  public void deleteInventory() {
    this.inventory = null;
  }

  /**
   * Verifies that the player meets the requirements to be visited by said visitable.
   *
   * @param visitor the visitor whose requirements need to be met
   * @return true if player meets requirements
   */
  @JsonIgnore
  public boolean canBeVisitedBy(Visitable visitor) {
    return visitor.playerMeetsRequirements(inventory);
  }

  /**
   * true if player needs to discard tokens before ending their turn.
   *
   * @return true or false.
   */
  public boolean hasToDiscardTokens() {
    return inventory.hasMoreThanTenTokens();
  }

  /**
   * decreases player bank by purchaseMap.
   *
   * @param purchaseMap purchase map.
   */
  public void decPlayerBank(PurchaseMap purchaseMap) {
    inventory.getPlayerBank().removeGemsFromBank(purchaseMap);
  }

  /**
   * returns a set of all the gem bonuses the player owns.
   *
   * @return set of owned Gems.
   */
  public Set<Gem> ownedGemBonuses() {
    Set<Gem> gems = new HashSet<>();
    for (ServerLevelCard card : inventory.getOwnedCards()) {
      for (Gem gem : Gem.values()) {
        if (card.getGemBonus().getGemCost(gem) > 0) {
          gems.add(gem);
        }
      }
    }
    return gems;
  }

  /**
   * Takes in a price and returns another price with the discounted funds.
   *
   * @param originalPrice the original price we want to discount.
   * @return Price Interface of the discounted price.
   */
  public PriceInterface discountPrice(PriceInterface originalPrice) {
    PriceMap pm = new PriceMap();
    Gem[] gems = Gem.values();
    ArrayList<Gem> gemList = new ArrayList<>(Arrays.asList(gems));
    gemList.remove(Gem.GOLD);
    PurchaseMap ownedBonuses = inventory.getGemBonuses();
    for (Gem gem : gemList) {
      int difference = originalPrice.getGemCost(gem) - ownedBonuses.getGemCost(gem);
      pm.addGems(gem, Math.max(difference, 0));
    }
    return pm;
  }


  // ACTION QUEUE RELATED SHENANIGANS ////////////////////////////////////////////////////////////

  /**
   * Adds action to action queue.
   *
   * @param action action.
   */
  public void addActionToQueue(Action action) {
    queueOfCascadingActionTypes.add(action);
  }

  /**
   * Gets but doesn't remove top most action in the action queue of the player.
   *
   * @return action that needs to performed by player or null if empty.
   * @throws NullPointerException if queue is empty.
   */
  public Action peekTopAction() {
    System.out.println(queueOfCascadingActionTypes);
    return queueOfCascadingActionTypes.peek();
  }

  /**
   * Removes the top action from the queue.
   */
  public void removeTopAction() {
    queueOfCascadingActionTypes.poll();
  }

  ///////   ACTION RELATED STUFF // MOVED HERE FOR GOOD CODE :D //////////////////////////////////

  /**
   * Adds Noble Choice as an action that needs to be performed.
   *
   * @param nobleList list of nobles to choose from. Not empty please.
   * @throws JsonProcessingException thrown if nobles cannot be parsed
   */
  public void addNobleListToPerform(ArrayList<Noble> nobleList) throws JsonProcessingException {
    addActionToQueue(new ChooseNobleAction(nobleList.toArray(new Noble[0])));
  }

  /**
   * Adds Cities Choice as an action that needs to be performed.
   *
   * @param citiesList list of cities to choose from. Not empty please.
   * @throws JsonProcessingException thrown if cities cannot be parsed
   */
  public void addCitiesToPerform(ArrayList<ServerCity> citiesList) throws JsonProcessingException {
    addActionToQueue(new ChooseCityAction(citiesList.toArray(new ServerCity[0])));
  }

  /**
   * Adds Take Two as an action that needs to be performed.
   */
  public void addTakeTwoToPerform() {
    addActionToQueue(new TakeTwoAction());
  }

  /**
   * Adds Take One as an action that needs to be performed.
   */
  public void addTakeOneToPerform() {
    addActionToQueue(new TakeOneAction());
  }

  /**
   * Adds Discard token as an action that needs to be performed.
   */
  public void addDiscardTokenToPerform() {
    Gem[] gems = inventory.getOwnedTokenTypes();
    addActionToQueue(new DiscardTokenAction(gems));
  }

  /**
   * adds Acquire card as an action that needs to be perfromed.
   *
   * @param acquiredCard card to which we will be associating,
   *                     acquiredCard needs to be a bag type card.
   * @throws JsonProcessingException if fails if json fails.
   */
  public void addAcquireCardToPerform(ServerLevelCard acquiredCard) throws JsonProcessingException {
    assert acquiredCard.isBag();
    addActionToQueue(new AssociateCardAction(acquiredCard));
  }

  /**
   * Adds Take Token as an action that needs to be performed.
   *
   * @param gem (optional) gem that cannot be taken
   */
  public void addTakeTokenToPerform(Optional<Gem> gem) {
    addActionToQueue(new TakeTokenAction(gem));
  }





  ////////////////////////////////////////////////////////////////////////////////////////////////


}
