package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.UpdateGameInfo;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.Level;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A class responsible for populating Reserve card prompt.
 */
public class ReserveCardPrompt implements PromptTypeInterface {
  /**
   * The Card is reserved.
   */
  protected boolean cardIsReserved;
  /**
   * The Card image.
   */
  protected Texture cardImage;
  /**
   * The card entity.
   */
  protected Level level;
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
   * Close buy prompt.
   */
  protected static void closeBuyPrompt() {
    PromptComponent.closePrompts();
  }

  /**
   * Do something if someone reserves a card.
   */
  protected void cardReservation() {
    long promptSessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    // send request to server
    PromptsRequests.reserveCard(promptSessionId, level, authToken);
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

  /**
   * Alternative function to populatePrompt, used exclusively for prompts to work with deck reserve.
   *
   * @param entity Prompt entity.
   * @param level  Level of this card.
   */
  public void populatePrompt(Entity entity, Level level) {
    this.level = level;
    populatePrompt(entity);
  }

  @Override
  public void populatePrompt(Entity entity) {

    String playerName = AuthUtils.getPlayer().getName();
    UpdateGameInfo.fetchPlayerBank(GameScreen.getSessionId(), playerName, true);

    // initiate Card to buy layout
    setCardImage();
    cardImage.setFitWidth(atCardWidth);
    cardImage.setFitHeight(atCardHeight);

    // initiate Reserve buttons layout
    VBox reserveBuy = new VBox();
    reserveBuy.setAlignment(Pos.CENTER);
    reserveBuy.setPrefSize(atButtonAreaWidth, atHeight / 5);
    reserveBuy.setSpacing(atButtonSpacing);

    //initializing Prompt Layout
    HBox myPrompt = new HBox();
    initiatePane(myPrompt);
    myPrompt.setAlignment(Pos.CENTER);
    myPrompt.setSpacing(atSurplusWidth);

    // initiate ReserveBuy
    setCardIsReserved();
    reserveBuy.getChildren().addAll(createReserveBuy(atButtonWidth, atButtonHeight));

    // adding to view
    myPrompt.getChildren().addAll(cardImage, reserveBuy);
    entity.getViewComponent().addChild(myPrompt);
  }

  /**
   * Create reserve buy collection.
   *
   * @param buttonWidth  the button width
   * @param buttonHeight the button height
   * @return the collection
   */
  protected StackPane createReserveBuy(double buttonWidth, double buttonHeight) {
    StackPane button = new StackPane();
    button.getChildren().setAll(createButton(buttonWidth, buttonHeight));
    setBehaviour(this, button);

    return button;
  }

  /**
   * Create a button.
   *
   * @param buttonWidth  the button width
   * @param buttonHeight the button height
   * @return the collection
   */
  private Collection<Node> createButton(double buttonWidth, double buttonHeight) {

    // make box
    Rectangle buttonBox = new Rectangle(buttonWidth, buttonHeight, Config.SECONDARY_COLOR);
    buttonBox.setStrokeWidth(buttonHeight / 20);
    buttonBox.setStroke(Color.BLACK);

    // make Text
    Text buttonText = new Text("Reserve");
    buttonText.setWrappingWidth(buttonWidth);
    buttonText.setTextAlignment(TextAlignment.CENTER);
    buttonText.setFont(GAME_FONT.newFont(buttonHeight * 0.6));
    buttonText.setFill(Config.PRIMARY_COLOR);

    // return them
    return new ArrayList<>(List.of(buttonBox, buttonText));
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

  /**
   * Need to override to have a different image.
   */
  protected void setCardImage() {
    // if we have a card entity we are inspecting
    if (this.level != null) {
      cardImage = FXGL.texture("level_" + this.level.name().toLowerCase() + ".png");
    } else {
      cardImage = FXGL.texture("level_one.png"); // placeholder
    }
  }

  // TO OVERRIDE/MODIFY ////////////////////////////////////////////////////////////////////////////

  /**
   * Need to override to not have a ReserveButton, set cardIsReserved to true to do so.
   */
  protected void setCardIsReserved() {
    cardIsReserved = false;
  }

  /**
   * Adds behaviour to the node t, supposed to be a button.
   *
   * @param reserveCardPrompt Object that allows access to instance method cardBought.
   * @param t                 the Node we want to add button like behaviour to.
   */
  private void setBehaviour(ReserveCardPrompt reserveCardPrompt, Node t) {
    t.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      // add behaviour here
      closeBuyPrompt();
      reserveCardPrompt.cardReservation();
      e.consume();
    });
  }
}
