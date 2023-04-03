package com.hexanome16.server.models;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.hexanome16.common.models.Level;
import com.hexanome16.server.models.bank.GameBank;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.winconditions.WinCondition;
import java.util.HashMap;
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
    List<Game> gameDummies = List.of(Mockito.mock(Game.class), Mockito.mock(Game.class));
    for (Game dummy : gameDummies) {
      when(dummy.getPlayers()).thenReturn(PlayerDummies.validDummies);
      when(dummy.getCurrentPlayer()).thenReturn(PlayerDummies.validDummies[0]);
      when(dummy.getCurrentPlayerIndex()).thenReturn(0);
      when(dummy.getWinCondition()).thenReturn(WinCondition.BASE);
      when(dummy.getOnBoardDecks()).thenReturn(new HashMap<>());
      when(dummy.getOnBoardNobles()).thenReturn(new Deck<>());
      when(dummy.getOnBoardCities()).thenReturn(new Deck<>());
      when(dummy.getLevelDeck(any(Level.class))).thenReturn(new Deck<>());
      when(dummy.getRemainingNobles()).thenReturn(new HashMap<>());
      when(dummy.getRemainingCities()).thenReturn(new HashMap<>());
      when(dummy.getGameBank()).thenReturn(new GameBank());
      doReturn(false).when(dummy).isNotPlayersTurn(eq(PlayerDummies.validDummies[0]));
    }
    validGames = gameDummies;
    return validGames;
  }
}
