package com.hexanome16.server.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Price for purchasing the red card with cards.
 */
@Getter
@AllArgsConstructor
public class CardPrice extends Price {
  private Gem gem;
}
