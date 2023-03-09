package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.server.models.Game;
import com.hexanome16.server.models.ServerLevelCard;
import com.hexanome16.server.models.ServerNoble;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import java.util.ArrayList;
import java.util.Map;
import lombok.SneakyThrows;
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
  private final GameManagerServiceInterface gameManagerService;
  private final ServiceUtils serviceUtils;

  /**
   * Instantiates the inventory service.
   *
   * @param gameManagerService the game manager service used to find games
   * @param serviceUtils       the utility used by services
   */
  public InventoryService(@Autowired GameManagerServiceInterface gameManagerService,
                          @Autowired ServiceUtils serviceUtils) {
    this.serviceUtils = serviceUtils;
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
      return CustomResponseFactory.getResponse(CustomHttpResponses.PLAYER_NOT_IN_GAME);
    }

    PurchaseMap playerBankMap = concernedPlayer.getBank().toPurchaseMap();

    return new ResponseEntity<>(objectMapper.writeValueAsString(playerBankMap.getPriceMap()),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String authenticationToken,
                                        PurchaseMap proposedDeal) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();


    // Fetch the card in question
    ServerLevelCard cardToBuy = game.getCardByHash(cardMd5);

    if (cardToBuy == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }

    // Get card price as a priceMap
    PriceInterface cardPriceMap = cardToBuy.getCardInfo().price();

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (!proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_PROPOSED_DEAL);
    }


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!player.hasAtLeast(
        proposedDeal.getGemCost(Gem.RUBY),
        proposedDeal.getGemCost(Gem.EMERALD),
        proposedDeal.getGemCost(Gem.SAPPHIRE),
        proposedDeal.getGemCost(Gem.DIAMOND),
        proposedDeal.getGemCost(Gem.ONYX),
        proposedDeal.getGemCost(Gem.GOLD)
    )) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INSUFFICIENT_FUNDS);
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

    // Remove the card from the player's reserved cards
    player.removeReservedCardFromInventory(cardToBuy);

    ResponseEntity<String> error =
        addNobleAction(game, player);
    if (error != null) {
      return error;
    }

    Level level = (cardToBuy).getLevel();

    // Remove card from the board and add new card
    if (game.removeOnBoardCard(cardToBuy)) {
      game.addOnBoardCard(level);
    }

    // Receive trade posts
    for (Map.Entry<RouteType, TradePost> tradePost : game.getTradePosts().entrySet()) {
      System.out.println(tradePost.getKey().name());
      if (tradePost.getValue().canBeTakenByPlayerWith(player.getInventory())) {
        System.out.println("can be taken");
        player.addTradePost(tradePost.getValue());
      }
    }

    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckJson(game.getOnBoardDeck(level).getCardList(), level)
    );


    actionUponCardAcquiral(game, player, cardToBuy);

    var nextAction = player.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  private static ResponseEntity<String> addNobleAction(Game game, ServerPlayer player) {
    var noblesList = new ArrayList<Noble>();
    for (ServerNoble noble : game.getRemainingNobles().values()) {
      if (player.canBeVisitedBy(noble)) {
        noblesList.add(noble);
      }
    }
    if (!noblesList.isEmpty()) {
      try {
        player.addNobleListToPerform(noblesList);
      } catch (JsonProcessingException e) {
        return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
      }
    }
    return null;
  }

  // TODO :: Add this methode everywhere when a card is aquired, (like bought
  //  or by cascading, not upon reserving a card)
  private void actionUponCardAcquiral(Game game, ServerPlayer player,
                                      ServerLevelCard acquiredCard) {
    // ACTION RELATED SHENANIGANS
    if (acquiredCard.getBonusType() == LevelCard.BonusType.CASCADING_TWO) {
      player.addTakeTwoToPerform();
    }
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
        serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();

    ServerLevelCard card = game.getCardByHash(cardMd5);


    if (card == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }


    // TODO: what is this for?
    if (!player.reserveCard(card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    Level level = card.getLevel();
    // Remove card from the board and add new card
    if (game.removeOnBoardCard(card)) {
      game.addOnBoardCard(level);
    }

    // Notify long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckJson(game.getOnBoardDeck(level).getCardList(), level)
    );

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
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
        serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken);
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

    ServerLevelCard card = game.getLevelDeck(atLevel).removeNextCard();

    // TODO: check if deck is null

    if (!player.reserveCard(card)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // give player a gold token
    game.incGameBankFromPlayer(player, 0, 0, 0, 0, 0, -1);

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @SneakyThrows
  @Override
  public ResponseEntity<String> takeLevelTwoCard(long sessionId, String authenticationToken,
                                                 String chosenCard) {
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }


    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final ServerLevelCard card = game.getCardByHash(chosenCard);
    if (card == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }
    var currentAction = player.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.LEVEL_TWO) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }

    // remove from board, add to inventory and remove action from queue.
    game.removeOnBoardCard(card);
    player.addCardToInventory(card);
    player.removeTopAction();

    Level level = (card).getLevel();
    // Add new card to the deck
    game.addOnBoardCard(level);

    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckJson(game.getOnBoardDeck(level).getCardList(), level)
    );

    actionUponCardAcquiral(game, player, card);

    var nextAction = player.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @Override
  public ResponseEntity<String> acquireNoble(long sessionId, String nobleHash,
                                             String authenticationToken) {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, authenticationToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();

    var currentAction = player.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.NOBLE) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }

    var noble = game.getNobleByHash(nobleHash);

    if (noble == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }

    if (!player.canBeVisitedBy(noble)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT);
    }

    if (!player.addCardToInventory(noble)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    player.removeTopAction();

    var nextAction = player.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }
}
