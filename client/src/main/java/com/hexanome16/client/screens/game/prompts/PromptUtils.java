package com.hexanome16.client.screens.game.prompts;

import static com.hexanome16.client.requests.RequestClient.objectMapper;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseCity;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNoble;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNobleReserve;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenAcquiringOne;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenDiscard;
import com.hexanome16.common.models.City;
import com.hexanome16.common.models.Level;
import com.hexanome16.common.models.Noble;
import com.hexanome16.common.util.CustomHttpResponses;
import java.util.List;
import javafx.util.Pair;
import kong.unirest.core.Headers;
import lombok.SneakyThrows;

/**
 * Class containing 2 methods to open prompts.
 */
public class PromptUtils {

  /**
   * Opens prompt of Type promptType.
   *
   * @param promptType Type of prompt to open.
   */
  public static void openPrompt(PromptTypeInterface.PromptType promptType) {
    FXGL.spawn("PromptBox", new SpawnData().put("promptType", promptType));
  }

  /**
   * Opens Buy prompt with the entity as the view of the card to be bought.
   * To be reworked.
   *
   * @param entity The card entity to be displayed.
   */
  public static void openPrompt(Entity entity) {
    FXGL.spawn("PromptBox", new SpawnData().put("entity", entity));
  }

  /**
   * Opens Deck Reserve prompt.
   *
   * @param level The deck level.
   */
  public static void openPrompt(Level level) {
    FXGL.spawn("PromptBox", new SpawnData().put("level", level));
  }


  // TODO : ADD ALL OTHER ACTION TYPES AND THEIR ACCORDING PROMPTS.

  /**
   * Takes in a response from the server and spawns appropriate prompts.
   *
   * @param serverResponse response from server.
   */
  @SneakyThrows
  public static void actionResponseSpawner(Pair<Headers, String> serverResponse) {
    Headers headers = serverResponse.getKey();

    // TODO :: FIND A FIX FOR THIS, once card is replaced, the card that comes
    // after doesnt seem to have a card bonus type. (check for null shouldnt be
    // necessary)
    if (headers == null) {
      System.out.println("WOW this is no good, response headers was null :(");
      return;
    }

    List<String> actionList = headers.get(CustomHttpResponses.ActionType.ACTION_TYPE);
    if (actionList.size() >= 1) {
      if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.LEVEL_TWO.getMessage())) {
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_LEVEL_TWO));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.LEVEL_ONE.getMessage())) {
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_LEVEL_ONE));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.NOBLE.getMessage())) {
        Noble[] nobles = objectMapper.readValue(serverResponse.getValue(), Noble[].class);
        ChooseNoble.setNobleList(nobles);
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_NOBLES));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
              .equals(CustomHttpResponses.ActionType.NOBLE_RESERVE.getMessage())) {
        Noble[] nobles = objectMapper.readValue(serverResponse.getValue(), Noble[].class);
        ChooseNobleReserve.setNobleList(nobles);
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType",
                PromptTypeInterface.PromptType.CHOOSE_NOBLE_TO_RESERVE));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.CITY.getMessage())) {
        City[] cities = objectMapper.readValue(serverResponse.getValue(), City[].class);
        ChooseCity.setCityList(cities);
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.CHOOSE_CITY));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.DISCARD.getMessage())) {
        String[] tokens = objectMapper.readValue(serverResponse.getValue(), String[].class);
        TokenDiscard.setPossibleBonuses(tokens);
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.TOKEN_DISCARD));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.ASSOCIATE_BAG.getMessage())) {
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.ASSOCIATE_BAG_CARD));
      } else if (headers.get(CustomHttpResponses.ActionType.ACTION_TYPE).get(0)
          .equals(CustomHttpResponses.ActionType.TAKE.getMessage())) {
        String bonus = serverResponse.getValue();
        TokenAcquiringOne.setBonus(bonus);
        FXGL.spawn("PromptBox",
            new SpawnData().put("promptType", PromptTypeInterface.PromptType.TOKEN_ACQUIRING_ONE));
      }
    }
  }
}
