package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.Noble;
import javafx.util.Pair;
import kong.unirest.core.Headers;


/**
 * Class responsible for populating Reserving Noble prompt.
 */
public class ChooseNobleReserve extends ChooseNoble {


  @Override
  protected String promptText() {
    return "Choose Noble To Reserve";
  }

  @Override
  protected void handleConfirmation() {
    int nobleIndex = chosenVisitableIndex;
    long sessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    Noble nobleOfInterest = nobleList[nobleIndex];
    String nobleHash = GameScreen.getNobleHash(nobleOfInterest);
    Pair<Headers, String> serverResponse = PromptsRequests.reserveNoble(sessionId,
            authToken, nobleHash);

    PromptComponent.closePrompts();
    PromptUtils.actionResponseSpawner(serverResponse);

  }
}
