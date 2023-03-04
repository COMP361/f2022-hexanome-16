package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.DeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.services.auth.AuthServiceInterface;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service is responsible for managing inventory related requests from
 * the {@link com.hexanome16.server.controllers.InventoryController}.
 */
@Service
public class InventoryService implements InventoryServiceInterface {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AuthServiceInterface authService;
  private final GameManagerServiceInterface gameManagerService;
  private final ServiceUtils serviceUtils;

  /**
   * Instantiates the inventory service.
   *
   * @param authService        the authentication service used to validate requests
   * @param gameManagerService the game manager service used to find games
   * @param serviceUtils       the utility used by services
   */
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
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_SESSION_ID);
    }

    // get player with username
    ServerPlayer concernedPlayer = serviceUtils.findPlayerByName(game, username);

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
                                        PurchaseMap proposedDeal) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken,
        gameManagerService, authService);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();


    // Fetch the card in question
    ServerLevelCard cardToBuy = game.getCardByHash(cardMd5);

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
        new DeckJson(game.getLevelDeck(level).getCardList(), level)
    );

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

    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken, gameManagerService,
            authService);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();

    ServerLevelCard card = game.getCardByHash(cardMd5);


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
        new DeckJson(game.getLevelDeck(level).getCardList(), level)
    );

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

    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken, gameManagerService,
            authService);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();

    Level atLevel = switch (level) {
      case "THREE" -> Level.THREE;
      case "TWO" -> Level.TWO;
      case "ONE" -> Level.ONE;
      default -> null;
    };

    if (atLevel == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_LEVEL_INFO);
    }

    ServerLevelCard card = game.getLevelDeck(atLevel).nextCard();

    if (!player.reserveCard(card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    serviceUtils.endCurrentPlayersTurn(game);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}