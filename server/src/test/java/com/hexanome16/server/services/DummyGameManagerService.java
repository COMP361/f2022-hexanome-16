package com.hexanome16.server.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hexanome16.common.dto.SessionJson;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.services.game.GameManagerService;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import org.mockito.Mockito;

/**
 * Mockito mock of GameManagerService.
 *
 * <p>Methods:
 * <pre>
 *   {@link GameManagerService#getGame} returns a game from the list of
 *   {@link GameDummies#validGames} if you pass a {@link DummyAuths#validSessionIds}
 *   and {@link DummyAuths#invalidSessionIds} will return null
 * </pre>
 * <pre>
 *   {@link GameManagerService#createGame} returns "success" on a {@link DummyAuths#validSessionIds}
 *   and "failure" on a {@link DummyAuths#invalidSessionIds}
 * </pre>
 */
public class DummyGameManagerService {
  /**
   * Get dummy game manager.
   *
   * @return game manager dummy
   */
  public static GameManagerServiceInterface getDummyGameManagerService() {
    return createDummy();
  }

  private static GameManagerServiceInterface createDummy() {
    GameManagerServiceInterface mock = Mockito.mock(GameManagerService.class);
    var gameDummies = GameDummies.getInstance();
    when(mock.getGame(DummyAuths.validSessionIds.get(0))).thenReturn(gameDummies.get(0));
    when(mock.getGame(DummyAuths.validSessionIds.get(1))).thenReturn(gameDummies.get(1));
    when(mock.getGame(DummyAuths.invalidSessionIds.get(0))).thenReturn(null);
    when(mock.getGame(DummyAuths.invalidSessionIds.get(1))).thenReturn(null);

    when(mock.createGame(eq(DummyAuths.validSessionIds.get(0)), any(SessionJson.class))).thenReturn(
        "success");
    when(mock.createGame(eq(DummyAuths.invalidSessionIds.get(0)),
        any(SessionJson.class))).thenReturn(
        "failure");
    return mock;
  }
}
