package com.hexanome16.server.services.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interface for managing game state backend requests.
 */
public interface GameServiceInterface {
  /**
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws com.fasterxml.jackson.core.JsonProcessingException if Json processing fails
   */
  ResponseEntity<String> getGameBankInfo(long sessionId)
      throws JsonProcessingException;

  /**
   * Ends current player's turn and starts next player's turn.
   *
   * @param game the game the player is in
   */
  void endCurrentPlayersTurn(Game game);

  /**
   * Finds a player in a game given their username.
   *
   * @param game     game where player is supposed to be.
   * @param username name of player.
   * @return Player with that username in that game, null if no such player.
   */
  ServerPlayer findPlayerByName(@NonNull Game game, String username);

  /**
   * Finds player with that authentication token in the game.
   *
   * @param game        game to search.
   * @param accessToken token associated to player
   * @return player with that token, null if no such player
   */
  ServerPlayer findPlayerByToken(@NonNull Game game, String accessToken);

  /**
   * Returns HTTPS_OK if game with sessionId exists, if the authToken can be verified,
   * if such an id gives is owned by a real player and if it is that player's turn.
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and a pair of Game and Player.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game and player will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game and player will be populated.
   * </p>
   *
   * @param sessionId game's identification number.
   * @param authToken access token.
   * @return The pair of response and a pair of game and player
   */
  Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> validRequestAndCurrentTurn(
      long sessionId, String authToken);

  /**
   * Returns HTTPS_OK if game with sessionId exists, if the authToken can be verified,
   * if such an id gives is owned by a real player.
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and a pair of Game and Player.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game and player will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game and player will be populated.
   * </p>
   *
   * @param sessionId game's identification number.
   * @param authToken access token.
   * @return The pair of response and a pair of game and player
   */
  Pair<ResponseEntity<String>, Pair<Game, ServerPlayer>> validRequest(long sessionId,
                                                                      String authToken);

  /**
   * Returns HTTPS_OK if game with sessionId exists,
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * <p>
   * Returns a pair of ResponseEntity and Game.
   * If the request wasn't valid,
   * the ResponseEntity will have an error code and the game will be null,
   * If the request was valid,
   * the ResponseEntity will have a success code and the game will be populated.
   * </p>
   *
   * @param sessionId game's identification number.
   * @return The pair of response and a pair of game and player
   */
  Pair<ResponseEntity<String>, Game> validGame(long sessionId);
}
