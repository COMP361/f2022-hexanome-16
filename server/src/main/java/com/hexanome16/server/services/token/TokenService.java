package com.hexanome16.server.services.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import java.util.ArrayList;
import java.util.Optional;
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

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ServiceUtils serviceUtils;

  /**
   * Constructor for the Token service Class.
   *
   * @param authService  Needed for player verification.
   * @param gameManager  Game manager for fetching game instances
   * @param serviceUtils the utility used by services
   */
  public TokenService(@Autowired AuthServiceInterface authService,
                      @Autowired GameManagerServiceInterface gameManager,
                      @Autowired ServiceUtils serviceUtils) {
    this.serviceUtils = serviceUtils;
    this.authService = authService;
    this.gameManagerService = gameManager;
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
  public ResponseEntity<String> takeOneToken(long sessionId, String accessToken,
                                             String tokenType) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    Game currentGame = request.getRight().getLeft();
    ServerPlayer requestingPlayer = request.getRight().getRight();

    Gem desiredGem = Gem.getGem(tokenType);

    if (!currentGame.allowedTakeOneOf(desiredGem)) {
      return new ResponseEntity<>("Can't take 1 of desired token type", HttpStatus.BAD_REQUEST);
    }

    currentGame.giveOneOf(desiredGem, requestingPlayer);

    requestingPlayer.removeTopAction();

    actionUponTokenInteraction(currentGame, requestingPlayer);

    var nextAction = requestingPlayer.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(currentGame);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @Override
  public ResponseEntity<String> takeTwoTokens(long sessionId, String accessToken,
                                              String tokenType) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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

    actionUponTokenInteraction(currentGame, requestingPlayer);

    if (currentGame.getWinCondition() == WinCondition.TRADEROUTES
        && requestingPlayer.getInventory().getTradePosts().containsKey(RouteType.DIAMOND_ROUTE)) {
      requestingPlayer.addTakeTokenAction(Optional.ofNullable(desiredGem));
    }

    var nextAction = requestingPlayer.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(currentGame);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }


  @Override
  public ResponseEntity<String> takeThreeTokens(long sessionId, String accessToken,
                                                String tokenTypeOne, String tokenTypeTwo,
                                                String tokenTypeThree) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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

    actionUponTokenInteraction(currentGame, requestingPlayer);

    var nextAction = requestingPlayer.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(currentGame);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @Override
  public ResponseEntity<String> discardToken(long sessionId, String accessToken,
                                             String tokenType) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> validity = request.getLeft();
    if (!validity.getStatusCode().is2xxSuccessful()) {
      return validity;
    }
    final Game currentGame = request.getRight().getLeft();
    final ServerPlayer requestingPlayer = request.getRight().getRight();

    final Gem desiredGem = Gem.getGem(tokenType);

    var currentAction = requestingPlayer.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.DISCARD) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }

    currentGame.takeBackToken(desiredGem, requestingPlayer);

    requestingPlayer.removeTopAction();

    actionUponTokenInteraction(currentGame, requestingPlayer);

    var nextAction = requestingPlayer.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(currentGame);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }


  // HELPERS //////////////////////////////////////////////////////////////

  private void actionUponTokenInteraction(Game game, ServerPlayer player) {
    if (player.hasToDiscardTokens()) {
      player.addDiscardTokenToPerform();
    }
  }
}
