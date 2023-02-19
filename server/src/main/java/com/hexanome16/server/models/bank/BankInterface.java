package com.hexanome16.server.models.bank;

import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.models.price.PriceInterface;
import com.hexanome16.server.models.price.PurchaseMap;

/**
 * This interface represents bank operations for a bank.
 */
public interface BankInterface {
  /**
   * Add gems to bank.
   *
   * @param gem    type of gem to be added
   * @param amount the amount of gems to be added
   */
  void addGemsToBank(Gem gem, int amount);

  /**
   * Add gems to bank.
   *
   * @param gems type of gem to be added
   */
  void addGemsToBank(PriceInterface gems);

  /**
   * Remove gems from bank.
   *
   * @param gem    type of gem to be removed
   * @param amount the amount of gems to be removed
   */
  void removeGemsFromBank(Gem gem, int amount);

  /**
   * Remove gems from bank.
   *
   * @param gems type of gem to be removed
   */
  void removeGemsFromBank(PriceInterface gems);

  /**
   * Returns the bank as if it was a Purchase Map. (Use for controllers, to
   * send a bank as a string)
   *
   * @return Purchase map of what the bank looks like.
   */
  PurchaseMap toPurchaseMap();
}
