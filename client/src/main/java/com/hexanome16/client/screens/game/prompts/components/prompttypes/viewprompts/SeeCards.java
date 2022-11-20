package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class responsible for populating See Cards prompt.
 */
public class SeeCards implements PromptTypeInterface {

  double atWidth = getAppWidth() / 2.;
  double atHeight = getAppHeight() / 2.;
  double atCardWidth = atWidth / 4;
  double atCardHeight = atCardWidth * 1.39;
  double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);

  @Override
  public double width() {
    return atWidth;
  }

  @Override
  public double height() {
    return atHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {

    Text myPromptMessage = new Text("Hand View");
    myPromptMessage.setFont(Font.font(15));
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(atWidth);

    ScrollPane myScrollPane = new ScrollPane();
    myScrollPane.setPrefViewportWidth(atWidth);
    myScrollPane.setPrefViewportHeight(atHeight - 20); // 20 is height of X button
    myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    myScrollPane.setPannable(true);
    myScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    myScrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

    BorderPane myBorderPane = new BorderPane();
    myBorderPane.setTranslateX(atTopLeftX);
    myBorderPane.setTranslateY(atTopLeftY);
    myBorderPane.setTop(myPromptMessage);
    myBorderPane.setCenter(myScrollPane);

    TilePane myCards = new TilePane();
    myScrollPane.setContent(myCards);

    // add cards to player's hand
    for (int i = 0; i < 2; i++) {
      Texture myCard = FXGL.texture("card1.png");
      myCard.setFitWidth(atCardWidth);
      myCard.setFitHeight(atCardHeight);
      myCards.getChildren().add(myCard);
    }
    myCards.setPrefWidth(atWidth);

    entity.getViewComponent().addChild(myBorderPane);
  }

}
