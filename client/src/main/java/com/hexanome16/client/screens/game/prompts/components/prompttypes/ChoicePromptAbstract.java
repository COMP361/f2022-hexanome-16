package com.hexanome16.client.screens.game.prompts.components.prompttypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * TokenAcquiringTwo but renamed for the purpose of code reuse.
 * Classes that extend this class need to assign values to
 * atPromptText and atPromptTextFontSize.
 */
public abstract class ChoicePromptAbstract implements PromptTypeInterface {

  /**
   * The width.
   */
  protected final double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  protected final double atHeight = getAppHeight() / 2.;
  /**
   * The top left x.
   */
  protected final double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2.);
  /**
   * The top left y.
   */
  protected final double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2.);

  /**
   * The confirm button.
   */
  protected StackPane atConfirmButton;
  /**
   * The confirmation circle.
   */
  protected Circle atConfirmCircle;


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

    promptOpens();
    // This is just to reinitialize myChoices when the prompt is closed
    // (These objects are unique, and so they don't get destroyed when the prompt closes)
    FXGL.getEventBus().addEventHandler(SplendorEvents.CLOSING, e -> {
      handlePromptForceQuit();
    });

    //initiate BorderPane Top will be Text, Center will be Token types and right will be the button
    BorderPane myPrompt = new BorderPane();
    initiatePane(myPrompt);

    // initialize and set up prompt message ////////////////////////////////////////////////////////

    // get prompt text and that text's size
    String atPromptText = promptText();
    double atPromptTextFontSize = promptTextSize();

    // do rest of set up
    Text promptMessage = new Text();            //Prompt Message : Top
    promptMessage.setText(atPromptText);
    promptMessage.setFont(Font.font(atPromptTextFontSize));
    promptMessage.setTextAlignment(TextAlignment.CENTER);
    promptMessage.setWrappingWidth(atWidth);
    myPrompt.setTop(promptMessage);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Confirm button //////////////////////////////////////////////////////////////////////////////
    double confirmButtonRadii = atHeight / 10;
    atConfirmCircle = new Circle();      //Button : Right
    atConfirmCircle.setRadius(confirmButtonRadii);
    atConfirmCircle.setFill(Config.SECONDARY_COLOR);
    atConfirmCircle.setOpacity(0.5);

    /*Centering circle*/
    Text confirm = new Text("Confirm");
    confirm.setFont(Font.font(atHeight / 20.));
    atConfirmButton = new StackPane(atConfirmCircle, confirm);
    atConfirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if (canConfirm()) {
        handleConfirmation();
      }
    });

    // little centering maneuver here
    HBox inter1 = new HBox(atConfirmButton);
    inter1.setAlignment(Pos.CENTER);
    inter1.setPrefSize(confirmButtonRadii * 4, atHeight);
    inter1.setSpacing(5);
    /*Circle's in inter1*/
    myPrompt.setRight(inter1);
    ////////////////////////////////////////////////////////////////////////////////////////////////


    // Choice  /////////////////////////////////////////////////////////////////////////////////////
    double maxWidthChoice = (getWidth() * 6 / 10);
    double maxHeightChoice = (getHeight() * 3 / 4);
    Node centerNode = promptChoice(maxWidthChoice, maxHeightChoice);
    myPrompt.setCenter(centerNode);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //add elements to view
    entity.getViewComponent().addChild(myPrompt);
  }

  // Helpers
  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(atTopLeftX);
    myPrompt.setTranslateY(atTopLeftY);
    myPrompt.setPrefWidth(atWidth);
    myPrompt.setMaxWidth(atWidth);
    myPrompt.setPrefHeight(atHeight);
    myPrompt.setMaxHeight(atHeight);
  }

  /**
   * Builds a Node which will be the center of the prompt, preferably of width 6/10 atWidth
   * and height 3/4 atHeight.
   *
   * @param choiceWidth  the choice width
   * @param choiceHeight the choice height
   * @return a Node in which the choice will happen.
   */
  protected Node promptChoice(double choiceWidth, double choiceHeight) {
    // initialize and set up bonuses layout
    HBox choicesLayout = new HBox();                  //Token Types : Center
    choicesLayout.setAlignment(Pos.CENTER);
    choicesLayout.setPrefSize(choiceWidth, choiceHeight);
    choicesLayout.setMaxWidth(choiceWidth);
    addToLayout(choicesLayout);
    return choicesLayout;
  }

  // ^^^^^^^^^^^^^    BASIC SETUP   ^^^^^^^^^^^^^^^
  // VVVVVVVVVVVVV ABSTRACT METHODS VVVVVVVVVVVVVVV

  /**
   * To override if there is a need to do something when prompt opens.
   */
  protected void promptOpens() {
  }

  /**
   * Gets the Prompt Text.
   *
   * @return Prompt Text as a String.
   */
  protected abstract String promptText();

  /**
   * Gets the Prompt text Size.
   *
   * @return Prompt text Size as a double.
   */
  protected abstract double promptTextSize();

  /**
   * Handles the User force quitting the prompt.
   */
  protected abstract void handlePromptForceQuit();

  /**
   * Checks if user can confirm his choice.
   *
   * @return true if user can confirm his choice, false otherwise.
   */
  protected abstract boolean canConfirm();

  /**
   * Does something when User Confirms his choice, probably would want to close prompt after that.
   * (Use PromptComponent.closePrompts() to do so)
   */
  protected abstract void handleConfirmation();

  /**
   * Modifies choicesLayout to contain the desired choices and their behaviour, is also
   * responsible for the spacing between the choices.
   *
   * @param choicesLayout The layout Node, an HBox
   */
  protected abstract void addToLayout(HBox choicesLayout);

}
