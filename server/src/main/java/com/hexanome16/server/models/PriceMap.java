package com.hexanome16.server.models;

import javax.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Holds the token price for a development card.
 */
@Data
@AllArgsConstructor
public class PriceMap {
  /**
   * The Ruby amount.
   */
  protected int rubyAmount;
  /**
   * The Emerald amount.
   */
  protected int emeraldAmount;
  /**
   * The Sapphire amount.
   */
  protected int sapphireAmount;
  /**
   * The Diamond amount.
   */
  protected int diamondAmount;
  /**
   * The Onyx amount.
   */
  protected int onyxAmount;
}
