package com.hexanome16.screens.game.prompts.actualyUI.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.PromptTypes.BuyCardHelper.CardCost;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BuyCard implements PromptTypeInterface {

  public static enum BankType{
    PLAYER_BANK,GAME_BANK;
    public BankType other(){
      if (this == PLAYER_BANK) {return  GAME_BANK;}
      else return PLAYER_BANK;
    }
  }
  public static enum CurrencyType{
    RED_TOKENS,GREEN_TOKENS,BLUE_TOKENS,WHITE_TOKENS,BLACK_TOKENS,GOLD_TOKENS,BONUS_GOLD_CARDS;
    static Map<CurrencyType,Color> Colortype = new HashMap<>();
    static {
      Colortype.put(RED_TOKENS,Color.DARKRED);
      Colortype.put(GREEN_TOKENS,Color.DARKGREEN);
      Colortype.put(BLUE_TOKENS,Color.DARKBLUE);
      Colortype.put(WHITE_TOKENS,Color.WHITE.darker());
      Colortype.put(BLACK_TOKENS,Color.BLACK);
      Colortype.put(GOLD_TOKENS,Color.GOLD.darker());
      Colortype.put(BONUS_GOLD_CARDS,Color.GOLD.darker());
    }
    public Paint getColor() {
     return Colortype.get(this);
    }

    public Paint getStrokeColor() {
      if (this == BLACK_TOKENS){return Color.WHITE;}
      return Color.BLACK;
    }

    public Paint getTextColor() {
      if (this == BLACK_TOKENS){return Color.WHITE;}
      return Color.BLACK;
    }
  }

  double aWidth = getAppWidth()/2;
  double aHeight = getAppHeight()/2;
  double aCardHeight = aHeight*0.7;
  double aCardWidth = aCardHeight*0.72;
  double topleftX = (getAppWidth()/2)-(aWidth/2);
  double topleftY = (getAppHeight()/2) - (aHeight/2);

  CardCost aCardCost;

  public BuyCard(CardCost pCardCost){
    aCardCost = pCardCost;
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
    double boxesWidth = aWidth/5;
    double SurplusWidth = (aWidth-3*boxesWidth-aCardWidth)/3;
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(SurplusWidth);

    // initiate elements
    VBox playerBank = new VBox();
    playerBank.setAlignment(Pos.CENTER);
    playerBank.setPrefSize(boxesWidth,aHeight/5);
//    playerBank.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
    playerBank.setSpacing((2*aHeight/10)/8);

    Texture myCard = FXGL.texture("card1.png");
    myCard.setFitWidth(aCardWidth);
    myCard.setFitHeight(aCardHeight);

    VBox GameBank = new VBox();
    GameBank.setAlignment(Pos.CENTER);
    GameBank.setPrefSize(boxesWidth,aHeight/5);
//    playerBank.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
    GameBank.setSpacing((2*aHeight/10)/8);
//    GameBank.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));

    VBox ReserveBuy= new VBox();
    ReserveBuy.setPrefSize(boxesWidth,aHeight/5);
    ReserveBuy.setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // initiate Player Bank elements
    initiateBank(playerBank,BankType.PLAYER_BANK);


    // initiate Game Bank elements
    initiateBank(GameBank,BankType.GAME_BANK);

    // initiate ReserveBuy
    initiateReserveBuy(aCardCost);


    // adding to view
    myPrompt.getChildren().addAll(playerBank,myCard,GameBank,ReserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private void initiateReserveBuy(CardCost pCardCost) {

  }

  private void initiateBank(VBox Bank,BankType banktype) {
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
    makeTokenNode(redTokens, CurrencyType.RED_TOKENS, banktype);

    // greenTokens
    makeTokenNode(greenTokens,CurrencyType.GREEN_TOKENS, banktype);

    // blueTokens
    makeTokenNode(blueTokens,CurrencyType.BLUE_TOKENS, banktype);

    // whiteTokens
    makeTokenNode(whiteTokens,CurrencyType.WHITE_TOKENS,banktype);

    // blackTokens
    makeTokenNode(blackTokens,CurrencyType.BLACK_TOKENS,banktype);

    // goldTokens
    makeTokenNode(goldenTokens,CurrencyType.GOLD_TOKENS,banktype);


    // bonus gold cards
    Rectangle bonusCard = new Rectangle((aHeight/10)*0.72,aHeight/10,CurrencyType.BONUS_GOLD_CARDS.getColor());
    Text bonusAmount = new Text();
    bonusAmount.textProperty().bind(
        FXGL.getWorldProperties().
            intProperty(banktype.toString()+"/"+CurrencyType.BONUS_GOLD_CARDS.toString())
            .asString());

    if (bonusAmount.getText().equals("0")){bonusCard.setOpacity(0.5);}
    bonusCard.setStrokeWidth(aHeight/120);
    bonusCard.setStroke(CurrencyType.BONUS_GOLD_CARDS.getStrokeColor());

    bonusAmount.setFont(Font.font(aHeight/20));
    bonusAmount.setFill(CurrencyType.BONUS_GOLD_CARDS.getTextColor());
    bonusTokens.getChildren().addAll(bonusCard,bonusAmount);
    bonusTokens.setOnMouseClicked(e -> {
      mouseClickToken(e, CurrencyType.BONUS_GOLD_CARDS,bonusCard, banktype);
    });
    bonusAmount.setOnMouseClicked(e -> {
      mouseClickToken(e, CurrencyType.BONUS_GOLD_CARDS,bonusCard, banktype);
    });
    bonusAmount.textProperty().addListener((observable,oldValue,newValue) ->{
      handleTextChange(oldValue,newValue,bonusCard);
    });

    // adding all of it to the bank
    Bank.getChildren().addAll(redTokens,greenTokens,blueTokens,
        whiteTokens,blackTokens,goldenTokens,bonusTokens);
  }

  private void makeTokenNode(StackPane tokenStackPane, CurrencyType tokenType, BankType tokenOwner)
  {
    Circle tokensCircle = new Circle(aHeight/20,tokenType.getColor());

    Text tokensAmount = new Text();
    tokensAmount.textProperty().bind(
        FXGL.getWorldProperties().
            intProperty(tokenOwner.toString()+"/"+tokenType.toString())
            .asString());
    if (tokensAmount.getText().equals("0")){tokensCircle.setOpacity(0.5);}

    tokensAmount.setFill(tokenType.getTextColor());
    tokensCircle.setStrokeWidth(aHeight/120);
    tokensCircle.setStroke(tokenType.getStrokeColor());
    tokensCircle.setOnMouseClicked(e -> {
      mouseClickToken(e, tokenType, tokensCircle, tokenOwner
      );
    });

    tokensAmount.setOnMouseClicked(e -> {
      mouseClickToken(e, tokenType, tokensCircle, tokenOwner
      );
    });
    tokensAmount.setFont(Font.font(aHeight/20));

    tokensAmount.textProperty().addListener((observable,oldValue,newValue) ->{
      handleTextChange(oldValue,newValue,tokensCircle);
    });

    tokenStackPane.getChildren().addAll(tokensCircle,tokensAmount);
  }

  private void handleTextChange(String oldValue, String newValue, Node tokenNode) {
    if (oldValue.equals("1") && newValue.equals("0")) {
      tokenNode.setOpacity(0.5);
    }
    if (oldValue.equals("0") && newValue.equals("1")){
      tokenNode.setOpacity(1);
    }
  }

  private void mouseClickToken(MouseEvent e, CurrencyType tokensType, Node tokenNode,
                               BankType tokenOwner) {
//    int amountLeft= mapOfInterest.get(tokensType);
    if (tokenNode.getOpacity()==1) {
      FXGL.getWorldProperties().increment(tokenOwner.other().toString()+"/"+tokensType.toString(),+1);
      FXGL.getWorldProperties()
          .increment(tokenOwner.toString()+"/"+tokensType.toString(),-1);
//      mapOfInterest.put(tokensType,amountLeft-1);
//      OtherMap.put(tokensType,OtherMap.get(tokensType)+1);

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
}
