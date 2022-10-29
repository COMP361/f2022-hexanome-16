package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getEventBus;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import javafx.event.EventType;
import javafx.geometry.Pos;
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

public class BuyCardByCard implements PromptTypeInterface {

  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aMainCardHeight = aHeight * 0.7;
  double aMainCardWidth = aMainCardHeight * 0.72;
  double aScrollCardWidth = aMainCardHeight * 0.4;
  double aScrollCardHeight = aScrollCardWidth * 1.4;
  double topleftX = (getAppWidth() / 2) - (aWidth / 2);
  double topleftY = (getAppHeight() / 2) - (aHeight / 2);
  Rectangle DisablingRectangleOthers = new Rectangle(aScrollCardWidth,aHeight,Color.GREY);
  StackPane OtherWhole = new StackPane();
  StackPane Buy = new StackPane();
  int CardsChosen = 0;




  @Override
  public double width() {
    return aWidth;
  }

  @Override
  public double height() {
    return aHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {
    //initializing Hbox

    double buttonAreaWidth = aWidth / 4;
    double buttonHeight = aHeight / 8;
    double buttonWidth = 3 * aWidth / 14;
    double buttonSpacing = buttonHeight / 2;
    double elementSpacing = (aWidth - 2*aScrollCardWidth- buttonAreaWidth-buttonWidth)/5;

    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(elementSpacing);



    // initiate elements
    Texture myCard = FXGL.texture("sacrificecard.png");
    myCard.setFitWidth(aMainCardWidth);
    myCard.setFitHeight(aMainCardHeight);

    BorderPane BagBorderPane = new BorderPane();
    Text BAGCARDS = new Text("BAG CARDS");
    BAGCARDS.setFont(Font.font(aHeight*0.05));
    BAGCARDS.setWrappingWidth(aScrollCardWidth);
    BAGCARDS.setTextAlignment(TextAlignment.CENTER);
    ScrollPane BagCardsScroll = new ScrollPane();
    BagCardsScroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
    BagCardsScroll.setPrefViewportWidth(aScrollCardWidth);
    BagCardsScroll.setPannable(true);
    BagCardsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    BagCardsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    BagBorderPane.setTop(BAGCARDS);
    BagBorderPane.setCenter(BagCardsScroll);


    DisablingRectangleOthers.setOpacity(0.75);
    BorderPane OtherBorderPane = new BorderPane();
    Text OTHERCARDS = new Text("OTHERS");
    OTHERCARDS.setFont(Font.font(aHeight*0.05));
    OTHERCARDS.setWrappingWidth(aScrollCardWidth);
    OTHERCARDS.setTextAlignment(TextAlignment.CENTER);
    ScrollPane OtherCardsScroll = new ScrollPane();
    OtherCardsScroll.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
    OtherCardsScroll.setPrefViewportWidth(aScrollCardWidth);
    OtherCardsScroll.setPrefViewportHeight(aHeight);
    OtherCardsScroll.setPannable(true);
    OtherCardsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    OtherCardsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    OtherBorderPane.setTop(OTHERCARDS);
    OtherBorderPane.setCenter(OtherCardsScroll);
    OtherWhole.getChildren().addAll(OtherBorderPane,DisablingRectangleOthers);


    // Fix BagCards
      VBox myBags = new VBox();
      myBags.setAlignment(Pos.TOP_CENTER);
      BagCardsScroll.setContent(myBags);
      addBagCard(myBags);



      VBox myOthers = new VBox();
      myOthers.setAlignment(Pos.TOP_CENTER);
      OtherCardsScroll.setContent(myOthers);
      addOtherCard(myOthers, 2);
      addOtherCard(myOthers,2);
      addOtherCard(myOthers,0);


    // initiate ReserveBuy
    VBox ReserveBuy = new VBox();
    ReserveBuy.setAlignment(Pos.CENTER);
    ReserveBuy.setPrefSize(buttonAreaWidth, aHeight / 5);
    ReserveBuy.setSpacing(buttonSpacing);
    initiateReserveBuy(ReserveBuy, buttonWidth, buttonHeight);


    // adding to view
    myPrompt.getChildren().addAll(BagBorderPane,OtherWhole,myCard, ReserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private void addOtherCard(VBox OtherCards, int PrestigeAmount) {
    StackPane myWholeCard = new StackPane();
    Rectangle BagCard = new Rectangle(aScrollCardWidth,aScrollCardHeight,Color.GREEN.darker());
    Text TextPrestigeAmount = new Text();
    if (PrestigeAmount >0){
      TextPrestigeAmount = new Text(Integer.toString(PrestigeAmount));
    }
    TextPrestigeAmount.setTextAlignment(TextAlignment.CENTER);
    TextPrestigeAmount.setFont(Font.font(aScrollCardHeight*0.75));
    TextPrestigeAmount.setWrappingWidth(aScrollCardWidth);
    Text X = new Text("X");
    X.setTextAlignment(TextAlignment.CENTER);
    X.setFont(Font.font(aScrollCardHeight*0.75));
    X.setFill(Color.DARKRED);
    X.setOpacity(0);

    myWholeCard.setOnMouseClicked(e -> {
      if (X.getOpacity() == 1) {
        X.setOpacity(0);
        CardsChosen--;
      }
      else {
        CardsChosen++;
        X.setOpacity(1);
      }
      if (CardsChosen == 2) Buy.setOpacity(1);
      else Buy.setOpacity(0.5);
    });

    myWholeCard.getChildren().addAll(BagCard,TextPrestigeAmount,X);
    OtherCards.getChildren().add(myWholeCard);

  }

  private void addBagCard(VBox BagCards) {
    StackPane myWholeCard = new StackPane();
    Rectangle BagCard = new Rectangle(aScrollCardWidth,aScrollCardHeight,Color.GREEN.darker());
    Circle BagIcon = new Circle(aScrollCardWidth/4,Color.SADDLEBROWN.brighter());
    Text X = new Text("X");
    X.setTextAlignment(TextAlignment.CENTER);
    X.setFont(Font.font(aScrollCardHeight*0.75));
    X.setFill(Color.DARKRED);
    X.setOpacity(0);

    myWholeCard.setOnMouseClicked(e -> {
      X.setOpacity(1);
      OtherWhole.getChildren().remove(DisablingRectangleOthers);
    });

    myWholeCard.getChildren().addAll(BagCard,BagIcon,X);
    BagCards.getChildren().add(myWholeCard);

  }

  private void initiateReserveBuy(VBox reserveBuy, double buttonWidth, double buttonHeight) {
    StackPane Reserve = new StackPane();
    createReserveButton(Reserve, buttonWidth, buttonHeight);
    createBuyButton(Buy, buttonWidth, buttonHeight);

    reserveBuy.getChildren().addAll(Reserve, Buy);
  }

  private void createReserveButton(StackPane reserve, double buttonWidth, double buttonHeight) {
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Color.rgb(249, 161, 89));
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);
    Text RESERVE = new Text("RESERVE");
    RESERVE.setWrappingWidth(buttonWidth);
    RESERVE.setTextAlignment(TextAlignment.CENTER);
    RESERVE.setFont(Font.font(buttonHeight * 0.6));
    getEventBus().addEventHandler(CustomEvent.CLOSING,e -> {closeBagPrompt();});
    reserve.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

      PromptComponent.closePrompts();
      e.consume();
    });

