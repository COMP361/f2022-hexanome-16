package com.hexanome16.screens.game.prompts.actualyUI.Components;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes.BuyCard;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes.CustomEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PromptComponent extends Component {

  PromptTypeInterface aPromptType;
  double aWidth;
  double aHeight;

  double topleftX;
  double topleftY;

  Entity aCardEntity;

  public PromptComponent(PromptTypeInterface pPromptType, Entity CardEntity) {
    aPromptType = pPromptType;
    aWidth = pPromptType.width();
    aHeight = pPromptType.height();
    topleftX = getAppWidth() / 2 - (aWidth / 2);
    topleftY = getAppHeight() / 2 - (aHeight / 2);
    aCardEntity = CardEntity;
  }

  public PromptComponent(PromptTypeInterface pPromptType) {
    aPromptType = pPromptType;
    aWidth = pPromptType.width();
    aHeight = pPromptType.height();
    topleftX = getAppWidth() / 2 - (aWidth / 2);
    topleftY = getAppHeight() / 2 - (aHeight / 2);
  }

  public static void closePrompts() {

    FXGL.getGameWorld()
        .removeEntities(FXGL.getGameWorld().getEntitiesByComponent(PromptComponent.class));
    FXGL.getEventBus().fireEvent(new CustomEvent(CustomEvent.CLOSING));
  }

  @Override
  public void onAdded() {
    initiateWorldProperties();
    buildBox(10);
    if (aCardEntity == null) {
      aPromptType.populatePrompt(entity);
    } else {
      ((BuyCard) aPromptType).populatePrompt(entity, aCardEntity);
    }
    buildButton(30, 15, 5);
  }

  // This is for Buying stuff
  private void initiateWorldProperties() {
    PropertyMap buyMap = FXGL.getWorldProperties();

    String playerbankPrefix = BuyCard.BankType.PLAYER_BANK + "/";
    String gameBankPrefix = BuyCard.BankType.GAME_BANK + "/";
    int i = 0;
    for (BuyCard.CurrencyType e : BuyCard.CurrencyType.values()) {
      String gameBank = gameBankPrefix + (e.toString());
      String playerBankKeys = playerbankPrefix + (e);
      i++;
      buyMap.setValue(playerBankKeys, i);
      int value = 0;
      buyMap.setValue(gameBank, value);
    }
  }

  private void buildBox(double pOutterSpace) {
    //initiate elements
    Rectangle myRectangle = new Rectangle(getAppWidth(), getAppHeight(), Color.GREY);
    Node myPromptOuter =
        new Rectangle(aWidth + pOutterSpace, aHeight + pOutterSpace, Color.rgb(249, 161, 89));
    Node myPrompt = new Rectangle(aWidth, aHeight, Color.rgb(78, 147, 180));

    //customizing Component/////////////////////////////////
    myRectangle.setOpacity(0.5);
    /////////
    myPromptOuter.setTranslateX(topleftX - (pOutterSpace / 2));
    myPromptOuter.setTranslateY(topleftY - (pOutterSpace / 2));
    /////////
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    ////////////////////////////////////////////////////////

    //adding elements
    entity.getViewComponent().addChild(myRectangle);
    entity.getViewComponent().addChild(myPromptOuter);
    entity.getViewComponent().addChild(myPrompt);

  }

  private void buildButton(double pButtonWidth, double pFontSize, double pButtonAddedHeight) {
    //initiate elements
    Text mybutton = new Text("X");
    Node myBBox = new Rectangle(pButtonWidth, pFontSize + pButtonAddedHeight, Color.RED);


    //customizing Component : Text
    mybutton.setFont((new Font(pFontSize)));
    mybutton.setTextAlignment(TextAlignment.CENTER);
    mybutton.setWrappingWidth(pButtonWidth);
    mybutton.setTranslateX(topleftX + aWidth - pButtonWidth);
    mybutton.setTranslateY(topleftY + pFontSize);
    mybutton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      closePrompts();
      e.consume();
    });

    //customizing Component : boundingbox
    myBBox.setOpacity(0);
    myBBox.setTranslateX(topleftX + aWidth - pButtonWidth);
    myBBox.setTranslateY(topleftY);
    myBBox.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
      myBBox.setOpacity(0.6);
    });
    myBBox.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
      myBBox.setOpacity(0);
    });
    myBBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      entity.removeFromWorld();
      closePrompts();
      e.consume();
    });


    //adding elements
    entity.getViewComponent().addChild(mybutton);
    entity.getViewComponent().addChild(myBBox);
  }

}
