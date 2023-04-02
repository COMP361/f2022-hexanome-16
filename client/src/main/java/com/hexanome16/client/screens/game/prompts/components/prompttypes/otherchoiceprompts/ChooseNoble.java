package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.Noble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Class responsible for populating Noble Conflict prompt.
 */
public class ChooseNoble extends NobleChoiceAbstract {

  /**
   * List of nobles to choose from.
   */
  protected static Noble[] nobleList;

  /**
   * Sets the list of nobles.
   *
   * @param nobleList list of nobles
   */
  public static void setNobleList(Noble[] nobleList) {
    ChooseNoble.nobleList = nobleList;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

  @Override
  public boolean canBeOpenedOutOfTurn() {
    return true;
  }

  @Override
  protected String promptText() {
    return "Noble conflict";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1.;
  }

  @Override
  protected void handleConfirmation() {
    // to modify, use chosenNobleIndex to get index of the choice

    int nobleIndex = chosenNobleIndex;
    long sessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    Noble nobleOfInterest = nobleList[nobleIndex];
    String nobleHash = GameScreen.getNobleHash(nobleOfInterest);
    Pair<Headers, String> serverResponse = PromptsRequests.claimNoble(sessionId,
        authToken, nobleHash);

    PromptComponent.closePrompts();
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  @Override
  protected ArrayList<Texture> getChoiceNobles() {
    // Hard Coded for now
    ArrayList<Texture> myList = Arrays.stream(nobleList).map(noble ->
        FXGL.texture(noble.getCardInfo().texturePath() + ".png"))
        .collect(Collectors.toCollection(ArrayList::new));
    return myList;
  }
}
