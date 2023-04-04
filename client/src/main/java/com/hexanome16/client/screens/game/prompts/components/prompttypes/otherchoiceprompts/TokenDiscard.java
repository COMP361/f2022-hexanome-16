package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.hexanome16.client.requests.backend.cards.GameRequest;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.UpdateGameInfo;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.utils.AuthUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Prompt for Discarding Tokens.
 */
public class TokenDiscard
    extends TokenAcquiringTwo {

  private static ArrayList<BonusType> possibleBonuses = new ArrayList<>();

  /**
   * public constructor.
   */
  public TokenDiscard() {
    atWidth = getAppWidth() / 1.5;
    atTopLeftX = (getAppWidth() / 2.) - (atWidth / 2.);
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
  protected void handleConfirmation() {
    long promptSessionId = GameScreen.getSessionId();
    String auth = AuthUtils.getAuth().getAccessToken();
    final Pair<Headers, String> serverResponse
        = PromptsRequests.discardOne(promptSessionId, auth, chosenBonus);
    PromptComponent.closePrompts();
    UpdateGameInfo.fetchGameBank(promptSessionId);
    UpdateGameInfo.fetchPlayerBank(promptSessionId, AuthUtils.getPlayer().getName(),
        false);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  @Override
  protected String promptText() {
    return "Choose Token To Discard";
  }

  /**
   * Sets the list of possible Bonuses.
   *
   * @param possibleBonuses list of possible bonuses.
   */
  public static void setPossibleBonuses(String[] possibleBonuses) {
    TokenDiscard.possibleBonuses =
        Arrays.stream(possibleBonuses).map(BonusType::fromStringDiscard)
            .collect(Collectors.toCollection(ArrayList::new));
  }


  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    return possibleBonuses;
  }
}
