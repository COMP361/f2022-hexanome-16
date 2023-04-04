package com.hexanome16.server.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexanome16.common.dto.cards.CitiesJson;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.dto.cards.NobleDeckJson;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.models.RouteType;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PriceInterface;
import com.hexanome16.common.models.price.PurchaseMap;
import com.hexanome16.common.util.CustomHttpResponses;
import com.hexanome16.common.util.ObjectMapperUtils;
import com.hexanome16.server.models.ServerPlayer;
import com.hexanome16.server.models.TradePost;
import com.hexanome16.server.models.actions.AssociateCardAction;
import com.hexanome16.server.models.cards.ServerLevelCard;
import com.hexanome16.server.models.cards.ServerNoble;
import com.hexanome16.server.models.game.Game;
import com.hexanome16.server.services.game.GameManagerServiceInterface;
import com.hexanome16.server.services.winconditions.WinCondition;
import com.hexanome16.server.util.CustomResponseFactory;
import com.hexanome16.server.util.ServiceUtils;
import com.hexanome16.server.util.broadcastmap.BroadcastMapKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
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

  private final ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
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
  public ResponseEntity<String> getDiscountedPrice(long sessionId, String cardMd5,
                                                   String accessToken)
      throws JsonProcessingException {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final ServerLevelCard card = game.getCardByHash(cardMd5);
    if (card == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }
    String priceMap = objectMapper
        .writeValueAsString(player.discountPrice(card.getCardInfo().price()));
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.OK, priceMap, null);
  }

  @Override
  public ResponseEntity<String> buyCard(long sessionId, String cardMd5, String accessToken,
                                        OrientPurchaseMap proposedDeal) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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
    if (cardToBuy.isBag() && (player.ownedGemBonuses().isEmpty()
        || (player.ownedGemBonuses().size() == 1 && player.ownedGemBonuses().contains(Gem.GOLD)))) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.NO_BONUS_TO_ASSOCIATE);
    }

    // Get card price as a priceMap
    PriceInterface originalPrice = cardToBuy.getCardInfo().price();
    PriceInterface cardPriceMap = player.discountPrice(originalPrice);

    // Makes sure player is in game && proposed deal is acceptable && player has enough tokens
    if (game.getWinCondition() == WinCondition.TRADEROUTES
        && player.getInventory().getTradePosts().containsKey(RouteType.SAPPHIRE_ROUTE)) {
      if (!proposedDeal.canBeUsedToBuyAlt(PurchaseMap.toPurchaseMap(cardPriceMap))) {
        return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_PROPOSED_DEAL);
      }
    } else {
      if (!proposedDeal.canBeUsedToBuy(PurchaseMap.toPurchaseMap(cardPriceMap))) {
        return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_PROPOSED_DEAL);
      }
    }


    // Last layer of sanity check, making sure player has enough funds to do the purchase.
    // and is player's turn
    if (!player.hasAtLeast(
        proposedDeal.getGemCost(Gem.RUBY),
        proposedDeal.getGemCost(Gem.EMERALD),
        proposedDeal.getGemCost(Gem.SAPPHIRE),
        proposedDeal.getGemCost(Gem.DIAMOND),
        proposedDeal.getGemCost(Gem.ONYX),
        proposedDeal.getGemCost(Gem.GOLD))
        || !player.hasAtLeastGoldenBonus(proposedDeal.getGoldenCardsAmount())) {
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

    // Remove the golden cards from the player banks
    int goldBonusAmount = proposedDeal.getGoldenCardsAmount();
    while (goldBonusAmount > 0) {
      ServerLevelCard goldCard = player.topGoldCard();
      player.removeCardFromInventory(goldCard);
      goldBonusAmount--;
    }

    // Add that card to the player's Inventory
    player.addCardToInventory(cardToBuy);

    // Remove the card from the player's reserved cards
    player.removeReservedCardFromInventory(cardToBuy);

    extensionActionUponPurchase(game, player);

    Level level = (cardToBuy).getLevel();

    // Remove card from the board and add new card
    if (game.removeOnBoardCard(cardToBuy)) {
      game.addOnBoardCard(level);
    }

    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckJson(game.getOnBoardDeck(level).getCardList(), level)
    );


    actionUponCardAcquiral(game, player, cardToBuy);

    return serviceUtils.checkForNextActions(game, player);
  }

  @Override
  public ResponseEntity<String> buySacrificeCard(long sessionId, String cardMd5, String accessToken,
                                                 String firstMd5, String secondMd5) {

    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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

    // Fetch the cards to discard
    ServerLevelCard firstCard = game.getCardByHash(firstMd5);
    ServerLevelCard secondCard = game.getCardByHash(secondMd5);

    // proposed deal is acceptable
    if (firstCard == null || secondCard == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INVALID_PROPOSED_DEAL);
    }

    discardCard(game, player, firstCard);
    discardCard(game, player, secondCard);

    // Add that card to the player's Inventory
    player.addCardToInventory(cardToBuy);

    // Remove the card from the player's reserved cards
    player.removeReservedCardFromInventory(cardToBuy);

    extensionActionUponPurchase(game, player);

    Level level = (cardToBuy).getLevel();

    // Remove card from the board and add new card
    if (game.removeOnBoardCard(cardToBuy)) {
      game.addOnBoardCard(level);
    }

    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(level),
        new DeckJson(game.getOnBoardDeck(level).getCardList(), level)
    );


    actionUponCardAcquiral(game, player, cardToBuy);

    return serviceUtils.checkForNextActions(game, player);
  }

  /**
   * Let the player reserve a face up card.
   *
   * @param sessionId   game session id.
   * @param cardMd5     card hash.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   * @throws JsonProcessingException exception
   */
  public ResponseEntity<String> reserveCard(long sessionId,
                                            String cardMd5,
                                            String accessToken)
      throws JsonProcessingException {

    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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


    actionUponCardReservation(game, player, card);

    return serviceUtils.checkForNextActions(game, player);
  }

  /**
   * Let the player reserve a face down card.
   *
   * @param sessionId   game session id.
   * @param level       deck level.
   * @param accessToken player's authentication token.
   * @return HttpStatus.OK if the request is valid. HttpStatus.BAD_REQUEST otherwise.
   */
  public ResponseEntity<String> reserveFaceDownCard(long sessionId,
                                                    String level,
                                                    String accessToken) {

    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();

    Level atLevel = Level.fromString(level);
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

    // Notify long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.fromLevel(atLevel),
        new DeckJson(game.getOnBoardDeck(atLevel).getCardList(), atLevel)
    );

    actionUponCardReservation(game, player, card);

    return serviceUtils.checkForNextActions(game, player);
  }

  @SneakyThrows
  @Override
  public ResponseEntity<String> takeLevelTwoCard(long sessionId, String accessToken,
                                                 String chosenCard) {
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }


    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final ServerLevelCard card = game.getCardByHash(chosenCard);

    ArrayList<ServerLevelCard> levelTwoCards = new ArrayList<>();
    levelTwoCards.addAll(game.getOnBoardDeck(Level.TWO).getCardList());
    levelTwoCards.addAll(game.getOnBoardDeck(Level.REDTWO).getCardList());

    if (card == null || !levelTwoCards.contains(card)) {
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

    return serviceUtils.checkForNextActions(game, player);
  }

  @Override
  public ResponseEntity<String> takeLevelOneCard(long sessionId, String accessToken,
                                                 String chosenCard) {
    var request = serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> left = request.getLeft();
    if (!left.getStatusCode().is2xxSuccessful()) {
      return left;
    }


    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final ServerLevelCard card = game.getCardByHash(chosenCard);

    ArrayList<ServerLevelCard> levelOneCards = new ArrayList<>();
    levelOneCards.addAll(game.getOnBoardDeck(Level.ONE).getCardList());
    levelOneCards.addAll(game.getOnBoardDeck(Level.REDONE).getCardList());

    if (card == null || !levelOneCards.contains(card)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }
    var currentAction = player.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.LEVEL_ONE) {
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

    return serviceUtils.checkForNextActions(game, player);
  }

  @Override
  @SneakyThrows
  public ResponseEntity<String> acquireNoble(long sessionId, String nobleHash,
                                             String accessToken) {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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
    player.completeChooseNoble();

    game.getOnBoardNobles().removeCard(noble);
    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.NOBLES,
        new NobleDeckJson(game.getOnBoardNobles().getCardList())
    );

    ServiceUtils.acquireTradePost(game, player);
    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @Override
  @SneakyThrows
  public ResponseEntity<String> acquireCity(long sessionId, String cityHash,
                                            String accessToken) {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
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
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.CITY) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }

    var city = game.getCityByHash(cityHash);

    if (cityHash == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }

    if (!player.canBeVisitedBy(city)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.INSUFFICIENT_BONUSES_FOR_VISIT);
    }

    if (!player.addCardToInventory(city)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    player.removeTopAction();
    game.getOnBoardCities().removeCard(city);
    // Update long polling
    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.CITIES,
        new CitiesJson(game.getOnBoardCities().getCardList())
    );

    var nextAction = player.peekTopAction();
    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);
  }

  @Override
  public ResponseEntity<String> reserveNoble(long sessionId, String nobleMd5, String accessToken)
      throws JsonProcessingException {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final ServerNoble noble = game.getNobleByHash(nobleMd5);

    var currentAction = player.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.NOBLE_RESERVE) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }
    // I am assuming Remaining nobles is the field with the nobles still up for the taking.
    if (noble == null || !game.getRemainingNobles().containsValue(noble)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_CARD_HASH);
    }
    if (!player.reserveCard(noble)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    // THis doesnt do what its supposed to for some reason
    game.getOnBoardNobles().removeCard(noble);

    game.getBroadcastContentManagerMap().updateValue(
        BroadcastMapKey.NOBLES, new NobleDeckJson(
            new ArrayList<>(game.getOnBoardNobles().getCardList()))

    );


    player.removeTopAction();
    var nextAction = player.peekTopAction();

    if (nextAction != null) {
      return nextAction.getActionDetails();
    }

    serviceUtils.endCurrentPlayersTurn(game);
    return CustomResponseFactory.getResponse(CustomHttpResponses.END_OF_TURN);


  }

  @Override
  @SneakyThrows
  public ResponseEntity<String> associateBagCard(long sessionId, String accessToken,
                                                 String tokenType) {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    final Gem gem = Gem.getGem(tokenType);

    if (player.ownedGemBonuses().isEmpty()) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.NO_BONUS_TO_ASSOCIATE);
    }

    var currentAction = player.peekTopAction();
    if (currentAction == null) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.SERVER_SIDE_ERROR);
    }
    if (currentAction.getActionType() != CustomHttpResponses.ActionType.ASSOCIATE_BAG) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.ILLEGAL_ACTION);
    }

    if (gem == null || gem == Gem.GOLD || !player.ownedGemBonuses().contains(gem)) {
      return CustomResponseFactory.getResponse(CustomHttpResponses.BAD_GEM_TO_ASSOCIATE_TO_BAG);
    }
    AssociateCardAction associateBagAction = (AssociateCardAction) currentAction;
    ServerLevelCard bagCard = associateBagAction.getCard();
    bagCard.associateBagToGem(gem);
    player.updateBonusGems();

    player.removeTopAction();

    game.getHashToCardMap()
        .put(DigestUtils.md5Hex(objectMapper.writeValueAsString(bagCard)), bagCard);
    return serviceUtils.checkForNextActions(game, player);
  }

  @Override
  @SneakyThrows
  public ResponseEntity<String> getOwnedBonuses(long sessionId, String accessToken) {
    var request =
        serviceUtils.validRequestAndCurrentTurn(sessionId, accessToken);
    ResponseEntity<String> response = request.getLeft();
    if (!response.getStatusCode().is2xxSuccessful()) {
      return response;
    }
    final Game game = request.getRight().getLeft();
    final ServerPlayer player = request.getRight().getRight();
    Set<Gem> set = player.ownedGemBonuses();
    set.remove(Gem.GOLD);
    Gem[] gems = set.toArray(Gem[]::new);
    String[] bonusTypes = Arrays.stream(gems).map(Gem::getBonusType).toArray(String[]::new);
    String json = objectMapper.writeValueAsString(bonusTypes);
    return CustomResponseFactory.getCustomResponse(CustomHttpResponses.OK, json, null);
  }

  @Override
  public ResponseEntity<String> getCards(long sessionId, String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    ServerPlayer player = serviceUtils.getValidPlayerByName(sessionId, username);
    // return the cards in the inventory as a response entity
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(player.getInventory().getOwnedCards()),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<String> getNobles(long sessionId, String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    ServerPlayer player = serviceUtils.getValidPlayerByName(sessionId, username);
    // return the cards in the inventory as a response entity
    return new ResponseEntity<>(
        objectMapper.writeValueAsString(player.getInventory().getOwnedNobles()),
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> getReservedCards(long sessionId, String username,
                                                 String accessToken)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    ServerPlayer player = serviceUtils.getValidPlayerByName(sessionId, username);
    // return the reserved level cards in the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(new DeckJson(
        player.getInventory().getReservedCards(), Level.ONE)), HttpStatus.OK);
  }


  // ACTION RELATED SHENANIGANS

  @Override
  public ResponseEntity<String> getCardPrice(long sessionId, String username, Gem gem)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    ServerPlayer player = serviceUtils.getValidPlayerByName(sessionId, username);

    List<LevelCard> cards = new ArrayList<>();
    for (LevelCard levelCard : player.getInventory().getOwnedCards()) {
      if (levelCard.getGemBonus().getGemCost(gem) > 0) {
        cards.add(levelCard);
      }
    }

    // return the cards in the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(new DeckJson(
        cards, Level.ONE)), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> getReservedNobles(long sessionId, String username)
      throws JsonProcessingException {
    // get the player (if valid) from the session id and access token
    ServerPlayer player = serviceUtils.getValidPlayerByName(sessionId, username);
    // return the reserved nobles in the inventory as a response entity
    return new ResponseEntity<>(objectMapper.writeValueAsString(
        player.getInventory().getReservedNobles()), HttpStatus.OK);
  }

  private void discardCard(Game game, ServerPlayer player, ServerLevelCard card) {
    player.removeCardFromInventory(card);
    // Lose trade posts
    List<TradePost> tradePostsToRemove = new ArrayList<>();
    for (Map.Entry<RouteType, TradePost> tradePost : player.getInventory().getTradePosts()
        .entrySet()) {
      if (!tradePost.getValue().canBeTakenByPlayerWith(player.getInventory())) {
        tradePostsToRemove.add(tradePost.getValue());
      }
    }

    for (TradePost tradePost : tradePostsToRemove) {
      player.getInventory().removeTradePost(tradePost);
    }
  }

  // TODO :: Add this methode everywhere when a card is aquired, (like bought
  //  or by cascading, not upon reserving a card)
  @SneakyThrows
  private void actionUponCardAcquiral(Game game, ServerPlayer player,
                                      ServerLevelCard acquiredCard) {
    // ACTION RELATED SHENANIGANS
    if (acquiredCard.isBag()) {
      player.addAcquireCardToPerform(acquiredCard);
    }
    if (acquiredCard.getBonusType() == LevelCard.BonusType.RESERVE_NOBLE) {
      ArrayList<Noble> nobles = new ArrayList<>(game.getOnBoardNobles().getCardList());
      player.addReserveNobleToPerform(nobles);
    }
    if (acquiredCard.getBonusType() == LevelCard.BonusType.CASCADING_ONE_BAG) {
      player.addTakeOneToPerform();
    }
    if (acquiredCard.getBonusType() == LevelCard.BonusType.CASCADING_TWO) {
      player.addTakeTwoToPerform();
    }
  }

  // Because you gain one gold that you may need to discard a token given you're over
  // 10 tokens total in your inventory.
  private void actionUponCardReservation(Game game, ServerPlayer player,
                                         ServerLevelCard reservedCard) {
    if (player.hasToDiscardTokens()) {
      player.addDiscardTokenToPerform();
    }
  }

  private void extensionActionUponPurchase(Game game, ServerPlayer player) {
    // Ruby route
    if (game.getWinCondition() == WinCondition.TRADEROUTES
        && player.getInventory().getTradePosts().containsKey(RouteType.RUBY_ROUTE)) {
      player.addTakeTokenToPerform(Optional.empty());
    }
  }
}
