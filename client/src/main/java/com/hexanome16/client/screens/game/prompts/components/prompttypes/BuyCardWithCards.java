package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.CurrencyType;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A class responsible for populating Buy sacrifice card prompt.
 */
public class BuyCardWithCards implements PromptTypeInterface {

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


  @Override
  public double getWidth() {
    return atWidth;
  }

  @Override
  public double getHeight() {
    return atHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {

    FXGL.getEventBus().addEventHandler(SplendorEvents.CLOSING, e -> {
      cardsChosen = 0;
    });

    //initializing Hbox
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(elementSpacing);


    // initiate elements
    Texture myCard = FXGL.texture("sacrificecard.png");
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
    otherWhole.getChildren().addAll(otherBorderPane, disablingRectangleOthers);


    // Fix BagCards
    VBox myBags = new VBox();
    myBags.setAlignment(Pos.TOP_CENTER);
    bagCardsScroll.setContent(myBags);
    addBagCard(myBags, otherWhole, disablingRectangleOthers);


    VBox myOthers = new VBox();
    myOthers.setAlignment(Pos.TOP_CENTER);
    otherCardsScroll.setContent(myOthers);
    StackPane buy = new StackPane();
    addOtherCard(myOthers, 2, buy);
    addOtherCard(myOthers, 2, buy);
    addOtherCard(myOthers, 0, buy);


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

  private void addOtherCard(VBox otherCards, int prestigeAmount, Node buy) {
    Text textPrestigeAmount = new Text();
    if (prestigeAmount > 0) {
      textPrestigeAmount = new Text(Integer.toString(prestigeAmount));
    }
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
      } else {
        cardsChosen++;
        x.setOpacity(1);
      }
      if (cardsChosen == 2) {
        buy.setOpacity(1);
      } else {
        buy.setOpacity(0.5);
      }
    });

    Rectangle bagCard = new Rectangle(atScrollCardWidth, atScrollCardHeight, Color.GREEN.darker());
    myWholeCard.getChildren().addAll(bagCard, textPrestigeAmount, x);
    otherCards.getChildren().add(myWholeCard);

  }

  private void addBagCard(VBox bagCards, StackPane otherWhole,
                          Rectangle disablingRectangleOthers) {
    Text x = new Text("X");
    x.setTextAlignment(TextAlignment.CENTER);
    x.setFont(GAME_FONT.newFont(atScrollCardHeight * 0.75));
    x.setFill(Color.DARKRED);
    x.setOpacity(0);

    StackPane myWholeCard = new StackPane();
    myWholeCard.setOnMouseClicked(e -> {
      x.setOpacity(1);
      cardsChosen++;
      otherWhole.getChildren().remove(disablingRectangleOthers);
    });
    Rectangle bagCard = new Rectangle(atScrollCardWidth, atScrollCardHeight, Color.GREEN.darker());
    Circle bagIcon = new Circle(atScrollCardWidth / 4, Color.SADDLEBROWN.brighter());
    myWholeCard.getChildren().addAll(bagCard, bagIcon, x);

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
        closeBagPrompt();
        e.consume();
      }
    });

    buy.getChildren().addAll(buttonBox, reserve);
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
