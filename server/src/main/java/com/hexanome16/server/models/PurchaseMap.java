package com.hexanome16.server.models;

/**
 * Class responsible for representing the gems the player decided to put down
 * to buy a card.
 */
public class PurchaseMap extends PriceMap {

  int goldAmount;

  public PurchaseMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                     int onyxAmount, int goldAmount) {
    super(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount);
    this.goldAmount = goldAmount;
  }

  public int getGoldAmount() {
    return goldAmount;
  }

  // TODO: TEST CASE
  /**
   * Makes a purchase map out of the input price map, sets gold amount in the new purchase
   * map to be 0.
   *
   * @param paPriceMap PriceMap we want to transform.
   * @return a PurchaseMap with the same amount of each gem as the inputted price map.
   */
  public static PurchaseMap toPurchaseMap(PriceMap paPriceMap) {
    int rubyAmount = paPriceMap.rubyAmount;
    int emeraldAmount = paPriceMap.emeraldAmount;
    int sapphireAmount = paPriceMap.sapphireAmount;
    int diamondAmount = paPriceMap.diamondAmount;
    int onyxAmount = paPriceMap.onyxAmount;
    int goldAmount = 0;
    return new PurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);
  }

  public int sumTokensNonJokers() {
    return rubyAmount + emeraldAmount + sapphireAmount + diamondAmount + onyxAmount;
  }


  // TODO: TEST CASE
  /**
   * Checks if implied argument can be used instead of the parameter to commit a purchase.
   *
   * @param otherPriceMap priceMap we want to compare to.
   * @return true if the implied can be used to commit the purchase, false otherwise.
   */
  public boolean canBeUsedToBuy(Object otherPriceMap) {
    if (otherPriceMap == null) {
      return false;
    }
    if (this == otherPriceMap) {
      return true;
    }
    if (this.getClass() != otherPriceMap.getClass()) {
      return false;
    }
    PurchaseMap confirmedOtherPriceMap = (PurchaseMap) otherPriceMap;

    if (this.sumTokensNonJokers() <= confirmedOtherPriceMap.sumTokensNonJokers()
        && this.rubyAmount <= confirmedOtherPriceMap.rubyAmount
        && this.emeraldAmount <= confirmedOtherPriceMap.emeraldAmount
        && this.sapphireAmount <= confirmedOtherPriceMap.sapphireAmount
        && this.diamondAmount <= confirmedOtherPriceMap.diamondAmount
        && this.onyxAmount <= confirmedOtherPriceMap.onyxAmount
    ) {
      return (confirmedOtherPriceMap.sumTokensNonJokers() - this.sumTokensNonJokers())
          <= goldAmount;
    }

    return false;
  }

  @Override
  public String toString() {
    return String.format("ruby : %s \n emerald : %s \n "
        + "sapphire : %s \n diamond : %s \n onyx : %s \n gold : %s ", rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount);
  }
}





