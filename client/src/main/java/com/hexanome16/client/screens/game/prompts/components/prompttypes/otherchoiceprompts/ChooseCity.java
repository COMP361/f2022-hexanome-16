package com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.requests.backend.prompts.PromptsRequests;
import com.hexanome16.client.screens.game.GameScreen;
import com.hexanome16.client.screens.game.prompts.PromptUtils;
import com.hexanome16.client.screens.game.prompts.components.PromptComponent;
import com.hexanome16.client.utils.AuthUtils;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Noble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import kong.unirest.core.Headers;

/**
 * Prompt for choose a city.
 */
public class ChooseCity extends VisitableChoiceAbstract {

  private static City[] cityList;

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
    return "Choose a City";
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
    int cityIndex = chosenVisitableIndex;
    long sessionId = GameScreen.getSessionId();
    String authToken = AuthUtils.getAuth().getAccessToken();
    City cityOfInterest = cityList[cityIndex];
    String cityHash = GameScreen.getCityHash(cityOfInterest);
    Pair<Headers, String> serverResponse = PromptsRequests.claimCity(sessionId,
        authToken, cityHash);

    PromptComponent.closePrompts();
    PromptUtils.actionResponseSpawner(serverResponse);
  }

  @Override
  protected ArrayList<Texture> getChoiceVisitables() {
    ArrayList<Texture> myList = Arrays.stream(cityList).map(city ->
            FXGL.texture(city.getCardInfo().texturePath() + ".png"))
        .collect(Collectors.toCollection(ArrayList::new));
    return myList;
  }

  /**
   * Set the static city list.
   *
   * @param cityList list of cities to show.
   */
  public static void setCityList(City[] cityList) {
    ChooseCity.cityList = cityList;
  }
}
