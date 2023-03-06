package com.hexanome16.client.screens.game.prompts.components;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.CurrencyType;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ReserveCardPrompt;
import com.hexanome16.common.models.Level;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * FXGL Component for prompts, used for Prompt entity.
 */
public class PromptComponent extends Component {

  /**
   * The prompt type.
   */
  PromptTypeInterface atPromptType;
  /**
   * The width.
   */
  double atWidth;
  /**
   * The height.
   */
  double atHeight;
  /**
   * The top left x.
   */
  double atTopLeftX;
  /**
   * The top left y.
   */
  double atTopLeftY;
  /**
   * The prompt brim.
   */
  double atPromptBrim;
  /**
   * The button width.
   */
  double atButtonWidth;
  /**
   * The button font size.
   */
  double atButtonFontSize;
  /**
   * The button added height.
   */
  double atButtonAddedHeight;
  /**
   * The card entity.
   */
  Entity atCardEntity;

  Level level;

  /**
   * Alternative constructor for PromptComponent (for buying cards prompt).
   * To rework.
   *
   * @param promptType Type of prompt to be associated to the entity with this component.
   * @param cardEntity The card entity whose view will be displayed in the prompt.
   */
  public PromptComponent(PromptTypeInterface promptType, Entity cardEntity) {
    this(promptType);
    atCardEntity = cardEntity;
  }

  /**
   * Alternative constructor for PromptComponent (for buying cards prompt).
   * Based on card level.
   *
   * @param promptType Type of prompt to be associated to the entity with this component.
   * @param level      The level of the card whose view will be displayed in the prompt.
   */
  public PromptComponent(PromptTypeInterface promptType, Level level) {
    this(promptType);
    this.level = level;
  }

  /**
   * Main constructor for PromptComponent.
   *
   * @param promptType Type of prompt to be associated to the entity with this component.
   */
  public PromptComponent(PromptTypeInterface promptType) {
    atPromptType = promptType;
    atWidth = promptType.getWidth();
    atHeight = promptType.getHeight();
    atTopLeftX = getAppWidth() / 2. - (atWidth / 2);
    atTopLeftY = getAppHeight() / 2. - (atHeight / 2);
    // The following numbers are simply what looks best.
    atPromptBrim = getAppWidth() * 0.0025;
    atButtonWidth = atPromptBrim * 6.;
    atButtonFontSize = atButtonWidth / 2.;
    atButtonAddedHeight = atButtonFontSize / 2.;
  }

  @Override
  public void onUpdate(double tpf) {
    entity.setZIndex(100);
  }

  /**
   * Closes all open prompts and fires an Event CustomEvent.CLOSING.
   */
  public static void closePrompts() {

    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(PromptComponent.class));
    FXGL.getEventBus().fireEvent(new SplendorEvents(SplendorEvents.CLOSING));
  }


  /**
   * Builds prompt and its different parts.
   */
  @Override
  public void onAdded() {
    initiateWorldProperties();
    buildBox();
    if (level != null) {
      ((ReserveCardPrompt) atPromptType).populatePrompt(entity, level);
    } else if (atCardEntity == null) {
      atPromptType.populatePrompt(entity);
    } else {
      ((BuyCardPrompt) atPromptType).populatePrompt(entity, atCardEntity);
    }
    buildButton();
  }

  // This method is to allow buying card prompt to be functional.
  private void initiateWorldProperties() {
    PropertyMap buyMap = FXGL.getWorldProperties();

    String playerbankPrefix = BuyCardPrompt.BankType.PLAYER_BANK + "/";
    String gameBankPrefix = BuyCardPrompt.BankType.GAME_BANK + "/";
    int i = 0;
    for (CurrencyType e : CurrencyType.values()) {
      String gameBank = gameBankPrefix + (e.toString());
      String playerBankKeys = playerbankPrefix + (e);
      i++;
      buyMap.setValue(playerBankKeys, i);
      int value = 0;
      buyMap.setValue(gameBank, value);
    }
  }

  // This method builds the box of the prompt.
  private void buildBox() {

    //initiate elements ////////////////////////////////////////////////////////////////////////////
    // disabling box
    Rectangle disablingBox = new Rectangle(getAppWidth(), getAppHeight(), Color.GREY);
    // prompt box
    Rectangle myPrompt = new Rectangle(atWidth, atHeight, Config.PRIMARY_COLOR);
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //customizing Component/////////////////////////////////////////////////////////////////////////
    // set disabling box opacity lower
    disablingBox.setOpacity(0.5);
    // put prompt where it's supposed to be and add a brim
    myPrompt.setTranslateX(atTopLeftX);
    myPrompt.setTranslateY(atTopLeftY);
    myPrompt.setStrokeWidth(atPromptBrim);
    myPrompt.setStrokeType(StrokeType.OUTSIDE);
    myPrompt.setStroke(Config.SECONDARY_COLOR);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //adding elements to entity ////////////////////////////////////////////////////////////////////
    entity.getViewComponent().addChild(disablingBox);
    entity.getViewComponent().addChild(myPrompt);
    ////////////////////////////////////////////////////////////////////////////////////////////////
  }

  // This method builds the little x at the top right
  private void buildButton() {
    // Initiate elements ///////////////////////////////////////////////////////////////////////////
    Text myX = new Text("X");
    myX.setFont(Font.font("", FontWeight.BOLD, atButtonFontSize));
    Node myBox = new Rectangle(atButtonWidth, atButtonFontSize + atButtonAddedHeight, Color.RED);
    StackPane button = new StackPane();
    ////////////////////////////////////////////////////////////////////////////////////////////////


    // Customizing elements ////////////////////////////////////////////////////////////////////////
    // customizing Component : Button
    button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> myBox.setOpacity(1));
    button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> myBox.setOpacity(0));
    button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      closePrompts();
      e.consume();
    });
    button.setTranslateX(atTopLeftX + atWidth - atButtonWidth);
    button.setTranslateY(atTopLeftY);
    button.setPrefSize(atButtonWidth, atButtonFontSize + atButtonAddedHeight);

    // customizing Component : boundingbox
    myBox.setOpacity(0);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // adding elements /////////////////////////////////////////////////////////////////////////////
    button.getChildren().addAll(myBox, myX);
    entity.getViewComponent().addChild(button);
    ////////////////////////////////////////////////////////////////////////////////////////////////
  }


}
