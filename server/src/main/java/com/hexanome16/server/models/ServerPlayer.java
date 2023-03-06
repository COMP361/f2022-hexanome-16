package com.hexanome16.server.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.Player;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.bank.PlayerBank;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

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
   * Gets and remove top most action in the action queue of the player.
   *
   * @return Response entity with information on the action that needs to performed by player.
   * @throws JsonProcessingException exception.
   */
  public ResponseEntity<String> peekTopAction() throws JsonProcessingException {
    return Objects.requireNonNull(queueOfCascadingActionTypes.peek()).getActionDetails();
  }



  ///////   ACTION RELATED STUFF // MOVED HERE FOR GOOD CODE :D //////////////////////////////////
  /**
   * Adds Noble Choice as an action that needs to be performed.
   *
   * @param nobleList list of nobles to choose from. Not empty please.
   */
  public void addNobleListToPerform(ArrayList<Noble> nobleList) {
    ObjectMapper objectMapper = new ObjectMapper();
    addActionToQueue(() ->
        CustomResponseFactory.getCustomResponse(CustomHttpResponses.CHOOSE_NOBLE,
            objectMapper.writeValueAsString(nobleList.toArray()), null));
  }

  /**
   * Adds Cities Choice as an action that needs to be performed.
   *
   * @param citiesList list of cities to choose from. Not empty please.
   */
  public void addCitiesToPerform(ArrayList<City> citiesList) {
    ObjectMapper objectMapper = new ObjectMapper();
    addActionToQueue(
        () -> CustomResponseFactory.getCustomResponse(CustomHttpResponses.CHOOSE_CITY,
            objectMapper.writeValueAsString(citiesList.toArray()), null));
  }

  /**
   * Adds Take Two as an action that needs to be performed.
   */
  public void addTakeTwoToPerform() {
    addActionToQueue(() ->
        CustomResponseFactory.getResponse(CustomHttpResponses.TAKE_LEVEL_TWO));
  }

  /**
   * Adds End turn as an action that needs to be performed.
   *
   * @param serviceUtils service util.
   * @param game game.
   */
  public void addEndTurnToPerform(ServiceUtils serviceUtils, Game game) {
    addActionToQueue(() -> {
      serviceUtils.endCurrentPlayersTurn(game);
      queueOfCascadingActionTypes.remove();
      return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
    });
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////


}
