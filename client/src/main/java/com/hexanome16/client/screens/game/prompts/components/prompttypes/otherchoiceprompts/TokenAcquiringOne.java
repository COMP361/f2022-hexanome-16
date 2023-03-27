package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.UpdateGameInfo;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.utils.AuthUtils;
import java.util.ArrayList;
import java.util.Optional;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Similar as take two tokens but take one instead.
 */
public class TokenAcquiringOne extends TokenAcquiringTwo {
  private static Optional<BonusType> bonus = Optional.empty();

  /**
   * Set the gem that cannot be taken.
   *
   * @param gem (optional) the gem that cannot be taken.
   */
  public static void setBonus(String gem) {
    TokenAcquiringOne.bonus = Optional.ofNullable(BonusType.fromString(gem));
  }

  @Override
  protected String promptText() {
    return "Choose A Token Type";
  }

  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    long promptSessionId = GameScreen.getSessionId();
    ArrayList<BonusType> availableBonuses =
        PromptsRequests.getAvailableThreeBonuses(promptSessionId);
    if (bonus.isPresent()) {
      availableBonuses.remove(bonus.get());
    }
    return availableBonuses;
  }

  @Override
  protected void handleConfirmation() {
    // this is where you handle what to do with their choice, refer to chosenBonus
    long promptSessionId = GameScreen.getSessionId();
    String auth = AuthUtils.getAuth().getAccessToken();


    final Pair<Headers, String>
        serverResponse = PromptsRequests.takeOne(promptSessionId, auth, chosenBonus);

    PromptComponent.closePrompts();

    UpdateGameInfo.fetchGameBank(promptSessionId);
    UpdateGameInfo.fetchPlayerBank(promptSessionId, AuthUtils.getPlayer().getName(),
        false);

    PromptUtils.actionResponseSpawner(serverResponse);
  }
}
