package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getEventBus;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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

public class BuyReservedNobleCard implements PromptTypeInterface {

  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aCardHeight = aHeight * 0.7;
  double aCardWidth = aCardHeight * 0.72;
  double topleftX = (getAppWidth() / 2) - (aWidth / 2);
  double topleftY = (getAppHeight() / 2) - (aHeight / 2);


  public BuyReservedNobleCard() {
  }


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
    double BankBoxesWidth = aWidth / 5;

    double buttonAreaWidth = aWidth / 4;
    double buttonHeight = aHeight / 8;
    double buttonWidth = 3 * aWidth / 14;
    double buttonSpacing = buttonHeight / 2;

    double SurplusWidth = (aWidth - 2 * BankBoxesWidth - buttonAreaWidth - aCardWidth) / 3;

    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(SurplusWidth);

    // initiate elements
    VBox playerBank = new VBox();
    playerBank.setAlignment(Pos.CENTER);
    playerBank.setPrefSize(BankBoxesWidth, aHeight / 5);
    playerBank.setSpacing((2 * aHeight / 10) / 8);

    Texture myCard = FXGL.texture("noblereserve.png");
    myCard.setFitWidth(aCardWidth);
    myCard.setFitHeight(aCardHeight);

    VBox GameBank = new VBox();
    GameBank.setAlignment(Pos.CENTER);
    GameBank.setPrefSize(BankBoxesWidth, aHeight / 5);
    GameBank.setSpacing((2 * aHeight / 10) / 8);

    VBox ReserveBuy = new VBox();
    ReserveBuy.setAlignment(Pos.CENTER);
    ReserveBuy.setPrefSize(buttonAreaWidth, aHeight / 5);
    ReserveBuy.setSpacing(buttonSpacing);
//    ReserveBuy.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // initiate Player Bank elements
    initiateBank(playerBank, BuyCard.BankType.PLAYER_BANK);


    // initiate Game Bank elements
    initiateBank(GameBank, BuyCard.BankType.GAME_BANK);

    // initiate ReserveBuy
    initiateReserveBuy(ReserveBuy, buttonWidth, buttonHeight);


