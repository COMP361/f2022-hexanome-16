package com.hexanome16.server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

/**
 * Player class.
 */
@Data
public class Player {
  private final String name;
  private final String preferredColour;
  private Inventory inventory; // the player has an inventory, not a bank

  /**
   * Player Constructor.
   *
   * @param name            name of the player.
   * @param preferredColour preferred color of the player.
   */
  @JsonCreator
  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
    this.inventory = new Inventory();
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
    getBank().incBank(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount,
        onyxAmount, goldAmount);

  }

  /**
   * Increments the player bank by the amount specified in the purchase map.
   *
   * @param purchaseMap purchase map representation of how much we want to increment
   *                    the player bank.
   */
  public void incPlayerBank(PurchaseMap purchaseMap) {
    getBank().incBank(purchaseMap.getRubyAmount(), purchaseMap.getEmeraldAmount(),
            purchaseMap.getSapphireAmount(), purchaseMap.getDiamondAmount(),
            purchaseMap.getOnyxAmount(), purchaseMap.getGoldAmount());
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
    return getBank().hasAtLeast(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount,
        onyxAmount, goldAmount);
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


  // HELPERS ///////////////////////////////////////////////////////////////////////////////////////

}
