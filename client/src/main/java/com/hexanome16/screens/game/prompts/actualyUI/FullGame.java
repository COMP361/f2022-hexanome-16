package com.hexanome16.screens.game.prompts.actualyUI;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes.BuyCard;
import java.util.Map;

public class FullGame extends GameApplication {
  @Override
  protected void initSettings(GameSettings gameSettings) {
      gameSettings.setVersion("1");
      gameSettings.setTitle("Am struggling real hard rn");
      gameSettings.setWidth(1920);
      gameSettings.setHeight(1080);
  }



  @Override
  protected void initGame() {
    FXGL.getGameWorld().addEntityFactory(new FullGameFactory());
    Entity myBG = FXGL.spawn("Background");
    Entity myCard = FXGL.spawn("Card", new SpawnData().put("Color", GemEnum.RUBY));
    Entity myNobleChoice = FXGL.spawn("NobleChoice");
    Entity aHandofCards = FXGL.spawn("ViewHandColor");
    Entity myOwnReservedCards = FXGL.spawn("ViewReservedSelf");
    Entity OthersReservedCards = FXGL.spawn("ViewOtherSelf");
    Entity AssociateBuyCard = FXGL.spawn("BuyBagCard");
    Entity TakingTokens = FXGL.spawn("TakingTokens");
    Entity BuyCardByCard = FXGL.spawn("BuyCardByCard");
    Entity BuyNobleReserve = FXGL.spawn("BuyNobleReserve");
  }

  @Override
  protected void initGameVars(Map<String, Object> vars) {
    String playerbankPrefix = BuyCard.BankType.PLAYER_BANK.toString() +"/";
    String gameBankPrefix = BuyCard.BankType.GAME_BANK.toString() + "/";
    int i=0;
    for (BuyCard.CurrencyType e : BuyCard.CurrencyType.values()){
      String gameBank = gameBankPrefix+(e.toString());
      String playerBankKeys = playerbankPrefix + (e.toString());
      i++;
      vars.put(playerBankKeys, i);
      int value = 0;
      vars.put(gameBank,value);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
