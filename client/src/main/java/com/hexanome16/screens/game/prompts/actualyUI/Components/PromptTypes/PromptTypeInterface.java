package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

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
    TOKEN_ACQUIRING,          // Main
    TOKEN_ACQUIRING_TWO,      // Helper
    TOKEN_ACQUIRING_THREE,    // Helper
    BUY_CARDS,                // Main
    SEE_OTHER_RESERVED,       // Main
    SEE_OWN_RESERVED,         // Main
    SEE_CARDS,                // Main
    CHOOSE_NOBLES,            // Main
    BUYING_RESERVED,          // Helper
    BUY_CARDS_BY_CARDS,       // Main
    ASSOCIATE_BAG_CARD,       // Helper
    BUY_BAG_CARD,             // Main
    NULL;                     // NullObject

    static final EnumMap<PromptType,PromptTypeInterface> myMap = new EnumMap<>(PromptType.class);

    static {
      myMap.put(TOKEN_ACQUIRING,new TokenAcquiring());
      myMap.put(TOKEN_ACQUIRING_TWO,new TokenAcquiringTwo());
      myMap.put(TOKEN_ACQUIRING_THREE,new TokenAcquiringThree());
      myMap.put(BUY_CARDS, new BuyCard());
      myMap.put(SEE_OTHER_RESERVED,new SeeOtherReserved());
      myMap.put(SEE_OWN_RESERVED,new SeeOwnReserved());
      myMap.put(SEE_CARDS,new SeeCards());
      myMap.put(CHOOSE_NOBLES, new ChooseNoble());
      myMap.put(BUYING_RESERVED,new BuyingReserved());
      myMap.put(BUY_CARDS_BY_CARDS,new BuyCardByCard());
      myMap.put(ASSOCIATE_BAG_CARD,new AssociateBagCard());
      myMap.put(BUY_BAG_CARD,new BuyingBagCard());
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
