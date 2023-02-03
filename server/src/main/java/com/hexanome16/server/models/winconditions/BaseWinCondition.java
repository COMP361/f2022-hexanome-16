package com.hexanome16.server.models.winconditions;

import lombok.ToString;

/**
 * This class represents the win condition for the base version of Splendor.
 * (15 prestige points total for owned cards + nobles)
 */
@ToString(callSuper = true)
public class BaseWinCondition extends WinCondition {

  /**
   * Constructor.
   */
  public BaseWinCondition() {

    super(player ->
        player.getInventory().getOwnedCards().stream()
            .mapToInt(card -> card.getCardInfo().prestigePoint()).sum()
            + player.getInventory().getOwnedNobles().stream()
            .mapToInt(card -> card.getCardInfo().prestigePoint()).sum() >= 15);
  }
}
