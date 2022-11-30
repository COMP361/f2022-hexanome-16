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
  protected int rubyAmount;
  protected int emeraldAmount;
  protected int sapphireAmount;
  protected int diamondAmount;
  protected int onyxAmount;
}
