package com.hexanome16.server.models.winconditions;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.ToString;

/**
 * This class is used to define a win condition for a game.
 */
@ToString
public abstract class WinCondition {
  @Getter
  private final Predicate<Player> condition;

  /**
   * Constructor.
   *
   * @param condition The condition to check if a player has won.
   */
  public WinCondition(Predicate<Player> condition) {
    this.condition = condition;
  }

  /**
   * Check if a game is won.
   *
   * @param game The game to check.
   * @return The player(s) who won the game, or null if no player has won.
   */
  public Player[] isGameWon(Game game) {
    Player[] matchingPlayers =
        Arrays.stream(game.getPlayers()).filter(condition).toArray(Player[]::new);
    return matchingPlayers.length > 0 ? matchingPlayers : new Player[0];
  }

  /**
   * Check if a game is won.
   *
   * @param gameManager The game manager service to fetch the game.
   * @param sessionId   The session id of the game to check.
   * @return The player(s) who won the game, or null if no player has won.
   */
  public Player[] isGameWon(GameManagerServiceInterface gameManager, Long sessionId) {
    return this.isGameWon(gameManager.getGame(sessionId));
  }
}
