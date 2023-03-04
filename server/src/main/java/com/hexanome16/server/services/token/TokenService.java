package com.hexanome16.server.services.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.game.GameServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import java.util.ArrayList;
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

  private final GameManagerServiceInterface gameManagerService;
  private final AuthServiceInterface authService;

  private final GameServiceInterface gameService;
  private final GameManagerServiceInterface gameManager;

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ServiceUtils serviceUtils;

  /**
   * Constructor for the Token service Class.
   * @param authService  Needed for player verification.
   * @param gameManager  Game manager for fetching game instances
   * @param serviceUtils the utility used by services
   * @param gameService Needed for getting games and such.
   * @param gameManager Game manager for fetching game instances
   */
  public TokenService(@Autowired AuthServiceInterface authService,
                      @Autowired GameManagerServiceInterface gameManager,
                      @Autowired ServiceUtils serviceUtils,
                      @Autowired GameServiceInterface gameService) {
    this.serviceUtils = serviceUtils;
    this.authService = authService;
    this.gameManagerService = gameManager;
    this.gameService = gameService;
    this.gameManager = gameManager;
  }

  @Override
  public ResponseEntity<String> availableTwoTokensType(long sessionId)
      throws JsonProcessingException {
    Game currentGame = gameManagerService.getGame(sessionId);
    if (currentGame == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
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
    Game currentGame = gameManagerService.getGame(sessionId);
    if (currentGame == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
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

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = request.getRight().getLeft();
    ServerPlayer requestingPlayer = request.getRight().getRight();

    Gem desiredGem = Gem.getGem(tokenType);

    if (!currentGame.allowedTakeTwoOf(desiredGem)) {
      return new ResponseEntity<>("Can't take 2 of desired token type", HttpStatus.BAD_REQUEST);
    }

    currentGame.giveTwoOf(desiredGem, requestingPlayer);
    serviceUtils.endCurrentPlayersTurn(currentGame);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> takeThreeTokens(long sessionId, String authenticationToken,
                                                String tokenTypeOne, String tokenTypeTwo,
                                                String tokenTypeThree) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = request.getRight().getLeft();
    ServerPlayer requestingPlayer = request.getRight().getRight();

    Gem desiredGemOne = Gem.getGem(tokenTypeOne);
    Gem desiredGemTwo = Gem.getGem(tokenTypeTwo);
    Gem desiredGemThree = Gem.getGem(tokenTypeThree);

    if (!currentGame.allowedTakeThreeOf(desiredGemOne, desiredGemTwo, desiredGemThree)) {
      return new ResponseEntity<>("Can't take 3 of desired token types", HttpStatus.BAD_REQUEST);
    }

    currentGame.giveThreeOf(desiredGemOne, desiredGemTwo, desiredGemThree, requestingPlayer);
    serviceUtils.endCurrentPlayersTurn(currentGame);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  // TODO : Not Implemented
  @Override
  public ResponseEntity<String> giveBackToken(long sessionId, String authenticationToken,
                                              String tokenType) {
    return null;
  }
}
