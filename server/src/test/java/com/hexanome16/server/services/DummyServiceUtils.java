package com.hexanome16.server.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.models.GameDummies;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Dummy ServiceUtils for tests.
 */
public class DummyServiceUtils {
  private static final ServiceUtils mock = Mockito.mock(ServiceUtils.class);

  /**
   * Creates a game service dummy that has pre-mocked auth methods.
   *
   * @return game service dummy
   */
  public static ServiceUtils getDummyServiceUtils() {
    addValidRequestMocks();
    addFindPlayerByTokenMocks();
    return mock;
  }

  private static void addFindPlayerByTokenMocks() {
    for (Game game : GameDummies.getInstance()) {
      for (int i = 0; i < DummyAuths.validTokensInfos.size(); ++i) {
        var player = game.getPlayers()[i];
        var token = DummyAuths.validTokensInfos.get(i);
        when(mock.findPlayerByToken(game, token.getAccessToken())).thenReturn(player);
        when(mock.findPlayerByName(game, player.getName())).thenReturn(player);
      }
    }
  }

  private static void addValidRequestMocks() {
    Game newGame;
    ResponseEntity<String> left;
    ImmutablePair<Game, ServerPlayer> right;
    for (int i = 0; i < DummyAuths.validSessionIds.size(); i++) {
      newGame = GameDummies.getInstance().get(i);
      left = new ResponseEntity<>(HttpStatus.OK);
      right = new ImmutablePair<>(newGame, newGame.getCurrentPlayer());
      var sessionId = DummyAuths.validSessionIds.get(i);
      var token = DummyAuths.validTokensInfos.get(0).getAccessToken();
      when(mock.validRequestAndCurrentTurn(eq(sessionId), eq(token))).thenReturn(
          Pair.of(left, right));

      // SessionID works for both players, must return correct player

      when(mock.validRequest(eq(sessionId), eq(token))).thenReturn(Pair.of(left, right));


      token = DummyAuths.validTokensInfos.get(1).getAccessToken();
      right = new ImmutablePair<>(newGame, newGame.getPlayers()[1]);
      when(mock.validRequest(eq(sessionId), eq(token))).thenReturn(Pair.of(left, right));

      // Not player's turn
      left = CustomResponseFactory.getResponse(CustomHttpResponses.NOT_PLAYERS_TURN);
      right = new ImmutablePair<>(null, null);
      when(mock.validRequestAndCurrentTurn(eq(sessionId), eq(token))).thenReturn(
          Pair.of(left, right));
    }

    for (var id : DummyAuths.invalidSessionIds) {
      left = CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
      right = new ImmutablePair<>(null, null);
      when(mock.validRequestAndCurrentTurn(eq(id), any())).thenReturn(Pair.of(left, right));
      when(mock.validRequest(eq(id), anyString())).thenReturn(Pair.of(left, right));
    }
    for (var id : DummyAuths.invalidTokensInfos) {
      left = CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN);
      right = new ImmutablePair<>(null, null);
      when(mock.validRequestAndCurrentTurn(anyLong(), eq(id.getAccessToken()))).thenReturn(
          Pair.of(left, right));
      when(mock.validRequest(anyLong(), eq(id.getAccessToken()))).thenReturn(Pair.of(left, right));
    }
  }
}
