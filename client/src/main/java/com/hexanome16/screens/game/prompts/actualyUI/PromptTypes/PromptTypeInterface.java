package com.hexanome16.screens.game.prompts.actualyUI.PromptTypes;

import com.almasb.fxgl.entity.Entity;
import java.util.EnumMap;

///Interface to implement if you want to add a Prompt
///need to also add it to enum and add it to the static bloc
public interface PromptTypeInterface {

  public static PromptTypeInterface nullPromptType = new PromptTypeInterface() {

    @Override
    public double width() {
      return 100;
    }

    @Override
    public double height() {
      return 100;
    }

    @Override
    public void populatePrompt(Entity entity) {
    }
  };

  public static enum PromptType {
    TOKEN_ACQUIRING,
    BUY_CARDS,
    SEE_OTHER_CARDS,
    SEE_OWN_CARDS,
    CHOOSE_NOBLES,
    BUY_CARDS_BY_CARDS,
    ASSOCIATE_BAG_CARD,
    NULL;

    static final EnumMap<PromptType,PromptTypeInterface> myMap = new EnumMap<>(PromptType.class);

    static {
      myMap.put(TOKEN_ACQUIRING,nullPromptType);
      myMap.put(BUY_CARDS, new BuyCard());
      myMap.put(SEE_OTHER_CARDS,nullPromptType);
      myMap.put(SEE_OWN_CARDS,nullPromptType);
      myMap.put(CHOOSE_NOBLES, new ChooseNoble());
      myMap.put(BUY_CARDS_BY_CARDS,nullPromptType);
      myMap.put(ASSOCIATE_BAG_CARD,nullPromptType);
      myMap.put(NULL,nullPromptType);
    }

    public PromptTypeInterface getAssociatedClass(){
      return myMap.get(this);
    }
  }
  double width();
  double height();
  void populatePrompt(Entity entity);
}
