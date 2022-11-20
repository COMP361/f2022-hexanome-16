package com.hexanome16.client.screens.game.prompts.components;

import com.almasb.fxgl.entity.Entity;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardByCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.Pause;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.TokenAcquiring;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyReservedNobleCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyingBagCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyingReserved;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.AssociateBagCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNoble;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNobleReserve;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenAcquiringThree;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenAcquiringTwo;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts.SeeCards;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts.SeeOtherReserved;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts.SeeOwnReserved;
import java.util.EnumMap;
import java.util.function.Predicate;
import javafx.scene.Node;

/**
 * Interface to implement for prompts, contains an enum of all the
 * possible prompt types as well (add the new type to the enum and
 * enum map for it to be usable).
 * Also contains some useful methods.
 */
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

  /**
   * Getter method to access prompt width.
   *
   * @return The prompt's width.
   */
  double width();

  /**
   * Getter method to access prompt height.
   *
   * @return The prompt's height.
   */
  double height();

  /**
   * Method which populates the prompt with elements specific to the prompt type.
   *
   * @param entity entity associated to the prompt component that called this method.
   */
  void populatePrompt(Entity entity);

  /**
   * Changes the opacity of affected Node to upperOpacity when mouse enters hoveredOver,
   * changes the opacity of affected Node to lowerOpacity when mouse leaves hoveredOver.
   *
   * @param hoveredOver Node that the mouse is hovering Over.
   * @param affectedNode Node affected by the hover.
   * @param lowerOpacity Opacity of affectedNode after mouse stops hovering over hoveredNode.
   * @param upperOpacity Opacity of affectedNode after mouse starts hovering over hoveredNode.
   */
  static void setOnHoverEffectOpacity(Node hoveredOver, Node affectedNode,
                                      double lowerOpacity, double upperOpacity) {
    hoveredOver.setOnMouseEntered(e -> {
      affectedNode.setOpacity(upperOpacity);
    });
    hoveredOver.setOnMouseExited(e -> {
      affectedNode.setOpacity(lowerOpacity);
    });
  }

  /**
   * Alternative function to setOnHoverEffectOpacity, takes a condition before doing the changes.
   * Changes the opacity of affected Node to upperOpacity when mouse enters hoveredOver,
   * changes the opacity of affected Node to lowerOpacity when mouse leaves hoveredOver.
   *
   * @param hoveredOver Node that the mouse is hovering Over.
   * @param affectedNode Node affected by the hover.
   * @param lowerOpacity Opacity of affectedNode after mouse stops hovering over hoveredNode.
   * @param upperOpacity Opacity of affectedNode after mouse starts hovering over hoveredNode.
   * @param condition a Predicate object which makes the changes happen only if it is true.
   * @param toTest the Object to test for the condition
   */
  static void setOnHoverEffectOpacity(Node hoveredOver, Node affectedNode,
                                      double lowerOpacity, double upperOpacity,
                                      Predicate<Object> condition, Object toTest) {
    hoveredOver.setOnMouseEntered(e -> {
      if (condition.test(toTest)) {
        affectedNode.setOpacity(upperOpacity);
      }
    });
    hoveredOver.setOnMouseExited(e -> {
      if (condition.test(toTest)) {
        affectedNode.setOpacity(lowerOpacity);
      }
    });
  }

  /**
   * Enum of all the possible types of prompts.
   */
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
      myMap.put(BUY_CARDS, new BuyCardPrompt());
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

    /**
     * Returns the class associated with the implied argument.
     *
     * @return A flyweight-like instance of the class associated to that type.
     */
    public PromptTypeInterface getAssociatedClass() {
      return myMap.get(this);
    }
  }
}
