package com.hexanome16.server.action;

import com.hexanome16.server.models.PriceMap;

/**
 * Similar to price map, but its used for payment.
 */
public class PaymentMap extends PriceMap {
  int goldAmount;
  // int amountGoldCards;

  /**
   * Creates a payment map.
   *
   * @param rubyAmount     ruby amount
   * @param emeraldAmount  emerald amount
   * @param sapphireAmount sapphire amount
   * @param diamondAmount  diamond amount
   * @param onyxAmount     onyx amount
   * @param goldAmount     gold amount
   */
  public PaymentMap(int rubyAmount, int emeraldAmount, int sapphireAmount, int diamondAmount,
                    int onyxAmount, int goldAmount) {
    super(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount);
    this.goldAmount = goldAmount;
  }

  public int getGoldAmount() {
    return goldAmount;
  }
}