    reserve.getChildren().addAll(buttonBox, RESERVE);
  }

  private void createBuyButton(StackPane buy, double buttonWidth, double buttonHeight) {
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Color.rgb(249, 161, 89));
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);
    Text RESERVE = new Text("BUY");
    RESERVE.setWrappingWidth(buttonWidth);
    RESERVE.setTextAlignment(TextAlignment.CENTER);
    RESERVE.setFont(Font.font(buttonHeight * 0.6));
    buy.setOpacity(0.5);

    FXGL.getEventBus().addEventHandler(EventType.ROOT, e -> {
      if (FXGL.getWorldProperties().
          getInt(BuyCard.BankType.GAME_BANK.toString() + "/" + BuyCard.CurrencyType.BONUS_GOLD_CARDS.toString()) >=
          2) {
        buy.setOpacity(1);
      } else buy.setOpacity(0.5);
    });

    buy.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if (buy.getOpacity() == 1) {
        closeBagPrompt();
        e.consume();
      }
    });

    buy.getChildren().addAll(buttonBox, RESERVE);
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    myPrompt.setPrefWidth(aWidth);
    myPrompt.setMaxWidth(aWidth);
    myPrompt.setPrefHeight(aHeight);
    myPrompt.setMaxHeight(aHeight);
  }

  private void closeBagPrompt() {
    CardsChosen = 0;
    PromptComponent.closePrompts();
  }
}
