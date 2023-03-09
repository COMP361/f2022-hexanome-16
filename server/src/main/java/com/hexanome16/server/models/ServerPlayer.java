package com.hexanome16.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.Player;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.bank.PlayerBank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import lombok.Getter;

/**
 * Player class.
 */
@Getter
public class ServerPlayer extends Player {
  private final Queue<Action> queueOfCascadingActionTypes;
  private Inventory inventory; // the player has an inventory, not a bank

  /**
   * Player Constructor.
   *
   * @param name            name of the player.
   * @param preferredColour preferred color of the player.
   */
  public ServerPlayer(String name, String preferredColour) {
    super(name, preferredColour);
    this.inventory = new Inventory();
    this.queueOfCascadingActionTypes = new LinkedList<>();
  }


  /**
   * Gets bank.
   *
   * @return the bank
   */
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
  public boolean canBeVisitedBy(Visitable visitor) {
    return visitor.playerMeetsRequirements(inventory);
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
  public void addCitiesToPerform(ArrayList<City> citiesList) throws JsonProcessingException {
    addActionToQueue(new ChooseCityAction(citiesList.toArray(new City[0])));
  }

  /**
   * Adds Take Two as an action that needs to be performed.
   */
  public void addTakeTwoToPerform() {
    addActionToQueue(new TakeTwoAction());
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////


}
