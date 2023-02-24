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
import com.hexanome16.server.util.ServiceUtils;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.util.Arrays;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;


@Service
public class InventoryService implements InventoryServiceInterface{

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;
  private final ServiceUtils serviceUtils;

  public InventoryService(@Autowired AuthServiceInterface authService,
                     @Autowired GameManagerServiceInterface gameManagerService,
                     @Autowired ServiceUtils serviceUtils) {
    this.serviceUtils = serviceUtils;
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
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String authenticationToken,
                                        int rubyAmount, int emeraldAmount, int sapphireAmount,
                                        int diamondAmount, int onyxAmount, int goldAmount)
      throws JsonProcessingException {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
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

    // Get proposed Deal as a purchase map
    PurchaseMap proposedDeal =
        new PurchaseMap(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount, onyxAmount,
            goldAmount);

    // Get card price as a priceMap
    PriceInterface cardPriceMap = cardToBuy.getCardInfo().price();

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (!proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    System.out.println("PLAYER FOUND");
    System.out.println(player.getName());


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    if (!player.hasAtLeast(rubyAmount, emeraldAmount, sapphireAmount, diamondAmount,
        onyxAmount, goldAmount)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // Increase Game Bank and decrease player funds
    game.incGameBankFromPlayer(player, rubyAmount, emeraldAmount, sapphireAmount,
        diamondAmount, onyxAmount, goldAmount);


    // Add that card to the player's Inventory
    player.addCardToInventory(cardToBuy);

    Level level = (cardToBuy).getLevel();

    // Remove card from player's reservation inventory
    if(!player.getInventory().getReservedCards().remove(cardToBuy)){
      // Remove card from the board
      game.removeOnBoardCard(cardToBuy);
      // Add new card to the deck
      game.addOnBoardCard(level);
    }

    // Update long polling
    ((BroadcastContentManager<DeckHash>) (game.getBroadcastContentManagerMap()
        .get((cardToBuy).getLevel().name()))).updateBroadcastContent(new DeckHash(game, level));

    // Ends players turn, which is current player
    serviceUtils.endCurrentPlayersTurn(game);

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

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
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
    ((BroadcastContentManager<DeckHash>) (game.getBroadcastContentManagerMap()
        .get((card).getLevel().name()))).updateBroadcastContent(new DeckHash(game, level));

    serviceUtils.endCurrentPlayersTurn(game);
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

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
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

    serviceUtils.endCurrentPlayersTurn(game);
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

}
