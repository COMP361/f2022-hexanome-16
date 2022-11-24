package com.hexanome16.server.models;

public class TokenPrice extends Price {
  private PriceMap priceMap;

  public TokenPrice(PriceMap priceMap) {
    this.priceMap = priceMap;
  }

  public PriceMap getPriceMap() {
    return priceMap;
  }
}
