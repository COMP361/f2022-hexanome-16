package com.hexanome16.server.services;

import static org.mockito.Mockito.when;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Dummy Game service for usage in tests.
 */
public class DummyGameService {
  private static final GameServiceInterface mock = Mockito.mock(GameService.class);

  /**
   * Creates a game service dummy that has pre-mocked auth methods.
   *
   * @return game service dummy
   */
  public static GameServiceInterface getDummyGameService() {
    addValidRequestMocks();
    return mock;
  }

  private static void addValidRequestMocks() {
    var gameDummies = GameDummies.getInstance();
    Game newGame;
    ResponseEntity<String> left;
    Game right;
    for (int i = 0; i < DummyAuths.validSessionIds.size(); i++) {
      newGame = gameDummies.get(i);
      left = new ResponseEntity<>(HttpStatus.OK);
      right = newGame;
      var sessionId = DummyAuths.validSessionIds.get(i);
      when(mock.validGame(sessionId)).thenReturn(Pair.of(left, right));
    }

    for (var id : DummyAuths.invalidSessionIds) {
      left = CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
      when(mock.validGame(id)).thenReturn(Pair.of(left, null));
    }
  }
}
