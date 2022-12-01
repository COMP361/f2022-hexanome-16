package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * An abstract class responsible for populating See reserved Cards prompt, for code reuse.
 */
public abstract class SeeReservedAbstract implements PromptTypeInterface {


  /**
   * The View able cards.
   */
  protected ArrayList<Texture> viewAbleCards;
  /**
   * The Hidden cards.
   */
  protected int hiddenCards;
  /**
   * The width.
   */
  double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  double atHeight = getAppHeight() / 2.;
  /**
   * The card width.
   */
  double atCardWidth = atWidth / 4;
  /**
   * The card height.
   */
  double atCardHeight = atCardWidth * 1.39;
  /**
   * The top left x.
   */
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  /**
   * The top left y.
   */
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);

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
    // Fetch info for prompt
    promptOpened();

    // Prompt Message
    Text myPromptMessage = new Text(promptText());
    myPromptMessage.setFont(Font.font(atHeight / 10));
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(atWidth);

    // Prompt Choice Layout
    HBox myReservedCards = new HBox();
    myReservedCards.setAlignment(Pos.CENTER);
    myReservedCards.setSpacing((atWidth - 3 * atCardWidth) / 4);
    myReservedCards.setPrefSize(atWidth, atHeight * 0.8);

    // make and add Visible cards to prompt choice layout
    for (Texture t : viewAbleCards) {
      t.setFitWidth(atCardWidth);
      t.setFitHeight(atCardHeight);
      appendBehaviour(t);
      myReservedCards.getChildren().add(t);
    }

    // make and add hidden cards to prompt choice layout
    for (int i = 0; i < hiddenCards; i++) {
      Node myAnonymousCard = makeAnonymousCard();
      myReservedCards.getChildren().add(myAnonymousCard);
    }

    // Overall Prompt layout
    BorderPane myBorderPane = new BorderPane();
    myBorderPane.setTranslateX(atTopLeftX);
    myBorderPane.setTranslateY(atTopLeftY);
    myBorderPane.setTop(myPromptMessage);
    myBorderPane.setCenter(myReservedCards);

    // add prompt layout to view
    entity.getViewComponent().addChild(myBorderPane);
  }

  // for making hidden cards
  private Node makeAnonymousCard() {
    Text myInterrogation = new Text("?");
    myInterrogation.setFont(Font.font(atCardHeight * 0.9));
    myInterrogation.setFill(Color.WHITE);
    myInterrogation.setTextAlignment(TextAlignment.CENTER);

    Rectangle myOtherCard = new Rectangle(atCardWidth, atCardHeight, Color.BLACK);
    StackPane myAnonymousCard = new StackPane();
    myAnonymousCard.getChildren().addAll(myOtherCard, myInterrogation);
    return myAnonymousCard;
  }

  /**
   * If others reserved card then hiddenCards = 0 and need to append behaviour
   * else hiddenCards >= 0  and no need to add behaviour.
   * Note that this will throw an exception if the number of cards total,
   * hiddenCards + (number of textures in viewAbleCards) is greater than 3.
   */
  protected abstract void promptOpened();

  /**
   * Prompt text string.
   *
   * @return the string
   */
  protected abstract String promptText();

  /**
   * Adds behaviour to object t, specifically made for see Own reserved.
   * This should allow opening a buy prompt.
   *
   * @param t the texture to which the behaviour will be added.
   */
  protected abstract void appendBehaviour(Texture t);

}
