package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SeeCards implements PromptTypeInterface {

  double aWidth = getAppWidth() / 2;
  double aHeight = getAppHeight() / 2;
  double aCardWidth = aWidth / 4;
  double aCardHeight = aCardWidth * 1.39;
  double topleftX = (getAppWidth() / 2) - (aWidth / 2);
  double topleftY = (getAppHeight() / 2) - (aHeight / 2);

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
    BorderPane myBorderPane = new BorderPane();
    myBorderPane.setTranslateX(topleftX);
    myBorderPane.setTranslateY(topleftY);
    ScrollPane myScrollPane = new ScrollPane();
    Text myPromptMessage = new Text("Hand View");
    myPromptMessage.setFont(Font.font(15));
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(aWidth);
    myBorderPane.setTop(myPromptMessage);
    myBorderPane.setCenter(myScrollPane);
    //    myScrollPane.setTranslateX(topleftX);
    //    myScrollPane.setTranslateY(topleftY);

    myScrollPane.setPrefViewportWidth(aWidth);
    myScrollPane.setPrefViewportHeight(aHeight - 20); // 20 is height of X button
    myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    myScrollPane.setPannable(true);
    myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myScrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

    TilePane myCards = new TilePane();
    myScrollPane.setContent(myCards);

    // add cards to player's hand
    for (int i = 0; i < 2; i++) {
      Texture myCard = FXGL.texture("card1.png");
      myCard.setFitWidth(aCardWidth);
      myCard.setFitHeight(aCardHeight);
      myCards.getChildren().add(myCard);
    }
    myCards.setPrefWidth(aWidth);

    entity.getViewComponent().addChild(myBorderPane);
  }

}
