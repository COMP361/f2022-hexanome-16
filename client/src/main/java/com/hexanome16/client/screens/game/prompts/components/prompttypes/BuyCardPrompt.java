package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getEventBus;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A class responsible for populating Buy card prompt.
 */
public class BuyCardPrompt implements PromptTypeInterface {

  double atWidth = getAppWidth() / 2.;
  double atHeight = getAppHeight() / 2.;
  double atCardHeight = atHeight * 0.7;
  double atCardWidth = atCardHeight * 0.72;
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);
  double atBankBoxesWidth = atWidth / 5;
  double atButtonAreaWidth = atWidth / 4;
  double atButtonHeight = atHeight / 8;
  double atButtonWidth = 3 * atWidth / 14;
  double atButtonSpacing = atButtonHeight / 2;
  double atSurplusWidth = (atWidth - 2 * atBankBoxesWidth - atButtonAreaWidth - atCardWidth) / 3;

  // make true if card is reserved
  protected boolean cardIsReserved;
  // image
  protected Texture cardImage;
  // card
  protected static Entity atCardEntity;

  /**
   * An enum of the possible bank types in a card purchase.
   */
  public enum BankType {
    PLAYER_BANK,
    GAME_BANK;

    /**
     * Gets the other element of this type.
     *
     * @return The BankType object other than this.
     */
    public BankType other() {
      if (this == PLAYER_BANK) {
        return GAME_BANK;
      } else {
        return PLAYER_BANK;
      }
    }
  }

  /**
   * An enum of the possible currency types in a card purchase.
   */
  public enum CurrencyType {
    RED_TOKENS,
    GREEN_TOKENS,
    BLUE_TOKENS,
    WHITE_TOKENS,
    BLACK_TOKENS,
    GOLD_TOKENS,
    BONUS_GOLD_CARDS;
    static final Map<CurrencyType, Color> colorType = new HashMap<>();

    static {
      colorType.put(RED_TOKENS, Color.DARKRED);
      colorType.put(GREEN_TOKENS, Color.DARKGREEN);
      colorType.put(BLUE_TOKENS, Color.DARKBLUE);
      colorType.put(WHITE_TOKENS, Color.WHITE.darker());
      colorType.put(BLACK_TOKENS, Color.BLACK);
      colorType.put(GOLD_TOKENS, Color.GOLD.darker());
      colorType.put(BONUS_GOLD_CARDS, Color.GOLD.darker());
    }

    /**
     * Gets the color of the CurrencyType.
     *
     * @return the color of the implied argument.
     */
    public Paint getColor() {
      return colorType.get(this);
    }

    /**
     * Gets a color that pops over the color of the CurrencyType, for the stroke.
     *
     * @return A color that would be suitable for the stroke over a CurrencyType.s
     */
    public Paint getStrokeColor() {
      return this.getTextColor();
    }

    /**
     * Gets a color that pops over the color of the CurrencyType, for the Text.
     *
     * @return A color that would be suitable for the Text over a currencyType.
     */
    public Paint getTextColor() {
      if (this == BLACK_TOKENS) {
        return Color.WHITE;
      }
      return Color.BLACK;
    }
  }

  /**
   * An enum of the possible button types in a card purchase.
   */
  public enum ButtonType {
    RESERVE, BUY;

    /**
     * Adds behaviour to the node t, supposed to be a button.
     *
     * @param buyCardPrompt Object that allows access to instance method cardBought.
     * @param t             the Node we want to add button like behaviour to.
     */
    public void setBehaviour(BuyCardPrompt buyCardPrompt, Node t) {
      if (this == RESERVE) {
        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
          // add behaviour here
          closeBuyPrompt();
          cardReservation();
          e.consume();
        });
      } else if (this == BUY) {
        t.setOpacity(0.5);

        FXGL.getEventBus().addEventHandler(EventType.ROOT, e -> {
          if (canBuy()) {
            t.setOpacity(1);
          } else {
            t.setOpacity(0.5);
          }
        });

        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
          if (t.getOpacity() == 1) {
            closeBuyPrompt();
            buyCardPrompt.cardBought();
            e.consume();
          }
        });
      }
    }


    private static boolean canBuy() {
      // hardCoded for now
      return FXGL.getWorldProperties()
          .getInt(BankType.GAME_BANK + "/" + CurrencyType.BONUS_GOLD_CARDS)
          >= 2;
    }
  }

  @Override
  public double width() {
    return atWidth;
  }

  @Override
  public double height() {
    return atHeight;
  }

  /**
   * Alternative function to populatePrompt, used exclusively for prompts to work with Peini's part.
   *
   * @param entity Prompt entity.
   * @param cardEntity card entity.
   */
  public void populatePrompt(Entity entity, Entity cardEntity) {
    atCardEntity = cardEntity;
    populatePrompt(entity);
  }

  @Override
  public void populatePrompt(Entity entity) {

    // initiate playerBank
    VBox playerBank = new VBox();
    playerBank.setAlignment(Pos.CENTER);
    playerBank.setPrefSize(atBankBoxesWidth, atHeight / 5);
    playerBank.setSpacing((2 * atHeight / 10) / 8);

    // initiate Card to buy layout
    setCardImage();
    cardImage.setFitWidth(atCardWidth);
    cardImage.setFitHeight(atCardHeight);

    // initiate gameBank layout
    VBox gameBank = new VBox();
    gameBank.setAlignment(Pos.CENTER);
    gameBank.setPrefSize(atBankBoxesWidth, atHeight / 5);
    gameBank.setSpacing((2 * atHeight / 10) / 8);

    // initiate Reserve/Buy buttons layout
    VBox reserveBuy = new VBox();
    reserveBuy.setAlignment(Pos.CENTER);
    reserveBuy.setPrefSize(atButtonAreaWidth, atHeight / 5);
    reserveBuy.setSpacing(atButtonSpacing);

    //initializing Prompt Layout
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(atSurplusWidth);

    // initiate Player Bank elements
    for (Node n : createBank(BankType.PLAYER_BANK)) {
      playerBank.getChildren().add(n);
    }

    // initiate Game Bank elements
    for (Node n : createBank(BankType.GAME_BANK)) {
      gameBank.getChildren().add(n);
    }

    // initiate ReserveBuy
    setCardIsReserved();
    reserveBuy.getChildren().addAll(createReserveBuy(atButtonWidth, atButtonHeight));

    // adding to view
    myPrompt.getChildren().addAll(playerBank, cardImage, gameBank, reserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private Collection<StackPane> createBank(BankType banktype) {

    // all tokens stack pane
    EnumMap<CurrencyType, StackPane> tokens =
        new EnumMap<CurrencyType, StackPane>(CurrencyType.class);

    // all tokens initialize and set up will need to modify gold cards tho
    for (CurrencyType t : CurrencyType.values()) {
      StackPane myToken = new StackPane();
      Collection<Node> myList = makeTokenNode(t, banktype);
      myToken.getChildren().addAll(myList);

      tokens.put(t, myToken);

      myToken.setOnMouseClicked(e -> {
        mouseClickToken(e, t, myToken.getChildren().get(0), banktype);
      });
    }

    // bonus gold cards Fixing
    // bonus gold card Rectangle
    Rectangle bonusCard = new Rectangle((atHeight / 10) * 0.72, atHeight / 10,
        CurrencyType.BONUS_GOLD_CARDS.getColor());
    StackPane bonusTokens = tokens.get(CurrencyType.BONUS_GOLD_CARDS);
    bonusTokens.getChildren().set(0, bonusCard);
    Text bonusGoldText = ((Text) bonusTokens.getChildren().get(1));
    // Initial opacity of rectangle
    if (bonusGoldText.getText().equals("0")) {
      bonusCard.setOpacity(0.5);
    }

    // fix up Card so that it looks better
    bonusCard.setStrokeWidth(atHeight / 120);
    bonusCard.setStroke(CurrencyType.BONUS_GOLD_CARDS.getStrokeColor());

    bonusGoldText.textProperty().addListener((observable, oldValue, newValue) -> {
      handleTextChange(oldValue, newValue, bonusCard);
    });

    // return bank nodes
    return tokens.values();
  }

  protected Collection<StackPane> createReserveBuy(double buttonWidth, double buttonHeight) {

    // buttons container, will contain all the button that we create
    EnumMap<ButtonType, StackPane> buttons = new EnumMap<ButtonType, StackPane>(ButtonType.class);

    // creates a button for each button type
    for (ButtonType t : ButtonType.values()) {
      // if the card is reserved then ignore reserved button
      if (t == ButtonType.RESERVE && cardIsReserved) {
        continue;
      }
      StackPane button = new StackPane();
      buttons.put(t, button);
      button.getChildren().setAll(createButton(t, buttonWidth, buttonHeight));
      t.setBehaviour(this, button);
    }

    return buttons.values();
  }

  private Collection<Node> createButton(ButtonType buttonType,
                                        double buttonWidth, double buttonHeight) {

    // make box
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Config.SECONDARY_COLOR);
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);

    // make Text
    Text buttonText = new Text(buttonType.toString());
    buttonText.setWrappingWidth(buttonWidth);
    buttonText.setTextAlignment(TextAlignment.CENTER);
    buttonText.setFont(Font.font(buttonHeight * 0.6));

    // return them
    return new ArrayList<>(List.of(buttonBox, buttonText));
  }

  private Collection<Node> makeTokenNode(CurrencyType tokenType, BankType tokenOwner) {

    // text that appears over the circle
    Text tokensAmount = new Text();
    tokensAmount.textProperty().bind(
        FXGL.getWorldProperties()
                .intProperty(tokenOwner.toString() + "/" + tokenType)
            .asString());
    tokensAmount.setFill(tokenType.getTextColor());
    tokensAmount.setFont(Font.font(atHeight / 20));

    // initialize circle
    Circle tokensCircle = new Circle(atHeight / 20, tokenType.getColor());
    if (tokensAmount.getText().equals("0")) {
      tokensCircle.setOpacity(0.5);
    }
    tokensCircle.setStrokeWidth(atHeight / 120);
    tokensCircle.setStroke(tokenType.getStrokeColor());

    tokensAmount.textProperty().addListener((observable, oldValue, newValue) -> {
      handleTextChange(oldValue, newValue, tokensCircle);
    });

    return new ArrayList<Node>(List.of(tokensCircle, tokensAmount));
  }

  private void handleTextChange(String oldValue, String newValue, Node tokenNode) {
    if (oldValue.equals("1") && newValue.equals("0")) {
      tokenNode.setOpacity(0.5);
    }
    if (oldValue.equals("0") && newValue.equals("1")) {
      tokenNode.setOpacity(1);
    }
  }

  private void mouseClickToken(MouseEvent e, CurrencyType tokensType, Node tokenNode,
                               BankType tokenOwner) {
    if (tokenNode.getOpacity() == 1) {
      FXGL.getWorldProperties()
          .increment(tokenOwner.other().toString() + "/" + tokensType.toString(), +1);
      FXGL.getWorldProperties()
          .increment(tokenOwner + "/" + tokensType, -1);
      getEventBus().fireEvent(new Event(EventType.ROOT));
    }
    e.consume();
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(atTopLeftX);
    myPrompt.setTranslateY(atTopLeftY);
    myPrompt.setPrefWidth(atWidth);
    myPrompt.setMaxWidth(atWidth);
    myPrompt.setPrefHeight(atHeight);
    myPrompt.setMaxHeight(atHeight);
  }

  // STATIC METHODS ////////////////////////////////////////////////////////////////////////////////

  protected static void closeBuyPrompt() {
    PromptComponent.closePrompts();
    for (CurrencyType e : CurrencyType.values()) {
      int gemsinBank =
          FXGL.getWorldProperties().getInt(BankType.GAME_BANK + "/" + e.toString());
      if (gemsinBank != 0) {
        FXGL.getWorldProperties()
            .increment(BankType.PLAYER_BANK + "/" + e, gemsinBank);
        FXGL.getWorldProperties().setValue(BankType.GAME_BANK + "/" + e, 0);
      }
    }
  }


  // TO OVERRIDE/MODIFY ////////////////////////////////////////////////////////////////////////////

  /**
   * Do something if someone reserves a card.
   */
  protected static void cardReservation() {
  }

  /**
   * Need to override to have a different image.
   */
  protected void setCardImage() {
    // if we have a card entity we are inspecting
    if (atCardEntity != null) {
      cardImage = FXGL.texture(atCardEntity.getComponent(CardComponent.class).texture);
      return;
    }

    cardImage = FXGL.texture("card1.png");
  }

  /**
   * Need to override to not have a ReserveButton, set cardIsReserved to true to do so.
   */
  protected void setCardIsReserved() {
    cardIsReserved = false;
  }

  /**
   * To Override if there is behaviour after someone presses on Confirm, is done after
   * closeBuyPrompt().
   */
  protected void cardBought() {
    if (atCardEntity != null) {
      FXGL.getEventBus().fireEvent(new SplendorEvents(SplendorEvents.BOUGHT, atCardEntity));
    }
  }



}
