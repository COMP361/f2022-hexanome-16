package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.price.Gem;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for managing taking tokens requests from
 * the {@link com.hexanome16.server.controllers.TokensController}.
 */
@Service
public class TokenService implements TokenServiceInterface {

  private final GameServiceInterface gameService;
  private final GameManagerServiceInterface gameManager;
  private final AuthServiceInterface authService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Constructor for the Token service Class.
   *
   * @param gameService Needed for getting games and such.
   * @param authService Needed for player verification.
   * @param gameManager Game manager for fetching game instances
   */
  public TokenService(@Autowired GameServiceInterface gameService,
                      @Autowired AuthServiceInterface authService,
                      @Autowired GameManagerServiceInterface gameManager) {
    this.gameService = gameService;
    this.authService = authService;
    this.gameManager = gameManager;
  }

  @Override
  public ResponseEntity<String> availableTwoTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameManager.getGame(sessionId);
    if (currentGame == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }
    ArrayList<Gem> listAvailableForTwo = currentGame.availableTwoTokensType();
    ArrayList<String> listAvailableForTwoBonusType =
        new ArrayList<>(listAvailableForTwo.stream().map(Gem::getBonusType).toList());
    return new ResponseEntity<>(objectMapper.writeValueAsString(listAvailableForTwoBonusType),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> availableThreeTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameManager.getGame(sessionId);
    if (currentGame == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }
    ArrayList<Gem> listAvailableForThree = currentGame.availableThreeTokensType();
    ArrayList<String> listAvailableForThreeBonusType =
        new ArrayList<>(listAvailableForThree.stream().map(Gem::getBonusType).toList());
    return new ResponseEntity<>(objectMapper.writeValueAsString(listAvailableForThreeBonusType),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> takeTwoTokens(long sessionId, String authenticationToken,
                                              String tokenType) {

    var request = validRequest(sessionId, authenticationToken);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = request.getRight().getLeft();
    Player requestingPlayer = request.getRight().getRight();

    Gem desiredGem = Gem.getGem(tokenType);

    if (!currentGame.allowedTakeTwoOf(desiredGem)) {
      return new ResponseEntity<>("Can't take 2 of desired token type", HttpStatus.BAD_REQUEST);
    }

    currentGame.giveTwoOf(desiredGem, requestingPlayer);
    gameService.endCurrentPlayersTurn(currentGame);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> takeThreeTokens(long sessionId, String authenticationToken,
                                                String tokenTypeOne, String tokenTypeTwo,
                                                String tokenTypeThree) {

    var request = validRequest(sessionId, authenticationToken);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = request.getRight().getLeft();
    Player requestingPlayer = request.getRight().getRight();

    Gem desiredGemOne = Gem.getGem(tokenTypeOne);
    Gem desiredGemTwo = Gem.getGem(tokenTypeTwo);
    Gem desiredGemThree = Gem.getGem(tokenTypeThree);

    if (!currentGame.allowedTakeThreeOf(desiredGemOne, desiredGemTwo, desiredGemThree)) {
      return new ResponseEntity<>("Can't take 3 of desired token types", HttpStatus.BAD_REQUEST);
    }

    currentGame.giveThreeOf(desiredGemOne, desiredGemTwo, desiredGemThree, requestingPlayer);
    gameService.endCurrentPlayersTurn(currentGame);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : Not Implemented
  @Override
  public ResponseEntity<String> giveBackToken(long sessionId, String authenticationToken,
                                              String tokenType) {
    return null;
  }


  // HELPERS /////////////////////////////////////////////////////////////////////////////////////

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
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequest(long sessionId,
                                                                       String authToken) {
    Game currentGame = gameManager.getGame(sessionId);

    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID),
          new ImmutablePair<>(null, null));
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);
    Player requestingPlayer = gameService.findPlayerByToken(currentGame, authToken);

    if (!isValidPlayer || requestingPlayer == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN),
          new ImmutablePair<>(null, null));
    }

    if (currentGame.isNotPlayersTurn(requestingPlayer)) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.NOT_PLAYERS_TURN),
          new ImmutablePair<>(null, null));
    }

    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
        new ImmutablePair<>(currentGame, requestingPlayer));
  }

}
