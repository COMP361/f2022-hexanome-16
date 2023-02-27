package com.hexanome16.server.models.winconditions;

import com.hexanome16.server.models.ServerPlayer;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;

/**
 * This enum represents the different win conditions for the game.
 */
public enum WinCondition {
  /**
   * Base win condition (15 prestige points).
   */
  BASE(player ->
      player.getInventory().getOwnedCards().stream()
          .mapToInt(card -> card.getCardInfo().prestigePoint()).sum()
          + player.getInventory().getOwnedNobles().stream()
          .mapToInt(card -> card.getCardInfo().prestigePoint()).sum() >= 15, "Splendor"),
  /**
   * Win condition for Trade Routes (15 prestige points + 3 trade routes).
   */
  TRADEROUTES(null, "SplendorTradeRoutes"),
  /**
   * Win condition for Cities (15 prestige points + 4 cities).
   */
  CITIES(null, "SplendorCities");

  private final Predicate<ServerPlayer> winCondition;
  @Getter
  private final String assocServerName;

  WinCondition(Predicate<ServerPlayer> winCondition, String assocServerName) {
    this.winCondition = winCondition;
    this.assocServerName = assocServerName;
  }

  /**
   * Get the win condition associated with a server name (based on extension).
   *
   * @param serverName The server name.
   * @return The win condition associated with the server name, or null if invalid server name.
   */
  public static WinCondition fromServerName(String serverName) {
    return Arrays.stream(WinCondition.values())
        .filter(winCondition -> winCondition.assocServerName.equals(serverName)).findFirst()
        .orElse(null);
  }

  /**
   * Check if a game is won.
   *
   * @param players The players to check.
   * @return The player(s) who won the game, or null if no player has won.
   */
  public ServerPlayer[] isMet(ServerPlayer[] players) {
    ServerPlayer[] matchingPlayers =
        Arrays.stream(players).filter(winCondition).toArray(ServerPlayer[]::new);
    return matchingPlayers.length > 0 ? matchingPlayers : new ServerPlayer[0];
  }

  /**
   * Check if a game is won.
   *
   * @param winConditions The win conditions to check.
   * @param players       The players to check.
   * @return The player(s) who won the game, or empty array if no player has won.
   */
  public static ServerPlayer[] getWinners(WinCondition[] winConditions, ServerPlayer[] players) {
    return Arrays.stream(winConditions).map(winCondition -> winCondition.isMet(players))
        .filter(players1 -> players1.length > 0).findFirst().orElse(new ServerPlayer[0]);
  }
}
