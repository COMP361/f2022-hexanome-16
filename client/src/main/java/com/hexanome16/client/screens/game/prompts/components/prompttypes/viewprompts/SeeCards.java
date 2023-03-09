package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.common.models.LevelCard;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class responsible for populating See Cards prompt.
 */
public class SeeCards implements PromptTypeInterface {

  private static List<String> cardTexturePaths = new ArrayList<>();

  /**
   * The width.
   */
  private final double atWidth = getAppWidth() / 2.;
  /**
   * The height.
   */
  private final double atHeight = getAppHeight() * 0.8;
  /**
   * The card width.
   */
  private final double atCardWidth = atWidth / 4;
  /**
   * The card height.
   */
  private final double atCardHeight = atCardWidth * 1.39;
  /**
   * The top left x.
   */
  private final double atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2);
  /**
   * The top left y.
   */
  private final double atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2);
  //List<String> cards = List.of("card1.png", "card2.png");


  /**
   * Fetches cards in the provided player's inventory.
   *
   * @param player player
   */
  public static void fetchCards(String player) {
    // make a call to the server
    long sessionId = GameScreen.getSessionId();
    LevelCard[] response = PromptsRequests.getCards(sessionId, player);
    // add the paths to our list
    cardTexturePaths = new ArrayList<>();
    for (LevelCard card : response) {
      cardTexturePaths.add(card.getCardInfo().texturePath() + ".png");
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
  public boolean isCancelable() {
    return true;
  }

  @Override
  public void populatePrompt(Entity entity) {

    Text myPromptMessage = new Text("Hand View");
    myPromptMessage.setFont(GAME_FONT.newFont(50));
    myPromptMessage.setFill(Config.SECONDARY_COLOR);
    myPromptMessage.setTextAlignment(TextAlignment.CENTER);
    myPromptMessage.setWrappingWidth(atWidth);

    ScrollPane myScrollPane = new ScrollPane();
    myScrollPane.setPrefViewportWidth(atWidth);
    myScrollPane.setPrefViewportHeight(atHeight - 60); // 20 is height of X button
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
    for (String card : cardTexturePaths) {
      myCards.getChildren().add(getCardTexture(card));
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
  private Texture getCardTexture(String cardName) {
    Texture card = FXGL.texture(cardName);
    card.setFitWidth(atCardWidth);
    card.setFitHeight(atCardHeight);
    return card;
  }
}
