package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.server.dto.DeckHash;
import com.hexanome16.server.dto.PlayerJson;
import com.hexanome16.server.dto.WinJson;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.Level;
import com.hexanome16.server.models.LevelCard;
import com.hexanome16.server.models.Player;
import com.hexanome16.server.models.price.PriceInterface;
import com.hexanome16.server.models.price.PurchaseMap;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.util.CustomHttpResponses;
import com.hexanome16.server.util.CustomResponseFactory;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.Arrays;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

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
  public DeferredResult<ResponseEntity<String>> getDeck(long sessionId, String level,
                                                        String accessToken, String hash) {
    Game game = gameManagerService.getGame(sessionId);
    if (!authService.verifyPlayer(accessToken, game)) {
      return ResponseGenerator.getHashBasedUpdate(10000,
          game.getBroadcastContentManagerMap().get(level),
          hash);
    }
    return null;
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getNobles(long sessionId, String accessToken,
                                                          String hash) {
    Game game = gameManagerService.getGame(sessionId);
    if (authService.verifyPlayer(accessToken, game)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000,
          game.getBroadcastContentManagerMap().get("noble"),
          hash);
      return result;
    }
    return null;

  }

  @Override
  public DeferredResult<ResponseEntity<String>> getCurrentPlayer(long sessionId, String accessToken,
                                                                 String hash) {
    Game game = gameManagerService.getGame(sessionId);
    if (authService.verifyPlayer(accessToken, game)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(10000,
          game.getBroadcastContentManagerMap().get("player"),
          hash);
      return result;
    }
    return null;

  }

  @Override
  public DeferredResult<ResponseEntity<String>> getWinners(long sessionId, String accessToken,
                                                           String hash) {
    Game game = gameManagerService.getGame(sessionId);
    if (authService.verifyPlayer(accessToken, game)) {
      DeferredResult<ResponseEntity<String>> result;
      result = ResponseGenerator.getHashBasedUpdate(
          10000, game.getBroadcastContentManagerMap().get("winners"), hash
      );
      return result;
    }
    return null;

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

    return new ResponseEntity<>(
        objectMapper.writeValueAsString(playerBankMap.getPriceMap()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> getGameBankInfo(long sessionId) throws JsonProcessingException {
    Game game = gameManagerService.getGame(sessionId);
    if (game == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }


    PurchaseMap gameBankMap = game.getGameBank().toPurchaseMap();


    return new ResponseEntity<>(
        objectMapper.writeValueAsString(gameBankMap.getPriceMap()), HttpStatus.OK);
  }

  @Override
  public void endCurrentPlayersTurn(Game game) {
    game.goToNextPlayer();
    int nextPlayerIndex = game.getCurrentPlayerIndex();
    if (nextPlayerIndex == 0) {
      Player[] winners = game.getWinCondition().isGameWon(game);
      if (winners.length > 0) {
        BroadcastContentManager<WinJson> broadcastContentManagerWinners =
            (BroadcastContentManager<WinJson>) game.getBroadcastContentManagerMap().get("winners");
        broadcastContentManagerWinners.updateBroadcastContent(new WinJson(
            Arrays.stream(winners).map(Player::getName).toArray(String[]::new)
        ));
      }
    } else {
      BroadcastContentManager<PlayerJson> broadcastContentManagerPlayer =
          (BroadcastContentManager<PlayerJson>) game.getBroadcastContentManagerMap().get("player");
      broadcastContentManagerPlayer.updateBroadcastContent(
          new PlayerJson(game.getCurrentPlayer().getName()));
    }
  }

  @Override
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String authenticationToken,
                                        int rubyAmount, int emeraldAmount, int sapphireAmount,
                                        int diamondAmount, int onyxAmount, int goldAmount)
      throws JsonProcessingException {


    // Fetch the card in question
    LevelCard cardToBuy = DeckHash.getCardFromDeck(cardMd5);

    Game game = gameManagerService.getGame(sessionId);

    //
    if (game == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }
    if (cardToBuy == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Verify player is who they claim to be
    if (!authService.verifyPlayer(authenticationToken, game)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get proposed Deal as a purchase map
    PurchaseMap proposedDeal =
        new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount,
            goldAmount);

    // Get card price as a priceMap
    PriceInterface cardPriceMap = cardToBuy.getCardInfo().price();

    // Get player using found index
    Player clientPlayer = findPlayerByToken(game, authenticationToken);

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (clientPlayer == null
        || !proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    System.out.println("PLAYER FOUND");
    System.out.println(clientPlayer.getName());


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!clientPlayer.hasAtLeast(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount,
        onyxAmount, goldAmount) || game.isNotPlayersTurn(clientPlayer)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Increase Game Bank and decrease player funds
    game.incGameBankFromPlayer(clientPlayer, rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);


    // Add that card to the player's Inventory
    clientPlayer.addCardToInventory(cardToBuy);

    // Remove card from the board
    game.removeOnBoardCard(cardToBuy);

    Level level = (cardToBuy).getLevel();
    // Add new card to the deck
    game.addOnBoardCard(level);


    // Update long polling
    ((BroadcastContentManager<DeckHash>)
        (game.getBroadcastContentManagerMap().get((cardToBuy).getLevel().name())))
        .updateBroadcastContent(new DeckHash(game, level));

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
  public ResponseEntity<String> reserveCard(@PathVariable long sessionId,
                                            @PathVariable String cardMd5,
                                            @RequestParam String authenticationToken)
      throws JsonProcessingException {

    LevelCard card = DeckHash.getCardFromDeck(cardMd5);

    Game game = gameManagerService.getGame(sessionId);

    if (game == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }
    //verify game and player
    if (card == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!authService.verifyPlayer(authenticationToken, game)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Player player = findPlayerByToken(game, authenticationToken);

    if (game.isNotPlayersTurn(player)) {
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
    ((BroadcastContentManager<DeckHash>)
        (game.getBroadcastContentManagerMap().get((card).getLevel().name())))
        .updateBroadcastContent(new DeckHash(game, level));

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
  public ResponseEntity<String> reserveFaceDownCard(@PathVariable long sessionId,
                                                    @RequestParam String level,
                                                    @RequestParam String authenticationToken) {
    Game game = gameManagerService.getGame(sessionId);
    //verify game and player
    if (game == null) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    if (!authService.verifyPlayer(authenticationToken, game)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

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

    Player player = findPlayerByToken(game, authenticationToken);

    if (game.isNotPlayersTurn(player)) {
      return CustomResponseFactory.getErrorResponse(CustomHttpResponses.NOT_PLAYERS_TURN);
    }

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
}
