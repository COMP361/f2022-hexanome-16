package com.hexanome16.client.screens.game.prompts.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.FontFactory;
import com.hexanome16.client.Config;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardPrompt;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.BuyCardWithCards;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.Pause;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.ReserveCardPrompt;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.TokenAcquiring;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyReserveNobleCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyingBagCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherbuyprompts.BuyingReserved;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.AssociateBagCard;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNoble;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.ChooseNobleReserve;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenAcquiringThree;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.otherchoiceprompts.TokenAcquiringTwo;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts.SeeCards;
import com.hexanome16.client.screens.game.prompts.components.prompttypes.viewprompts.SeeReserved;
import java.util.function.Predicate;
import javafx.scene.Node;

/**
 * Interface to implement for prompts, contains an enum of all the
 * possible prompt types as well (add the new type to the enum and
 * enum map for it to be usable).
 * Also contains some useful methods.
 */
public interface PromptTypeInterface {

  FontFactory GAME_FONT = Config.CURSIVE_FONT_FACTORY;

  /**
   * The constant nullPromptType.
   */
  PromptTypeInterface nullPromptType = new PromptTypeInterface() {

    @Override
    public double getWidth() {
      return 100;
    }

    @Override
    public double getHeight() {
      return 100;
    }

    @Override
    public void populatePrompt(Entity entity) {
    }
  };

  /**
   * Changes the opacity of affected Node to upperOpacity when mouse enters hoveredOver,
   * changes the opacity of affected Node to lowerOpacity when mouse leaves hoveredOver.
   *
   * @param hoveredOver  Node that the mouse is hovering Over.
   * @param affectedNode Node affected by the hover.
   * @param lowerOpacity Opacity of affectedNode after mouse stops hovering over hoveredNode.
   * @param upperOpacity Opacity of affectedNode after mouse starts hovering over hoveredNode.
   */
  static void setOnHoverEffectOpacity(Node hoveredOver, Node affectedNode,
                                      double lowerOpacity, double upperOpacity) {
    hoveredOver.setOnMouseEntered(e -> affectedNode.setOpacity(upperOpacity));
    hoveredOver.setOnMouseExited(e -> affectedNode.setOpacity(lowerOpacity));
  }

  /**
   * Alternative function to setOnHoverEffectOpacity, takes a condition before doing the changes.
   * Changes the opacity of affected Node to upperOpacity when mouse enters hoveredOver,
   * changes the opacity of affected Node to lowerOpacity when mouse leaves hoveredOver.
   *
   * @param hoveredOver  Node that the mouse is hovering Over.
   * @param affectedNode Node affected by the hover.
   * @param lowerOpacity Opacity of affectedNode after mouse stops hovering over hoveredNode.
   * @param upperOpacity Opacity of affectedNode after mouse starts hovering over hoveredNode.
   * @param condition    a Predicate object which makes the changes happen only if it is true.
   * @param toTest       the Object to test for the condition
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
   * Getter method to access prompt width.
   *
   * @return The prompt's width.
   */
  double getWidth();

  /**
   * Getter method to access prompt height.
   *
   * @return The prompt's height.
   */
  double getHeight();

  /**
   * Method which populates the prompt with elements specific to the prompt type.
   *
   * @param entity entity associated to the prompt component that called this method.
   */
  void populatePrompt(Entity entity);

  /**
   * Enum of all the possible types of prompts.
   */
  enum PromptType {
    /**
     * Token acquiring prompt type.
     * <p><i>Main</i></p>
     */
    TOKEN_ACQUIRING(new TokenAcquiring()),
    /**
     * Token acquiring two prompt type.
     * <p><i>Helper</i></p>
     */
    TOKEN_ACQUIRING_TWO(new TokenAcquiringTwo()),
    /**
     * Token acquiring three prompt type.
     * <p><i>Helper</i></p>
     */
    TOKEN_ACQUIRING_THREE(new TokenAcquiringThree()),
    /**
     * Buy cards prompt type.
     * <p><i>Main</i></p>
     */
    BUY_CARD(new BuyCardPrompt()),
    /**
     * Buy cards prompt type.
     * <p><i>Main</i></p>
     */
    RESERVE_CARD(new ReserveCardPrompt()),
    //    Note : Elea removed this
    //    /**
    //     * See other reserved prompt type.
    //     * <p><i>Main</i></p>
    //     */
    //    SEE_OTHER_RESERVED(new SeeOtherReserved()),
    //    /**
    //     * See own reserved prompt type.
    //     * <p><i>Main</i></p>
    //     */
    //    SEE_OWN_RESERVED(new SeeOwnReserved()),
    /**
     * See own reserved prompt type.
     * <p><i>Main</i></p>
     */
    SEE_RESERVED(new SeeReserved()),
    /**
     * See cards prompt type.
     * <p><i>Main</i></p>
     */
    SEE_CARDS(new SeeCards()),
    /**
     * Choose nobles prompt type.
     * <p><i>Main</i></p>
     */
    CHOOSE_NOBLES(new ChooseNoble()),
    /**
     * Buy cards by cards prompt type.
     * <p><i>Main</i></p>
     */
    BUY_CARD_WITH_CARDS(new BuyCardWithCards()),
    /**
     * Buy bag card prompt type.
     * <p><i>Main</i></p>
     */
    BUY_BAG_CARD(new BuyingBagCard()),
    /**
     * Buy reserve noble card prompt type.
     * <p><i>Main</i></p>
     */
    BUY_RESERVE_NOBLE_CARD(new BuyReserveNobleCard()),
    /**
     * Pause prompt type.
     * <p><i>Main</i></p>
     */
    PAUSE(new Pause()),
    /**
     * Buying reserved prompt type.
     * <p><i>Main</i></p>
     */
    BUYING_RESERVED(new BuyingReserved()),
    /**
     * Associate bag card prompt type.
     * <p><i>Helper</i></p>
     */
    ASSOCIATE_BAG_CARD(new AssociateBagCard()),
    /**
     * Choose noble to reserve prompt type.
     * <p><i>Helper</i></p>
     */
    CHOOSE_NOBLE_TO_RESERVE(new ChooseNobleReserve()),
    /**
     * Null prompt type.
     * <p><i>NullObject</i></p>
     */
    NULL(nullPromptType);

    private final PromptTypeInterface promptType;

    PromptType(PromptTypeInterface promptType) {
      this.promptType = promptType;
    }

    /**
     * Returns the class associated with the implied argument.
     *
     * @return A flyweight-like instance of the class associated to that type.
     */
    public PromptTypeInterface getAssociatedClass() {
      return this.promptType;
    }
  }
}
