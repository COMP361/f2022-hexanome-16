package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.CurrencyType;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.LevelCard;
import com.hexanome16.common.models.price.Gem;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.util.ObjectMapperUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import kong.unirest.core.Headers;
import lombok.SneakyThrows;

/**
 * A class responsible for populating Buy sacrifice card prompt.
 */
public class BuyCardWithCards implements PromptTypeInterface {
  /**
   * The card entity.
   */
  protected static Entity atCardEntity;
  /**
   * The card price map.
   */
  PriceMap atCardPriceMap;
  /**
   * The Card is reserved.
   */
  protected boolean cardIsReserved = false;
  /**
   * The width.
   */
  double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  double atHeight = getAppHeight() / 2.;
  /**
   * The main card height.
   */
  double atMainCardHeight = atHeight * 0.7;
  /**
   * The main card width.
   */
  double atMainCardWidth = atMainCardHeight * 0.72;
  /**
   * The scroll card width.
   */
  double atScrollCardWidth = atMainCardHeight * 0.4;
  /**
   * The scroll card height.
   */
  double atScrollCardHeight = atScrollCardWidth * 1.4;
  /**
   * The top left x.
   */
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  /**
   * The top left y.
   */
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);
  /**
   * The Button area width.
   */
  double buttonAreaWidth = atWidth / 4;
  /**
   * The Button height.
   */
  double buttonHeight = atHeight / 8;
  /**
   * The Button spacing.
   */
  double buttonSpacing = buttonHeight / 2;
  /**
   * The Button width.
   */
  double buttonWidth = 3 * atWidth / 14;
  /**
   * The Element spacing.
   */
  double elementSpacing = (atWidth - 2 * atScrollCardWidth - buttonAreaWidth - buttonWidth) / 5;

  /**
   * The Cards chosen.
   */
  int cardsChosen = 0;

  static Map<LevelCard, String> bagCardHashList = new HashMap<LevelCard, String>();

  static Map<LevelCard, String> cardHashList = new HashMap<LevelCard, String>();

  List<String> prices = new ArrayList<>();

  @Override
  public double getWidth() {
    return atWidth;
  }

  @Override
  public double getHeight() {
    return atHeight;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }

  @Override
  public boolean canBeOpenedOutOfTurn() {
    return false;
  }

  /**
   * Alternative function to populatePrompt, used exclusively for prompts to work with Peini's part.
   *
   * @param entity     Prompt entity.
   * @param cardEntity card entity.
   */
  public void populatePrompt(Entity entity, Entity cardEntity) {
    atCardEntity = cardEntity;
    atCardPriceMap = cardEntity.getComponent(CardComponent.class).getPriceMap();
    cardIsReserved = !cardEntity.getComponent(CardComponent.class).getOnBoard();
    populatePrompt(entity);
  }

  @Override
  public void populatePrompt(Entity entity) {

    FXGL.getEventBus().addEventHandler(SplendorEvents.CLOSING, e -> cardsChosen = 0);

    //initializing Hbox
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(elementSpacing);


    // initiate elements
    Texture myCard = FXGL.texture(atCardEntity.getComponent(CardComponent.class).texture);
    myCard.setFitWidth(atMainCardWidth);
    myCard.setFitHeight(atMainCardHeight);

    Text bagCardsText = new Text("BAG CARDS");
    bagCardsText.setFont(GAME_FONT.newFont(atHeight * 0.05));
    bagCardsText.setFill(Config.SECONDARY_COLOR);
    bagCardsText.setWrappingWidth(atScrollCardWidth);
    bagCardsText.setTextAlignment(TextAlignment.CENTER);

    ScrollPane bagCardsScroll = new ScrollPane();
    bagCardsScroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    bagCardsScroll.setPrefViewportWidth(atScrollCardWidth);
    bagCardsScroll.setPannable(true);
    bagCardsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    bagCardsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    BorderPane bagBorderPane = new BorderPane();
    bagBorderPane.setTop(bagCardsText);
    bagBorderPane.setCenter(bagCardsScroll);

    Rectangle disablingRectangleOthers = new Rectangle(atScrollCardWidth, atHeight, Color.GREY);
    disablingRectangleOthers.setOpacity(0.75);

    Text otherCards = new Text("OTHERS");
    otherCards.setFont(GAME_FONT.newFont(atHeight * 0.05));
    otherCards.setFill(Config.SECONDARY_COLOR);
    otherCards.setWrappingWidth(atScrollCardWidth);
    otherCards.setTextAlignment(TextAlignment.CENTER);

    ScrollPane otherCardsScroll = new ScrollPane();
    otherCardsScroll.setBackground(
        new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    otherCardsScroll.setPrefViewportWidth(atScrollCardWidth);
    otherCardsScroll.setPrefViewportHeight(atHeight);
    otherCardsScroll.setPannable(true);
    otherCardsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    otherCardsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    BorderPane otherBorderPane = new BorderPane();
    otherBorderPane.setTop(otherCards);
    otherBorderPane.setCenter(otherCardsScroll);

    StackPane otherWhole = new StackPane();
    if (bagCardHashList.size() > 0) {
      otherWhole.getChildren().addAll(otherBorderPane, disablingRectangleOthers);
    } else {
      otherWhole.getChildren().addAll(otherBorderPane);
    }


    // Fix BagCards
    VBox myBags = new VBox();
    myBags.setAlignment(Pos.TOP_CENTER);
    bagCardsScroll.setContent(myBags);


    VBox myOthers = new VBox();
    myOthers.setAlignment(Pos.TOP_CENTER);
    otherCardsScroll.setContent(myOthers);
    StackPane buy = new StackPane();

    for (Map.Entry<LevelCard, String> card :
        cardHashList.entrySet()) {
      addOtherCard(myOthers, card.getKey(), buy);
    }

    for (Map.Entry<LevelCard, String> card :
        bagCardHashList.entrySet()) {
      addBagCard(myBags, card.getKey(), otherWhole, disablingRectangleOthers);
    }

    // initiate ReserveBuy
    VBox reserveBuy = new VBox();
    reserveBuy.setAlignment(Pos.CENTER);
    reserveBuy.setPrefSize(buttonAreaWidth, atHeight / 5);
    reserveBuy.setSpacing(buttonSpacing);
    initiateReserveBuy(reserveBuy, buttonWidth, buttonHeight, buy);


    // adding to view
    myPrompt.getChildren().addAll(bagBorderPane, otherWhole, myCard, reserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private void addOtherCard(VBox otherCards, LevelCard levelCard, Node buy) {
    Text textPrestigeAmount = new Text();
    textPrestigeAmount.setTextAlignment(TextAlignment.CENTER);
    textPrestigeAmount.setFont(GAME_FONT.newFont(atScrollCardHeight * 0.75));
    textPrestigeAmount.setFill(Config.SECONDARY_COLOR);
    textPrestigeAmount.setWrappingWidth(atScrollCardWidth);
    Text x = new Text("X");
    x.setTextAlignment(TextAlignment.CENTER);
    x.setFont(GAME_FONT.newFont(atScrollCardHeight * 0.75));
    x.setFill(Config.SECONDARY_COLOR);
    x.setFill(Color.DARKRED);
    x.setOpacity(0);

    StackPane myWholeCard = new StackPane();
    myWholeCard.setOnMouseClicked(e -> {
      if (x.getOpacity() == 1) {
        x.setOpacity(0);
        cardsChosen--;
        prices.remove(cardHashList.get(levelCard));
      } else {
        cardsChosen++;
        x.setOpacity(1);
        prices.add(cardHashList.get(levelCard));
      }
      if (cardsChosen == 2) {
        buy.setOpacity(1);
      } else {
        buy.setOpacity(0.5);
      }
    });

    Texture texture = FXGL.texture(levelCard.getCardInfo().texturePath() + ".png");
    texture.setFitWidth(atScrollCardWidth);
    texture.setFitHeight(atScrollCardHeight);
    myWholeCard.getChildren().addAll(texture, x);
    otherCards.getChildren().add(myWholeCard);

  }

  private void addBagCard(VBox bagCards, LevelCard levelCard, StackPane otherWhole,
                          Rectangle disablingRectangleOthers) {
    Text x = new Text("X");
    x.setTextAlignment(TextAlignment.CENTER);
    x.setFont(GAME_FONT.newFont(atScrollCardHeight * 0.75));
    x.setFill(Color.DARKRED);
    x.setOpacity(0);

    StackPane myWholeCard = new StackPane();
    myWholeCard.setOnMouseClicked(e -> {
      if (x.getOpacity() == 1) {
        x.setOpacity(0);
        cardsChosen--;
        prices.remove(bagCardHashList.get(levelCard));
        if (cardsChosen < bagCardHashList.size()) {
          otherWhole.getChildren().add(disablingRectangleOthers);
        }
      } else {
        x.setOpacity(1);
        cardsChosen++;
        prices.add(bagCardHashList.get(levelCard));
        if (cardsChosen >= bagCardHashList.size() && 2 >= bagCardHashList.size()) {
          otherWhole.getChildren().remove(disablingRectangleOthers);
        }
      }
    });

    Texture texture = FXGL.texture(levelCard.getCardInfo().texturePath() + ".png");
    texture.setFitWidth(atScrollCardWidth);
    texture.setFitHeight(atScrollCardHeight);
    myWholeCard.getChildren().addAll(texture, x);

    bagCards.getChildren().add(myWholeCard);

  }

  private void initiateReserveBuy(VBox reserveBuy, double buttonWidth, double buttonHeight,
                                  StackPane buy) {
    StackPane reserve = new StackPane();
    createReserveButton(reserve, buttonWidth, buttonHeight);
    createBuyButton(buy, buttonWidth, buttonHeight);

    reserveBuy.getChildren().addAll(reserve, buy);
  }

  private void createReserveButton(StackPane reserve, double buttonWidth, double buttonHeight) {
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Config.SECONDARY_COLOR);
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);
    Text reserveText = new Text("RESERVE");
    reserveText.setWrappingWidth(buttonWidth);
    reserveText.setTextAlignment(TextAlignment.CENTER);
    reserveText.setFont(GAME_FONT.newFont(buttonHeight * 0.6));
    reserveText.setFill(Config.PRIMARY_COLOR);

    reserve.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      cardReservation();
      closeBagPrompt();
      e.consume();
    });

    reserve.getChildren().addAll(buttonBox, reserveText);
  }

  private void createBuyButton(StackPane buy, double buttonWidth, double buttonHeight) {
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Config.SECONDARY_COLOR);
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);
    Text reserve = new Text("BUY");
    reserve.setWrappingWidth(buttonWidth);
    reserve.setTextAlignment(TextAlignment.CENTER);
    reserve.setFont(GAME_FONT.newFont(buttonHeight * 0.6));
    reserve.setFill(Config.PRIMARY_COLOR);
    buy.setOpacity(0.5);

    FXGL.getEventBus().addEventHandler(EventType.ROOT, e -> {
      if (FXGL.getWorldProperties()
          .getInt(BuyCardPrompt.BankType.GAME_BANK
              + "/" + CurrencyType.BONUS_GOLD_CARDS)
          >= 2) {
        buy.setOpacity(1);
      } else {
        buy.setOpacity(0.5);
      }
    });

    buy.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if (buy.getOpacity() == 1) {
        notifyServer();
        closeBagPrompt();
        e.consume();
      }
    });

    buy.getChildren().addAll(buttonBox, reserve);
  }

  /**
   * Fetch cards from player's inventory.
   *
   * @param player the current player.
   * @param gem    the gem type.
   */
  @SneakyThrows
  public static void fetchCards(String player, Gem gem) {
    cardHashList.clear();
    bagCardHashList.clear();
    long sessionId = GameScreen.getSessionId();
    Map<String, LevelCard> responseHashList =
        PromptsRequests.getCardPrice(sessionId, player, gem).getCards();
    System.out.println("init: " + responseHashList.size());
    for (Map.Entry<String, LevelCard> card : responseHashList.entrySet()) {
      System.out.println(card.getValue());
      System.out.println(ObjectMapperUtils.getObjectMapper().writeValueAsString(card.getValue()));
      if (card.getValue().isBag()) {
        bagCardHashList.put(card.getValue(), card.getKey());
      } else {
        cardHashList.put(card.getValue(), card.getKey());
      }
    }
  }

  private void notifyServer() {
    long promptSessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    String firstHash = prices.get(0);
    String secondHash = prices.get(1);
    System.out.println(firstHash);
    System.out.println(secondHash);
    // sends a request to server telling it purchase information
    Pair<Headers, String> serverResponse = PromptsRequests.buySacrificeCard(promptSessionId,
        atCardEntity.getComponent(CardComponent.class).getCardHash(),
        authToken,
        firstHash, secondHash);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  /**
   * Do something if someone reserves a card.
   */
  protected void cardReservation() {
    long promptSessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    // send request to server
    Pair<Headers, String> serverResponse = PromptsRequests.reserveCard(promptSessionId,
        atCardEntity.getComponent(CardComponent.class).getCardHash(),
        authToken);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(atTopLeftX);
    myPrompt.setTranslateY(atTopLeftY);
    myPrompt.setPrefWidth(atWidth);
    myPrompt.setMaxWidth(atWidth);
    myPrompt.setPrefHeight(atHeight);
    myPrompt.setMaxHeight(atHeight);
  }

  private void closeBagPrompt() {
    cardsChosen = 0;
    PromptComponent.closePrompts();
  }

}
