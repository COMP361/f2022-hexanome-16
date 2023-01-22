package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.google.gson.Gson;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import com.hexanome16.client.utils.AuthUtils;
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
 * A class responsible for populating See Own reserved Cards prompt.
 */
public class SeeReserved extends SeeReservedAbstract {

  private static List<String> cardTexturePaths = new ArrayList<>();

  /**
   * Fetches cards in the provided player's inventory.
   *
   * @param player player
   */
  public static void fetchReservedCards(String player) {
    // make a call to the server
    String response = PromptsRequests.getReservedCards(GameScreen.getSessionId(),
        player, AuthUtils.getAuth().getAccessToken());
    // are these my cards?
    boolean myCards = AuthUtils.getPlayer().getName().equals(player);
    // convert it to a list of maps
    Gson myGson = new Gson();
    List<Map<String, Object>> cards = myGson.fromJson(response, List.class);
    // add the paths to our list
    cardTexturePaths = new ArrayList<>();
    for (Map<String, Object> card : cards) {
      // my cards are always face up
      if (myCards || !(boolean) card.get("faceDown")) {
        cardTexturePaths.add(card.get("texturePath") + ".png");
      } else {
        cardTexturePaths.add("level_" + ((String) card.get("level")).toLowerCase() + ".png");
      }
    }
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
  
  // ************************************************************* 
  
  @Override
  protected void promptOpened() {
    assert (hiddenCards + viewAbleCards.size()) < 3;
    hiddenCards = 0;
    viewAbleCards = new ArrayList<>(
        List.of(FXGL.texture("card1.png"), FXGL.texture("card1.png")));
  }

  @Override
  protected String promptText() {
    return "Own reserved cards";
  }

  @Override
  protected void appendBehaviour(Texture t) {
    t.setOnMouseClicked(e -> {
      OpenPrompt.openPrompt(PromptType.BUYING_RESERVED);
    });
  }
  
}
