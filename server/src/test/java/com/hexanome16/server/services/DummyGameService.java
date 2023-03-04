package com.hexanome16.server.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.services.game.GameService;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.hexanome16.common.util.CustomHttpResponses;

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
    GameDummies gameDummies = new GameDummies();
    Game newGame;
    ResponseEntity<String> left;
    ImmutablePair<Game, ServerPlayer> right;
    for (int i = 0; i < DummyAuths.validSessionIds.size(); i++) {
      newGame = gameDummies.validGames.get(i);
      left = new ResponseEntity<>(HttpStatus.OK);
      right = new ImmutablePair<>(newGame, newGame.getCurrentPlayer());
      var sessionId = DummyAuths.validSessionIds.get(i);
      var token = DummyAuths.validTokensInfos.get(i).getAccessToken();
      when(mock.validRequestAndCurrentTurn(sessionId, token))
          .thenReturn(
              new ImmutablePair<>(left, right));
    }

    for (var id : DummyAuths.invalidSessionIds) {
      left = CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
      right = new ImmutablePair<>(null, null);
      when(mock.validRequestAndCurrentTurn(eq(id), anyString())).thenReturn(
          new ImmutablePair<>(left, right));
    }
    for (var id : DummyAuths.invalidTokensInfos) {
      left = CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN);
      right = new ImmutablePair<>(null, null);
      when(mock.validRequestAndCurrentTurn(anyLong(), eq(id.getAccessToken()))).thenReturn(
          new ImmutablePair<>(left, right));
    }
  }
}
