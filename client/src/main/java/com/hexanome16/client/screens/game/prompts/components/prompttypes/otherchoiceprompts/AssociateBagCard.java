package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;


import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.UpdateGameInfo;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BonusType;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.price.Gem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Class responsible for populating Associate Bag prompt.
 */
public class AssociateBagCard extends BonusChoiceAbstract {

  private List<BonusType> possibleBonuses = new ArrayList<>();


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
    return "Choose bag content";
  }

  @Override
  protected double promptTextSize() {
    return getHeight() / 6.;
  }

  @Override
  protected boolean canConfirm() {
    return atConfirmCircle.getOpacity() == 1;
  }

  @Override
  protected void handleConfirmation() {
    long promptSessionId = GameScreen.getSessionId();
    String auth = AuthUtils.getAuth().getAccessToken();

    final Pair<Headers, String> serverResponse
        = PromptsRequests.associateBag(promptSessionId, auth, chosenBonus);

    PromptComponent.closePrompts();

    UpdateGameInfo.fetchGameBank(promptSessionId);
    UpdateGameInfo.fetchPlayerBank(promptSessionId, AuthUtils.getPlayer().getName(),
        false);
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  /**
   * sets possible bonuses.
   *
   * @param possibleBonuses possible bonuses as gem[].
   */
  public void setPossibleBonuses(String[] possibleBonuses) {
    this.possibleBonuses = Arrays.stream(possibleBonuses)
        .map(BonusType::fromString).toList();
  }


  @Override
  protected ArrayList<BonusType> getAvailableBonuses() {
    long sessionId = GameScreen.getSessionId();
    String auth = AuthUtils.getAuth().getAccessToken();
    String[] tokens = PromptsRequests.getPossibleBonuses(sessionId, auth);
    this.setPossibleBonuses(tokens);
    return new ArrayList<>(possibleBonuses);
  }
}
