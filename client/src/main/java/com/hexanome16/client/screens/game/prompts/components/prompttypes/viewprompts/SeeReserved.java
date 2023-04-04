package com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.Config;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.dto.cards.DeckJson;
import com.hexanome16.common.models.LevelCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.SneakyThrows;

/**
 * A class responsible for populating See Own reserved Cards prompt.
 */
public class SeeReserved implements PromptTypeInterface {
  static Map<String, LevelCard> cardHashList = new HashMap<String, LevelCard>();

  private static boolean myCards = false;


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
  double atHeight = getAppHeight() * 0.8;
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
  public boolean isCancelable() {
    return true;
  }

  @Override
  public boolean canBeOpenedOutOfTurn() {
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
    if (myCards) {
      cardTexture.setOnMouseClicked(e -> PromptUtils.openPrompt(getCardEntity(card)));
    } else if (card.getValue().getFaceDown()) {
      cardTexture =
          FXGL.texture("level_" + card.getValue().getLevel().name().toLowerCase() + ".png");
    }
    cardTexture.setFitWidth(atCardWidth);
    cardTexture.setFitHeight(atCardHeight);
    return cardTexture;
  }

  @SneakyThrows
  private Entity getCardEntity(Map.Entry<String, LevelCard> card) {
    return FXGL.entityBuilder()
        .view(card.getValue().getCardInfo().texturePath() + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(card.getValue().getCardInfo().id(), card.getValue().getLevel(),
            card.getValue().getCardInfo().texturePath() + ".png",
            card.getValue().getCardInfo().price(),
            card.getKey(), false))
        .build();
  }

  // *************************************************************
  // for making hidden cards
  private Node makeAnonymousCard() {
    Text myInterrogation = new Text("?");
    myInterrogation.setFont(GAME_FONT.newFont(atCardHeight * 0.9));
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
  protected void promptOpened() {
    assert (hiddenCards + viewAbleCards.size()) < 3;
    hiddenCards = 0;
    viewAbleCards = new ArrayList<>(
        List.of(FXGL.texture("card1.png"), FXGL.texture("card1.png")));
  }

  /**
   * Prompt text string.
   *
   * @return the string
   */
  protected String promptText() {
    return "Own reserved cards";
  }

  /**
   * Adds behaviour to object t, specifically made for see Own reserved.
   * This should allow opening a buy prompt.
   *
   * @param t the texture to which the behaviour will be added.
   */
  protected void appendBehaviour(Texture t) {
    t.setOnMouseClicked(e -> PromptUtils.openPrompt(PromptType.BUYING_RESERVED));
  }

}
