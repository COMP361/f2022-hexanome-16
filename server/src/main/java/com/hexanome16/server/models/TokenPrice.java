package com.hexanome16.server.models;

/**
 * Stores the price of a card that you need to pay with tokens.
 */
public class TokenPrice extends Price {
  private final PriceMap priceMap;

  public TokenPrice(PriceMap priceMap) {
    this.priceMap = priceMap;
  }

  public PriceMap getPriceMap() {
    return priceMap;
  }
}
