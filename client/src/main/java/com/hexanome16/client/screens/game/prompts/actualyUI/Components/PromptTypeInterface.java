package com.hexanome16.client.screens.game.prompts.actualyUI.Components;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.AssociateBagCard;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.BuyCard;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.BuyCardByCard;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.BuyReservedNobleCard;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.BuyingBagCard;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.BuyingReserved;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.ChooseNoble;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.ChooseNobleReserve;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.Pause;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.SeeCards;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.SeeOtherReserved;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.SeeOwnReserved;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.TokenAcquiring;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.TokenAcquiringThree;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypes.TokenAcquiringTwo;
import java.util.EnumMap;

///Interface to implement if you want to add a Prompt
///need to also add it to enum and add it to the static bloc
public interface PromptTypeInterface {

  PromptTypeInterface nullPromptType = new PromptTypeInterface() {

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

  double width();

  double height();

  void populatePrompt(Entity entity);

  enum PromptType {
    TOKEN_ACQUIRING,          // Main
    BUY_CARDS,                // Main
    SEE_OTHER_RESERVED,       // Main
    SEE_OWN_RESERVED,         // Main
    SEE_CARDS,                // Main
    CHOOSE_NOBLES,            // Main
    BUY_CARDS_BY_CARDS,       // Main
    BUY_BAG_CARD,             // Main
    BUY_RESERVE_NOBLE_CARD,   // Main
    PAUSE,                    // Main
    BUYING_RESERVED,          // Helper
    TOKEN_ACQUIRING_TWO,      // Helper
    TOKEN_ACQUIRING_THREE,    // Helper
    ASSOCIATE_BAG_CARD,       // Helper
    CHOOSE_NOBLE_TO_RESERVE,  // Helper
    NULL;                     // NullObject

    static final EnumMap<PromptType, PromptTypeInterface> myMap = new EnumMap<>(PromptType.class);

    static {
      myMap.put(TOKEN_ACQUIRING, new TokenAcquiring());
      myMap.put(TOKEN_ACQUIRING_TWO, new TokenAcquiringTwo());
      myMap.put(TOKEN_ACQUIRING_THREE, new TokenAcquiringThree());
      myMap.put(BUY_CARDS, new BuyCard());
      myMap.put(SEE_OTHER_RESERVED, new SeeOtherReserved());
      myMap.put(SEE_OWN_RESERVED, new SeeOwnReserved());
      myMap.put(SEE_CARDS, new SeeCards());
      myMap.put(CHOOSE_NOBLES, new ChooseNoble());
      myMap.put(BUYING_RESERVED, new BuyingReserved());
      myMap.put(BUY_CARDS_BY_CARDS, new BuyCardByCard());
      myMap.put(ASSOCIATE_BAG_CARD, new AssociateBagCard());
      myMap.put(BUY_BAG_CARD, new BuyingBagCard());
      myMap.put(BUY_RESERVE_NOBLE_CARD, new BuyReservedNobleCard());
      myMap.put(CHOOSE_NOBLE_TO_RESERVE, new ChooseNobleReserve());
      myMap.put(PAUSE, new Pause());
      myMap.put(NULL, nullPromptType);
    }

    public PromptTypeInterface getAssociatedClass() {
      return myMap.get(this);
    }
  }
}
