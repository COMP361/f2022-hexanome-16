package com.hexanome16.server.models;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;
import org.mockito.Mockito;

/**
 * Game Mockito mocks.
 */
public class GameDummies {

  /**
   * List of valid games.
   */
  private static List<Game> validGames;

  /**
   * Initialize Game mocks.
   *
   * @return a list of valid games
   */
  public static List<Game> getInstance() {
    if (validGames != null) {
      return validGames;
    }
    Game validGame1 = Mockito.mock(Game.class);
    when(validGame1.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(validGame1.getCurrentPlayer()).thenReturn(PlayerDummies.validDummies[0]);
    when(validGame1.getCurrentPlayerIndex()).thenReturn(0);
    doReturn(false).when(validGame1).isNotPlayersTurn(eq(PlayerDummies.validDummies[0]));
    Game validGame2 = Mockito.mock(Game.class);
    when(validGame2.getPlayers()).thenReturn(PlayerDummies.validDummies);
    when(validGame2.getCurrentPlayer()).thenReturn(PlayerDummies.validDummies[0]);
    when(validGame2.getCurrentPlayerIndex()).thenReturn(0);
    doReturn(false).when(validGame2).isNotPlayersTurn(eq(PlayerDummies.validDummies[0]));
    validGames = List.of(validGame1, validGame2);
    return validGames;
  }
}
