package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import java.util.Map;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Interface for managing game state backend requests.
 */
public interface GameServiceInterface {
  /**
   * Gets game map.
   *
   * @return the game map
   */
  Map<Long, Game> getGameMap();

  /**
   * Create a new game as client requested.
   *
   * @param sessionId sessionId
   * @param payload   the payload
   * @return error if present
   */
  String createGame(long sessionId, Map<String, Object> payload);

  /**
   * Long polling on update on onboard deck.
   *
   * @param sessionId   game session id
   * @param level       deck level
   * @param accessToken account access token
   * @param hash        hash used for long polling
   * @return updated game deck
   */
  DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                 String accessToken,
                                                 String hash);

  /**
   * Returns nobles present on the game board.
   *
   * @param sessionId   session id
   * @param accessToken access token
   * @param hash        the hash
   * @return nobles present on the game board
   */
  DeferredResult<ResponseEntity<String>> getNobles(long sessionId,
                                                   String accessToken,
                                                   String hash);

  /**
   * Return the username of current player.
   *
   * @param sessionId   game id
   * @param accessToken player access token
   * @param hash        hash for long polling
   * @return current player username
   */
  DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId,
                                                          String accessToken,
                                                          String hash);

  /**
   * Allows client to see how many of each gem a player has.
   *
   * @param sessionId sessionId.
   * @param username  username of the player.
   * @return String representation of the Purchase map
   * @throws JsonProcessingException if Json processing fails
   */
  ResponseEntity<String> getPlayerBankInfo(long sessionId,
                                           String username)
      throws JsonProcessingException;

  /**
   * Allows client to see how many of each gem the game bank has.
   *
   * @param sessionId sessionId.
   * @return String representation of the Purchase map
   * @throws JsonProcessingException if Json processing fails
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
   * Allows client to buy card, given that they send a valid way to buy that card.
   *
   * @param sessionId           sessionID.
   * @param cardMd5             Card we want to purchase's md5.
   * @param authenticationToken username of the player trying to buy the card.
   * @param rubyAmount          amount of ruby gems proposed.
   * @param emeraldAmount       amount of emerald gems proposed.
   * @param sapphireAmount      amount of sapphire gems proposed.
   * @param diamondAmount       amount of diamond gems proposed.
   * @param onyxAmount          amount of onyx gems proposed.
   * @param goldAmount          amount of gold gems proposed.
   * @return <p>HTTP OK if it's the player's turn and the proposed offer is acceptable,
   *         HTTP BAD_REQUEST otherwise.
   *         </p>
   * @throws JsonProcessingException the json processing exception
   */
  ResponseEntity<String> buyCard(long sessionId,
                                 String cardMd5,
                                 String authenticationToken,
                                 int rubyAmount,
                                 int emeraldAmount,
                                 int sapphireAmount,
                                 int diamondAmount,
                                 int onyxAmount,
                                 int goldAmount)
      throws JsonProcessingException;

  /**
   * Let the player reserve a card.
   *
   * @param sessionId game session id.
   * @param cardMd5 card hash.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  ResponseEntity<String> reserveCard(@PathVariable long sessionId,
              @PathVariable String cardMd5,
              @RequestParam String authenticationToken) throws JsonProcessingException;


  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId game session id.
   * @param level deck level.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                             @RequestParam String level,
                                             @RequestParam String authenticationToken)
      throws JsonProcessingException;

  /**
   * Finds a player in a game given their username.
   *
   * @param game     game where player is supposed to be.
   * @param username name of player.
   * @return Player with that username in that game, null if no such player.
   */
  Player findPlayerByName(@NonNull Game game, String username);

  /**
   * Finds player with that authentication token in the game.
   *
   * @param game                game to search.
   * @param accessToken token associated to player
   * @return player with that token, null if no such player
   */
  Player findPlayerByToken(@NonNull Game game, String accessToken);
}
