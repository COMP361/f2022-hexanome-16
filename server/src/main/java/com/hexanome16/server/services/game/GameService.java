package com.hexanome16.server.services.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import dto.PlayerJson;
import dto.WinJson;
import java.util.Arrays;
import lombok.NonNull;
import models.Level;
import models.price.Gem;
import models.price.PriceInterface;
import models.price.PurchaseMap;
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
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;

  /**
   * Instantiates the game service.
   *
   * @param authService        the authentication service used to validate requests
   * @param gameManagerService the game manager service used to find games
   */
  public GameService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService) {
    this.authService = authService;
    this.gameManagerService = gameManagerService;
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  }

  @Override
  public ResponseEntity<String> getPlayerBankInfo(long sessionId, String username)
      throws JsonProcessingException {
    Game game = gameManagerService.getGame(sessionId);
    if (game == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    // get player with username
    Player concernedPlayer = findPlayerByName(game, username);

    // Player not in game
    if (concernedPlayer == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    PurchaseMap playerBankMap = concernedPlayer.getBank().toPurchaseMap();

    return new ResponseEntity<>(objectMapper.writeValueAsString(playerBankMap.getPriceMap()),
        HttpStatus.OK);
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
  public void endCurrentPlayersTurn(Game game) {
    game.goToNextPlayer();
    int nextPlayerIndex = game.getCurrentPlayerIndex();
    if (nextPlayerIndex == 0) {
      Player[] winners = game.getWinCondition().isGameWon(game);
      if (winners.length > 0) {
        game.getBroadcastContentManagerMap().updateValue(
            BroadcastMapKey.WINNERS,
            new WinJson(Arrays.stream(winners).map(Player::getName).toArray(String[]::new))
        );
      }
    } else {
      game.getBroadcastContentManagerMap().updateValue(
          BroadcastMapKey.PLAYERS,
          new PlayerJson(game.getCurrentPlayer().getName())
      );
    }
  }

  @Override
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String authenticationToken,
                                        PurchaseMap proposedDeal)
      throws JsonProcessingException {

    var request = validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final Player player = request.getRight().getRight();


    // Fetch the card in question
    LevelCard cardToBuy = DeckHash.getCardFromDeck(cardMd5);

    // TODO test
    // TODO add http error
    if (cardToBuy == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Verify player is who they claim to be
    if (!authService.verifyPlayer(authenticationToken, game)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get card price as a priceMap
    PriceInterface cardPriceMap = cardToBuy.getCardInfo().price();

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (!proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    System.out.println("PLAYER FOUND");
    System.out.println(player.getName());


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!player.hasAtLeast(
        proposedDeal.getGemCost(Gem.RUBY),
        proposedDeal.getGemCost(Gem.EMERALD),
        proposedDeal.getGemCost(Gem.SAPPHIRE),
        proposedDeal.getGemCost(Gem.DIAMOND),
        proposedDeal.getGemCost(Gem.ONYX),
        proposedDeal.getGemCost(Gem.GOLD)
        ) || game.isNotPlayersTurn(player)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Increase Game Bank and decrease player funds
    game.incGameBankFromPlayer(player,
        proposedDeal.getGemCost(Gem.RUBY),
        proposedDeal.getGemCost(Gem.EMERALD),
        proposedDeal.getGemCost(Gem.SAPPHIRE),
        proposedDeal.getGemCost(Gem.DIAMOND),
        proposedDeal.getGemCost(Gem.ONYX),
        proposedDeal.getGemCost(Gem.GOLD));


    // Add that card to the player's Inventory
    player.addCardToInventory(cardToBuy);

    // Remove card from the board
    game.removeOnBoardCard(cardToBuy);

    Level level = (cardToBuy).getLevel();
    // Add new card to the deck
    game.addOnBoardCard(level);


    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckHash(game, level)
    );

    // Ends players turn, which is current player
    endCurrentPlayersTurn(game);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId           game session id.
   * @param cardMd5             card hash.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  public ResponseEntity<String> reserveCard(long sessionId,
                                            String cardMd5,
                                            String authenticationToken)
      throws JsonProcessingException {

    var request = validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }
    final Game game = request.getRight().getLeft();
    final Player player = request.getRight().getRight();

    LevelCard card = DeckHash.getCardFromDeck(cardMd5);


    if (card == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    if (!player.reserveCard(card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    //TODO: probably need a check to only remove level cards from board

    // replace this card with a new one on board
    game.removeOnBoardCard(card);
    Level level = (card).getLevel();
    game.addOnBoardCard(level);

    // Notify long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckHash(game, level)
    );

    endCurrentPlayersTurn(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId           game session id.
   * @param level               deck level.
   * @param authenticationToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   */
  public ResponseEntity<String> reserveFaceDownCard(long sessionId,
                                                    String level,
                                                    String authenticationToken) {

    var request = validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final Player player = request.getRight().getRight();

    Level atLevel = switch (level) {
      case "THREE" -> Level.THREE;
      case "TWO" -> Level.TWO;
      case "ONE" -> Level.ONE;
      default -> null;
    };

    if (atLevel == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    }

    LevelCard card = game.getLevelDeck(atLevel).nextCard();

    if (!player.reserveCard(card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    endCurrentPlayersTurn(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public Player findPlayerByName(@NonNull Game game, String username) {
    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
  public Player findPlayerByToken(@NonNull Game game, String accessToken) {
    ResponseEntity<String> usernameEntity = authService.getPlayer(accessToken);

    String username = usernameEntity.getBody();


    for (Player e : game.getPlayers()) {
      if (e.getName().equals(username)) {
        return e;
      }
    }
    return null;
  }

  @Override
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequestAndCurrentTurn(
      long sessionId,
      String authToken) {

    var response = validRequest(sessionId, authToken);
    if (!response.getLeft().getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game currentGame = response.getRight().getLeft();
    final Player requestingPlayer = response.getRight().getRight();

    if (currentGame.isNotPlayersTurn(requestingPlayer)) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.NOT_PLAYERS_TURN),
          new ImmutablePair<>(null, null));
    }

    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
        new ImmutablePair<>(currentGame, requestingPlayer));
  }

  @Override
  public Pair<ResponseEntity<String>, Pair<Game, Player>> validRequest(long sessionId,
                                                                       String authToken) {
    final Game currentGame = gameManagerService.getGame(sessionId);

    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID),
          new ImmutablePair<>(null, null));
    }

    boolean isValidPlayer = authService.verifyPlayer(authToken, currentGame);
    Player requestingPlayer = findPlayerByToken(currentGame, authToken);

    if (!isValidPlayer || requestingPlayer == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_ACCESS_TOKEN),
          new ImmutablePair<>(null, null));
    }

    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK),
        new ImmutablePair<>(currentGame, requestingPlayer));
  }

  @Override
  public Pair<ResponseEntity<String>, Game> validGame(long sessionId) {
    final Game currentGame = gameManagerService.getGame(sessionId);
    if (currentGame == null) {
      return new ImmutablePair<>(
          CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID), null);
    }
    return new ImmutablePair<>(new ResponseEntity<>(HttpStatus.OK), currentGame);
  }

}
