package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getEventBus;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.CurrencyType;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.UpdateGameInfo;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.price.OrientPurchaseMap;
import com.hexanome16.common.models.price.PriceMap;
import com.hexanome16.common.models.price.PurchaseMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * A class responsible for populating Buy card prompt.
 */
public class BuyCardPrompt implements PromptTypeInterface {

  /**
   * The Card is reserved.
   */
  protected boolean cardIsReserved = false;
  /**
   * The Card image.
   */
  protected Texture cardImage;
  /**
   * The card entity.
   */
  protected Entity atCardEntity;
  /**
   * The width.
   */
  double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  double atHeight = getAppHeight() / 2.;
  /**
   * The card height.
   */
  double atCardHeight = atHeight * 0.7;
  /**
   * The card width.
   */
  double atCardWidth = atCardHeight * 0.72;
  /**
   * The top left x.
   */
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  /**
   * The top left y.
   */
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);
  /**
   * The bank boxes width.
   */
  double atBankBoxesWidth = atWidth / 5;
  /**
   * The button area width.
   */
  double atButtonAreaWidth = atWidth / 4;
  /**
   * The surplus width.
   */
  double atSurplusWidth = (atWidth - 2 * atBankBoxesWidth - atButtonAreaWidth - atCardWidth) / 3;
  /**
   * The button height.
   */
  double atButtonHeight = atHeight / 8;
  /**
   * The button spacing.
   */
  double atButtonSpacing = atButtonHeight / 2;
  /**
   * The button width.
   */
  double atButtonWidth = 3 * atWidth / 14;
  /**
   * The card price map.
   */
  PriceMap atCardPriceMap;
  /**
   * The proposed offer.
   */
  OrientPurchaseMap atProposedOffer;

  private static OrientPurchaseMap getOrientPurchaseMapOfCurrentInput() {
    int rubyAmount = 0;
    int emeraldAmount = 0;
    int sapphireAmount = 0;
    int diamondAmount = 0;
    int onyxAmount = 0;
    int goldAmount = 0;
    int goldCardAmount = 0;
    int amountInBank;
    // go through every currently supported currency and modify the value of the
    // variable associated to it
    for (CurrencyType e : CurrencyType.values()) {
      amountInBank = FXGL.getWorldProperties()
          .getInt(BankType.GAME_BANK + "/" + e);
      switch (e) {
        case RED_TOKENS -> rubyAmount = amountInBank;
        case GREEN_TOKENS -> emeraldAmount = amountInBank;
        case BLUE_TOKENS -> sapphireAmount = amountInBank;
        case WHITE_TOKENS -> diamondAmount = amountInBank;
        case BLACK_TOKENS -> onyxAmount = amountInBank;
        case GOLD_TOKENS -> goldAmount = amountInBank;
        case BONUS_GOLD_CARDS -> goldCardAmount = amountInBank;
        default -> { /* do nothing */ }
      }
    }
    // Creates a Purchase map of the current amounts in the Transaction bank
    return new OrientPurchaseMap(rubyAmount, emeraldAmount,
        sapphireAmount, diamondAmount, onyxAmount, goldAmount, goldCardAmount);
  }

  // -------------------------------------------------------------------------------------------- //

  /**
   * Close buy prompt.
   */
  protected static void closeBuyPrompt() {
    PromptComponent.closePrompts();

    for (CurrencyType e : CurrencyType.values()) {
      int gemsInBank =
          FXGL.getWorldProperties().getInt(BankType.GAME_BANK + "/" + e.toString());
      if (gemsInBank != 0) {
        FXGL.getWorldProperties()
            .increment(BankType.PLAYER_BANK + "/" + e, gemsInBank);
        FXGL.getWorldProperties().setValue(BankType.GAME_BANK + "/" + e, 0);
      }
    }
  }

  /**
   * Do something if someone reserves a card.
   */
  protected void cardReservation() {
    long promptSessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    // send request to server
    PromptsRequests.reserveCard(promptSessionId,
        atCardEntity.getComponent(CardComponent.class).getCardHash(),
        authToken);
  }

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

    String playerName = AuthUtils.getPlayer().getName();
    UpdateGameInfo.fetchPlayerBank(GameScreen.getSessionId(), playerName, true);

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

    reserveBuy.getChildren().addAll(createReserveBuy(atButtonWidth, atButtonHeight));

    // adding to view
    myPrompt.getChildren().addAll(playerBank, cardImage, gameBank, reserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  private Collection<StackPane> createBank(BankType banktype) {

    // all tokens stack pane
    EnumMap<CurrencyType, StackPane> tokens = new EnumMap<>(CurrencyType.class);

    // all tokens initialize and set up will need to modify gold cards tho
    for (CurrencyType t : CurrencyType.values()) {
      StackPane myToken = new StackPane();
      Collection<Node> myList = makeTokenNode(t, banktype);
      myToken.getChildren().addAll(myList);

      tokens.put(t, myToken);

      myToken.setOnMouseClicked(e -> mouseClickToken(e, t, myToken.getChildren().get(0), banktype));
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

    bonusGoldText.textProperty().addListener((observable, oldValue, newValue) ->
        handleTextChange(oldValue, newValue, bonusCard));

    // return bank nodes
    return tokens.values();
  }

  /**
   * Create reserve buy collection.
   *
   * @param buttonWidth  the button width
   * @param buttonHeight the button height
   * @return the collection
   */
  protected Collection<StackPane> createReserveBuy(double buttonWidth, double buttonHeight) {

    // buttons container, will contain all the button that we create
    EnumMap<ButtonType, StackPane> buttons = new EnumMap<>(ButtonType.class);

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
    buttonText.setFont(GAME_FONT.newFont(buttonHeight * 0.6));
    buttonText.setFill(Config.PRIMARY_COLOR);

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
    tokensAmount.setFont(GAME_FONT.newFont(atHeight / 20));

    // initialize circle
    Circle tokensCircle = new Circle(atHeight / 20, tokenType.getColor());
    if (tokensAmount.getText().equals("0")) {
      tokensCircle.setOpacity(0.5);
    }
    tokensCircle.setStrokeWidth(atHeight / 120);
    tokensCircle.setStroke(tokenType.getStrokeColor());

    tokensAmount.textProperty().addListener((observable, oldValue, newValue) ->
        handleTextChange(oldValue, newValue, tokensCircle)
    );

    return new ArrayList<>(List.of(tokensCircle, tokensAmount));
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

  private void notifyServer() {
    long promptSessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    // sends a request to server telling it purchase information
    Pair<Headers, String> serverResponse = PromptsRequests.buyCard(promptSessionId,
        atCardEntity.getComponent(CardComponent.class).getCardHash(),
        authToken,
        atProposedOffer);
    PromptUtils.actionResponseSpawner(serverResponse);
  }


  // STATIC METHODS ////////////////////////////////////////////////////////////////////////////////

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
   * To Override if there is behaviour after someone presses on Confirm, is done after
   * closeBuyPrompt().
   */
  protected void cardBought() {
    if (atCardEntity != null) {
      FXGL.getEventBus().fireEvent(new SplendorEvents(SplendorEvents.BOUGHT, atCardEntity));
    }
  }

  /**
   * An enum of the possible bank types in a card purchase.
   */
  public enum BankType {
    /**
     * Player bank type.
     */
    PLAYER_BANK,
    /**
     * Game bank type.
     */
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
   * An enum of the possible button types in a card purchase.
   */
  public enum ButtonType {
    /**
     * Reserve button type.
     */
    RESERVE,
    /**
     * Buy button type.
     */
    BUY;

    // allows to check if card can be bought with current amount of tokens in trade bank
    private static boolean canBuy(BuyCardPrompt buyCardPrompt) {
      OrientPurchaseMap amountInBankMap = getOrientPurchaseMapOfCurrentInput();
      // Creates a purchase map of what the price map of the current card
      PurchaseMap priceToMeet = PurchaseMap.toPurchaseMap(buyCardPrompt.atCardPriceMap);
      // check if we can buy card with the gems we put down.
      if (GameScreen.hasSapphireRoute()) {
        return amountInBankMap.canBeUsedToBuyAlt(priceToMeet);
      } else {
        return amountInBankMap.canBeUsedToBuy(priceToMeet);
      }
    }

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
          buyCardPrompt.cardReservation();
          buyCardPrompt.cardBought();
          e.consume();
        });
      } else if (this == BUY) {
        t.setOpacity(0.5);

        FXGL.getEventBus().addEventHandler(EventType.ROOT, e -> {
          if (canBuy(buyCardPrompt)) {
            t.setOpacity(1);
          } else {
            t.setOpacity(0.5);
          }
        });

        t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
          if (t.getOpacity() == 1) {
            buyCardPrompt.atProposedOffer = getOrientPurchaseMapOfCurrentInput();
            closeBuyPrompt();
            buyCardPrompt.cardBought();
            buyCardPrompt.notifyServer();
            e.consume();
          }
        });
      }
    }
  }


}