    // adding to view
    myPrompt.getChildren().addAll(playerBank, myCard, GameBank, ReserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private void initiateReserveBuy(VBox reserveBuy, double buttonWidth, double buttonHeight) {
    StackPane Reserve = new StackPane();
    StackPane Buy = new StackPane();

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
    getEventBus().addEventHandler(CustomEvent.CLOSING, e -> {
      closeBagPrompt();
    });
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
          getInt(BuyCard.BankType.GAME_BANK + "/" +
              BuyCard.CurrencyType.BONUS_GOLD_CARDS) >=
          2) {
        buy.setOpacity(1);
      } else {
        buy.setOpacity(0.5);
      }
    });

    buy.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if (buy.getOpacity() == 1) {
        OpenBagBonusPrompt();
        e.consume();
      }
    });

    buy.getChildren().addAll(buttonBox, RESERVE);
  }

  private void initiateBank(VBox Bank, BuyCard.BankType banktype) {
    StackPane redTokens = new StackPane();
    StackPane greenTokens = new StackPane();
    StackPane blueTokens = new StackPane();
    StackPane whiteTokens = new StackPane();
    StackPane blackTokens = new StackPane();
    StackPane goldenTokens = new StackPane();
    StackPane bonusTokens = new StackPane();
//    Map<CurrencyType,Integer> MapOfInterest = InteractionTokens.get(banktype);
//    Map<CurrencyType,Integer> OtherMap = InteractionTokens.get(banktype.other());


    // redTokens
    makeTokenNode(redTokens, BuyCard.CurrencyType.RED_TOKENS, banktype);

    // greenTokens
    makeTokenNode(greenTokens, BuyCard.CurrencyType.GREEN_TOKENS, banktype);

    // blueTokens
    makeTokenNode(blueTokens, BuyCard.CurrencyType.BLUE_TOKENS, banktype);

    // whiteTokens
    makeTokenNode(whiteTokens, BuyCard.CurrencyType.WHITE_TOKENS, banktype);

    // blackTokens
    makeTokenNode(blackTokens, BuyCard.CurrencyType.BLACK_TOKENS, banktype);

    // goldTokens
    makeTokenNode(goldenTokens, BuyCard.CurrencyType.GOLD_TOKENS, banktype);


    // bonus gold cards
    Rectangle bonusCard = new Rectangle((aHeight / 10) * 0.72, aHeight / 10,
        BuyCard.CurrencyType.BONUS_GOLD_CARDS.getColor());
    Text bonusAmount = new Text();
    bonusAmount.textProperty().bind(
        FXGL.getWorldProperties().
            intProperty(
                banktype + "/" + BuyCard.CurrencyType.BONUS_GOLD_CARDS)
            .asString());

    if (bonusAmount.getText().equals("0")) {
      bonusCard.setOpacity(0.5);
    }
    bonusCard.setStrokeWidth(aHeight / 120);
    bonusCard.setStroke(BuyCard.CurrencyType.BONUS_GOLD_CARDS.getStrokeColor());

    bonusAmount.setFont(Font.font(aHeight / 20));
    bonusAmount.setFill(BuyCard.CurrencyType.BONUS_GOLD_CARDS.getTextColor());
    bonusTokens.getChildren().addAll(bonusCard, bonusAmount);
    bonusTokens.setOnMouseClicked(e -> {
      mouseClickToken(e, BuyCard.CurrencyType.BONUS_GOLD_CARDS, bonusCard, banktype);
    });
    bonusAmount.setOnMouseClicked(e -> {
      mouseClickToken(e, BuyCard.CurrencyType.BONUS_GOLD_CARDS, bonusCard, banktype);
    });
    bonusAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      handleTextChange(oldValue, newValue, bonusCard);
    });

    // adding all of it to the bank
    Bank.getChildren().addAll(redTokens, greenTokens, blueTokens,
        whiteTokens, blackTokens, goldenTokens, bonusTokens);
  }

  private void makeTokenNode(StackPane tokenStackPane, BuyCard.CurrencyType tokenType,
                             BuyCard.BankType tokenOwner) {
    Circle tokensCircle = new Circle(aHeight / 20, tokenType.getColor());

    Text tokensAmount = new Text();
    tokensAmount.textProperty().bind(
        FXGL.getWorldProperties().
            intProperty(tokenOwner.toString() + "/" + tokenType)
            .asString());
    if (tokensAmount.getText().equals("0")) {
      tokensCircle.setOpacity(0.5);
    }

    tokensAmount.setFill(tokenType.getTextColor());
    tokensCircle.setStrokeWidth(aHeight / 120);
    tokensCircle.setStroke(tokenType.getStrokeColor());
    tokensCircle.setOnMouseClicked(e -> {
      mouseClickToken(e, tokenType, tokensCircle, tokenOwner
      );
    });

    tokensAmount.setOnMouseClicked(e -> {
      mouseClickToken(e, tokenType, tokensCircle, tokenOwner
      );
    });
    tokensAmount.setFont(Font.font(aHeight / 20));

    tokensAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      handleTextChange(oldValue, newValue, tokensCircle);
    });

    tokenStackPane.getChildren().addAll(tokensCircle, tokensAmount);
  }

  private void handleTextChange(String oldValue, String newValue, Node tokenNode) {
    if (oldValue.equals("1") && newValue.equals("0")) {
      tokenNode.setOpacity(0.5);
    }
    if (oldValue.equals("0") && newValue.equals("1")) {
      tokenNode.setOpacity(1);
    }
  }

  private void mouseClickToken(MouseEvent e, BuyCard.CurrencyType tokensType, Node tokenNode,
                               BuyCard.BankType tokenOwner) {
//    int amountLeft= mapOfInterest.get(tokensType);
    if (tokenNode.getOpacity() == 1) {
      FXGL.getWorldProperties()
          .increment(tokenOwner.other().toString() + "/" + tokensType.toString(), +1);
      FXGL.getWorldProperties()
          .increment(tokenOwner + "/" + tokensType, -1);
//      mapOfInterest.put(tokensType,amountLeft-1);
//      OtherMap.put(tokensType,OtherMap.get(tokensType)+1);
      getEventBus().fireEvent(new Event(EventType.ROOT));
    }
    e.consume();
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    myPrompt.setPrefWidth(aWidth);
    myPrompt.setMaxWidth(aWidth);
    myPrompt.setPrefHeight(aHeight);
    myPrompt.setMaxHeight(aHeight);
  }

  private void OpenBagBonusPrompt() {
    FXGL.spawn("PromptBox", new SpawnData().put("promptType", PromptType.CHOOSE_NOBLE_TO_RESERVE));
    for (BuyCard.CurrencyType e : BuyCard.CurrencyType.values()) {
      int gemsinBank = FXGL.getWorldProperties()
          .getInt(BuyCard.BankType.GAME_BANK + "/" + e.toString());
      if (gemsinBank != 0) {
        FXGL.getWorldProperties()
            .increment(BuyCard.BankType.PLAYER_BANK + "/" + e, gemsinBank);
        FXGL.getWorldProperties()
            .setValue(BuyCard.BankType.GAME_BANK + "/" + e, 0);
      }
    }
  }

  private void closeBagPrompt() {
    for (BuyCard.CurrencyType e : BuyCard.CurrencyType.values()) {
      int gemsinBank = FXGL.getWorldProperties()
          .getInt(BuyCard.BankType.GAME_BANK + "/" + e.toString());
      if (gemsinBank != 0) {
        FXGL.getWorldProperties()
            .increment(BuyCard.BankType.PLAYER_BANK + "/" + e, gemsinBank);
        FXGL.getWorldProperties()
            .setValue(BuyCard.BankType.GAME_BANK + "/" + e, 0);
      }
    }
  }
}
