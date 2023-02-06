package com.hexanome16.server.models;

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
  public final List<Game> validGames;
  /**
   * List of invalid games.
   */
  public final List<Game> invalidGames;

  /**
   * Initialize Game mocks.
   */
  public GameDummies() {
    Game validGame1 = Mockito.mock(Game.class);
    when(validGame1.getPlayers()).thenReturn(PlayerDummies.dummies);
    when(validGame1.getCurrentPlayer()).thenReturn(PlayerDummies.dummies[0]);
    when(validGame1.getCurrentPlayerIndex()).thenReturn(0);
    Game validGame2 = Mockito.mock(Game.class);
    when(validGame2.getPlayers()).thenReturn(PlayerDummies.dummies);
    when(validGame2.getCurrentPlayer()).thenReturn(PlayerDummies.dummies[0]);
    when(validGame2.getCurrentPlayerIndex()).thenReturn(0);
    Game invalidGame1 = Mockito.mock(Game.class);
    when(invalidGame1.getPlayers()).thenReturn(PlayerDummies.dummies);
    when(invalidGame1.getCurrentPlayer()).thenReturn(PlayerDummies.dummies[0]);
    when(invalidGame1.getCurrentPlayerIndex()).thenReturn(0);
    Game invalidGame2 = Mockito.mock(Game.class);
    when(invalidGame2.getPlayers()).thenReturn(PlayerDummies.dummies);
    when(invalidGame2.getCurrentPlayer()).thenReturn(PlayerDummies.dummies[0]);
    when(invalidGame2.getCurrentPlayerIndex()).thenReturn(0);

    validGames = List.of(validGame1, validGame2);
    invalidGames = List.of(invalidGame1, invalidGame2);
  }
}
