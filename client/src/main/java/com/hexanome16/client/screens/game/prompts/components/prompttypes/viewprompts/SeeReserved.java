package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.models.LevelCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.SneakyThrows;

/**
 * A class responsible for populating See Own reserved Cards prompt.
 */
public class SeeReserved extends SeeReservedAbstract {
  static Map<String, LevelCard> cardHashList = new HashMap<String, LevelCard>();

  private static boolean myCards = false;

  /**
   * Fetches cards in the provided player's inventory.
   *
   * @param player player
   */
  public static void fetchReservedCards(String player) {
    cardHashList.clear();
    // make a call to the server
    DeckJson response = PromptsRequests.getReservedCards(GameScreen.getSessionId(),
        player, AuthUtils.getAuth().getAccessToken());
    // are these my cards?
    myCards = AuthUtils.getPlayer().getName().equals(player);
    // add the paths to our list
    cardHashList = response.getCards();
  }

  @Override
  public void populatePrompt(Entity entity) {

    Text myPromptMessage = new Text("Hand View");
    myPromptMessage.setFont(GAME_FONT.newFont(15));
    myPromptMessage.setFill(Config.SECONDARY_COLOR);
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
    for (Map.Entry<String, LevelCard> entry : cardHashList.entrySet()) {
      myCards.getChildren().add(getCardTexture(entry));
    }

    myCards.setPrefWidth(atWidth);

    entity.getViewComponent().addChild(myBorderPane);
  }

  /**
   * Get the card as a texture from its name.
   *
   * @param card card
   * @return card as a texture
   */
  private Texture getCardTexture(Map.Entry<String, LevelCard> card) {
    Texture cardTexture = FXGL.texture(card.getValue().getCardInfo().texturePath() + ".png");
    cardTexture.setFitWidth(atCardWidth);
    cardTexture.setFitHeight(atCardHeight);
    if (myCards) {
      cardTexture.setOnMouseClicked(e -> OpenPrompt.openPrompt(getCardEntity(card)));
    }
    return cardTexture;
  }

  @SneakyThrows
  private Entity getCardEntity(Map.Entry<String, LevelCard> card) {
    if (myCards) {
      return FXGL.entityBuilder()
          .view(card.getValue().getCardInfo().texturePath() + ".png")
          .scale(0.15, 0.15)
          .with(new CardComponent(card.getValue().getCardInfo().id(), card.getValue().getLevel(),
              card.getValue().getCardInfo().texturePath() + ".png",
              card.getValue().getCardInfo().price(),
              card.getKey(), false))
          .build();
    } else if (card.getValue().isFaceDown()) {
      return FXGL.entityBuilder()
          .view(card.getValue().getLevel().name().toLowerCase() + ".png")
          .scale(0.15, 0.15)
          .build();
    } else {
      return FXGL.entityBuilder()
          .view(card.getValue().getCardInfo().texturePath() + ".png")
          .scale(0.15, 0.15)
          .build();
    }
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
    t.setOnMouseClicked(e -> OpenPrompt.openPrompt(PromptType.BUYING_RESERVED));
  }

}
