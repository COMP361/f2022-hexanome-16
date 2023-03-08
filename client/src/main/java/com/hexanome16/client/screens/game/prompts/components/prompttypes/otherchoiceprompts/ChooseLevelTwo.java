package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.core.collection.Array;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.events.SplendorEvents;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ChoicePromptAbstract;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.LevelCard;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Prompt for Choosing level two card.
 */
public class ChooseLevelTwo extends ChoicePromptAbstract {

  private ArrayList<LevelCard> levelTwoCards;
  private int chosenNobleIndex;
  private double cardWidth;
  private double cardHeight;
  private double cardSpacing;
  private ArrayList<Node> cardSelectionBox;

  /**
   * default constructor.
   */
  public ChooseLevelTwo() {
    atWidth = FXGL.getAppWidth() * 0.90;
    atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2.);
    atTopLeftY = (getAppHeight() / 2.) - (atHeight / 2.);
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

  @Override
  protected void promptOpens() {
    chosenNobleIndex = -1;
    cardSelectionBox = new ArrayList<>();
    levelTwoCards = new ArrayList<>(
        Arrays.stream(
            PromptsRequests.getLevelTwoCardsOnBoard(GameScreen.getSessionId()))
            .toList());

  }

  @Override
  protected String promptText() {
    return "Choose Level Two";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }

  @Override
  protected void handlePromptForceQuit() {
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1.;
  }

  @Override
  protected void handleConfirmation() {
    LevelCard chosenCard = levelTwoCards.get(chosenNobleIndex);
    String hash = GameScreen.getCardHash(chosenCard);
    PromptComponent.closePrompts();
    long sessionId = GameScreen.getSessionId();
    String accessToken = AuthUtils.getAuth().getAccessToken();
    Pair<Headers, String> serverResponse = PromptsRequests
        .takeLevelTwo(sessionId, accessToken, hash);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  @Override
  protected void addToLayout(HBox choicesLayout) {
    cardWidth = FXGL.getAppWidth() * 0.1;
    cardHeight = cardWidth * 1.44;
    cardSpacing = (8 * (getWidth() / 10.)
        - (cardWidth * levelTwoCards.size()))
        / (levelTwoCards.size() + 1);
    choicesLayout.setSpacing(cardSpacing);
    for (LevelCard levelCard : levelTwoCards) {
      Texture texture = FXGL.texture(levelCard.getCardInfo().texturePath() + ".png");
      Node node = makeNode(texture);
      choicesLayout.getChildren().add(node);
    }
  }

  private Node makeNode(Texture texture) {
    // fix texture size
    texture.setFitWidth(cardWidth);
    texture.setFitHeight(cardHeight);

    // create selection rectangle and add it to array
    Rectangle selectionRectangle = new Rectangle(cardWidth * 1.1, cardHeight * 1.1, Color.WHITE);
    selectionRectangle.setOpacity(0.5);
    cardSelectionBox.add(selectionRectangle);

    // set up layout of noble and add behaviour
    StackPane myCard = new StackPane();
    myCard.getChildren().addAll(selectionRectangle, texture);
    PromptTypeInterface.setOnHoverEffectOpacity(myCard, selectionRectangle, 0.5, 0.7,
        e -> ((Rectangle) e).getOpacity() != 1, selectionRectangle);

    myCard.setOnMouseClicked(e -> {
      for (Node n : cardSelectionBox) {
        n.setOpacity(0.5);
      }
      selectionRectangle.setOpacity(1);
      chosenNobleIndex = cardSelectionBox.indexOf(selectionRectangle);
      atConfirmCircle.setOpacity(1);
    });
    return myCard;
  }
}