package com.hexanome16.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import java.util.ArrayList;
import models.price.Gem;
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
      return new ResponseEntity<>("Game doesnt exist", HttpStatus.BAD_REQUEST);
    }
    ArrayList<Gem> listAvailableForTwo = currentGame.availableTwoTokensType();
    ArrayList<String> listAvailableForTwoBonusType = new ArrayList<>(listAvailableForTwo.stream()
        .map(Gem::getBonusType).toList());
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(listAvailableForTwoBonusType), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> availableThreeTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameManager.getGame(sessionId);
    if (currentGame == null) {
      return new ResponseEntity<>("Game doesnt exist", HttpStatus.BAD_REQUEST);
    }
    ArrayList<Gem> listAvailableForThree = currentGame.availableThreeTokensType();
    ArrayList<String> listAvailableForThreeBonusType =
        new ArrayList<>(listAvailableForThree.stream()
            .map(Gem::getBonusType).toList());
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(listAvailableForThreeBonusType), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> takeTwoTokens(long sessionId, String authenticationToken,
                                              String tokenType) {

    ResponseEntity<String> validity = validRequest(sessionId, authenticationToken);
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = gameManager.getGame(sessionId);
    Player requestingPlayer = gameService.findPlayerByToken(currentGame, authenticationToken);
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

    ResponseEntity<String> validity = validRequest(sessionId, authenticationToken);
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = gameManager.getGame(sessionId);
    Player requestingPlayer = gameService.findPlayerByToken(currentGame, authenticationToken);

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
  public ResponseEntity<String> giveBackToken(long sessionId,
                                              String authenticationToken, String tokenType) {
    return null;
  }


  // HELPERS /////////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns HTTPS_OK if game with sessionId exists, if the authToken can be verified,
   * if such an id gives is owned by a real player and if it is that player's turn.
   * Returns HTTPS_BAD_REQUEST otherwise.
   *
   * @param sessionId game's identification number.
   * @param authToken access token.
   * @return true if the request is appropriate, false otherwise
   */
  public ResponseEntity<String> validRequest(long sessionId, String authToken) {
    Game currentGame = gameManager.getGame(sessionId);

    if (currentGame == null) {
      return new ResponseEntity<>("Game doesnt exist", HttpStatus.BAD_REQUEST);
    }
    if (!authService.verifyPlayer(sessionId, authToken, currentGame)) {
      return new ResponseEntity<>("Can't verify player", HttpStatus.BAD_REQUEST);
    }

    Player requestingPlayer = gameService.findPlayerByToken(currentGame, authToken);

    if (requestingPlayer == null) {
      return new ResponseEntity<>("Can't find player", HttpStatus.BAD_REQUEST);
    }

    if (currentGame.isNotPlayersTurn(requestingPlayer)) {
      return new ResponseEntity<>("Not player turn", HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
