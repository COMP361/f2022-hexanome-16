package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.LevelCard;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Prompt for choosing level one card.
 */
public class ChooseLevelOne extends ChooseLevelTwo {

  /**
   * default constructor, just sets up size of prompt.
   */
  public ChooseLevelOne() {
    super();
  }

  @Override
  protected void promptOpens() {
    chosenLevelIndex = -1;
    cardSelectionBox = new ArrayList<>();
    levelCards = new ArrayList<>(
        Arrays.stream(
                PromptsRequests.getLevelOneCardsOnBoard(GameScreen.getSessionId()))
            .toList());
  }

  @Override
  protected String promptText() {
    return "Choose Level One";
  }

  @Override
  protected void handleConfirmation() {
    LevelCard chosenCard = levelCards.get(chosenLevelIndex);
    String hash = GameScreen.getCardHash(chosenCard);
    PromptComponent.closePrompts();
    long sessionId = GameScreen.getSessionId();
    String accessToken = AuthUtils.getAuth().getAccessToken();
    Pair<Headers, String> serverResponse = PromptsRequests
        .takeLevelOne(sessionId, accessToken, hash);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

}
