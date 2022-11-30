package com.hexanome16.server.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Stores the price of a card that you need to pay with tokens.
 */
@Getter
@AllArgsConstructor
public class TokenPrice extends Price {
  private final PriceMap priceMap;
}
