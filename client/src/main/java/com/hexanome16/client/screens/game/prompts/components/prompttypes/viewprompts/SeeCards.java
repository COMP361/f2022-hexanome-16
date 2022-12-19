package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.google.gson.Gson;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  private static List<String> cardPaths = new ArrayList<>();

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
  //List<String> cards = List.of("card1.png", "card2.png");

  /**
   * Fetches cards in the provided player's inventory.
   *
   * @param player player
   */
  public static void fetchCards(String player) {
    // make a call to the server
    long sessionId = GameScreen.getSessionId();
    String response = PromptsRequests.getCards(sessionId, player);
    // convert it to a list of maps
    Gson myGson = new Gson();
    List<Map<String, String>> cards = myGson.fromJson(response, List.class);
    // add the paths to our list
    cardPaths = new ArrayList<>();
    for (Map<String, String> card : cards) {
      cardPaths.add(card.get("texturePath") + ".png");
    }
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

    // add cards to player's hand TODO this
    for (String card : cardPaths) {
      myCards.getChildren().add(cardFromName(card));
    }

    myCards.setPrefWidth(atWidth);

    entity.getViewComponent().addChild(myBorderPane);
  }

  /**
   * Get the card as a texture from its name.
   *
   * @param cardName cardName
   * @return card as a texture
   */
  private Texture cardFromName(String cardName) {
    Texture card = FXGL.texture(cardName);
    card.setFitWidth(atCardWidth);
    card.setFitHeight(atCardHeight);
    return card;
  }
}
