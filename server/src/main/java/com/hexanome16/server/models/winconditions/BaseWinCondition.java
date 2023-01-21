package com.hexanome16.server.models.winconditions;

import com.hexanome16.server.models.DevelopmentCard;
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
            .mapToInt(DevelopmentCard::getPrestigePoint).sum()
            + player.getInventory().getOwnedNobles().stream()
            .mapToInt(DevelopmentCard::getPrestigePoint).sum() >= 15);
  }
}
