package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.actions.Action;
import com.hexanome16.server.models.cards.Deck;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for managing game state requests from
 * the {@link com.hexanome16.server.controllers.GameController}.
 */
@Service
public class GameService implements GameServiceInterface {
  private final ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;
  private final ServiceUtils serviceUtils;

  /**
   * Instantiates the game service.
   *
   * @param authService        the authentication service used to validate requests
   * @param gameManagerService the game manager service used to find games
   * @param serviceUtils       the service utils used to find player.
   */
  public GameService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService,
                     @Autowired ServiceUtils serviceUtils) {
    this.authService = authService;
    this.gameManagerService = gameManagerService;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    this.serviceUtils = serviceUtils;
  }



  @Override
  public ResponseEntity<String> getGameBankInfo(long sessionId) throws JsonProcessingException {
    var request = validGame(sessionId);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }
    Game game = request.getRight();

    PurchaseMap gameBankMap = game.getGameBank().toPurchaseMap();


    return new ResponseEntity<>(objectMapper.writeValueAsString(gameBankMap.getPriceMap()),
        HttpStatus.OK);
  }

  @Override
  public Pair<ResponseEntity<String>, Game> validGame(long sessionId) {
    final Game currentGame = gameManagerService.getGame(sessionId);
    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID), null);
    }
    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK), currentGame);
  }

  @Override
  public ResponseEntity<String> getLevelTwoOnBoard(long sessionId) throws JsonProcessingException {
    Game game = gameManagerService.getGame(sessionId);
    Deck<ServerLevelCard> levelTwoNormal = game.getOnBoardDecks().get(Level.TWO);
    Deck<ServerLevelCard> levelTwoRed = game.getOnBoardDecks().get(Level.REDTWO);
    ArrayList<ServerLevelCard> allLevelTwoCards = new ArrayList<>();

    allLevelTwoCards.addAll(levelTwoNormal.getCardList());
    allLevelTwoCards.addAll(levelTwoRed.getCardList());

    return CustomResponseFactory
        .getCustomResponse(CustomHttpResponses.OK,
            objectMapper.writeValueAsString(allLevelTwoCards.toArray()),
            null);
  }

  @Override
  public ResponseEntity<String> getLevelOneOnBoard(long sessionId) throws JsonProcessingException {
    Game game = gameManagerService.getGame(sessionId);
    Deck<ServerLevelCard> levelTwoNormal = game.getOnBoardDecks().get(Level.ONE);
    Deck<ServerLevelCard> levelTwoRed = game.getOnBoardDecks().get(Level.REDONE);
    ArrayList<ServerLevelCard> allLevelTwoCards = new ArrayList<>();

    allLevelTwoCards.addAll(levelTwoNormal.getCardList());
    allLevelTwoCards.addAll(levelTwoRed.getCardList());

    return CustomResponseFactory
        .getCustomResponse(CustomHttpResponses.OK,
            objectMapper.writeValueAsString(allLevelTwoCards.toArray()),
            null);
  }

  @Override
  public ResponseEntity<String> getPlayerAction(long sessionId, String accessToken) {
    Game game = gameManagerService.getGame(sessionId);
    if (game == null) {
      return
          CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }
    ServerPlayer player = serviceUtils.findPlayerByToken(game, accessToken);

    if (player == null) {
      return CustomResponseFactory
          .getResponse(CustomHttpResponses.PLAYER_NOT_IN_GAME);
    }

    Action action = player.peekTopAction();

    if (action == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
    }

    return action.getActionDetails();
  }

}
