package com.hexanome16.client.screens.game;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FontFactory;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.prompts.OpenPrompt;
import com.hexanome16.client.screens.game.prompts.components.PromptTypeInterface;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * FXGL factory for the game board.
 */
public class GameFactory implements EntityFactory {
  /**
   * The constant matCoordsX.
   */
  public static final int matCoordsX = 400;
  /**
   * The constant matCoordsY.
   */
  public static final int matCoordsY = 150;
  private static final FontFactory CURSIVE_FONT_FACTORY = FXGL.getAssetLoader()
      .loadFont("BrushScriptMT.ttf");
  private final String levelOne = "level_one";
  private final String levelTwo = "level_two";
  private final String levelThree = "level_three";

  /**
   * Adds a level-one card from the level-one deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelOneCard")
  public Entity newLevelOneCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.ONE,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a level-two card from the level-two deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelTwoCard")
  public Entity newLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.TWO,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a level-three card from the level-three deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelThreeCard")
  public Entity newLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.THREE,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a red level-three card from the red level-three deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("RedLevelThreeCard")
  public Entity newRedLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 155)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.REDTHREE,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a red level-two card from the red level-two deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("RedLevelTwoCard")
  public Entity newRedLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 360)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.REDTWO,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a red level-one card from the red level-one deck to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("RedLevelOneCard")
  public Entity newRedLevelOneCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 565)
        .view(data.getData().get("texture") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(((Double) data.getData().get("id")).longValue(), Level.REDONE,
            data.getData().get("texture") + ".png", (PriceMap) data.getData().get("price"),
            (String) data.getData().get("MD5")))
        .build();
  }

  /**
   * Adds a sacrifice card to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("SacrificeCard")
  public Entity newSacrificeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 710, matCoordsY + 155)
        .view("sacrificecard.png")
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(PromptTypeInterface.PromptType.BUY_CARD_WITH_CARDS);
        })
        .build();
  }

  /**
   * Adds a noble-reserve card to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("NobleReserveCard")
  public Entity newNobleReserveCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 710, matCoordsY + 360)
        .view("noblereserve.png")
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(PromptTypeInterface.PromptType.BUY_RESERVE_NOBLE_CARD);
        })
        .build();
  }

  /**
   * Adds a bag card to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("BagCard")
  public Entity newBagCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 710, matCoordsY + 565)
        .view("bagcard.png")
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(PromptTypeInterface.PromptType.BUY_BAG_CARD);
        })
        .build();
  }

  /**
   * Adds the level-three deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelThreeDeck")
  public Entity levelThree(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level3deck = FXGL.texture("level_three.png");
    myStackPane.getChildren().addAll(level3deck, myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(Level.THREE);
        })
        .build();
  }

  /**
   * Adds the level-two deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelTwoDeck")
  public Entity levelTwo(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level2deck = FXGL.texture("level_two.png");
    myStackPane.getChildren().addAll(level2deck, myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(Level.TWO);
        })
        .build();
  }

  /**
   * Adds the level-one deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelOneDeck")
  public Entity levelOne(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("level_one.png");
    myStackPane.getChildren().addAll(level1deck, myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .onClick(e -> {
          OpenPrompt.openPrompt(Level.ONE);
        })
        .build();
  }

  /**
   * Adds the red level-one deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("RedLevelOneDeck")
  public Entity redLevelOne(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("red_level_two.png");
    myStackPane.getChildren().addAll(level1deck, myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 565)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the red level-two deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("RedLevelTwoDeck")
  public Entity redLevelTwo(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("red_level_two.png");
    myStackPane.getChildren().addAll(level1deck, myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 360)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the red level-three deck to the game board.
   *
   * @param data spawn data
   * @return deck entity
   */
  @Spawns("RedLevelThreeDeck")
  public Entity redLevelThree(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("red_level_three.png");
    myStackPane.getChildren().addAll(level1deck, myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 985, matCoordsY + 155)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }


  /**
   * Adds the game bank to the game board.
   *
   * @param data spawn data
   * @return bank entity
   */
  @Spawns("TokenBank")
  public Entity tokenBank(SpawnData data) {
    Rectangle myRectangle = new Rectangle(266, 200, Color.GREY);
    myRectangle.setOpacity(0.5);
    TilePane tokens = new TilePane();

    tokens.setHgap(5);
    tokens.setVgap(5);
    tokens.setAlignment(Pos.CENTER);
    tokens.setPrefColumns(3);
    tokens.setPrefRows(2);
    tokens.setPrefSize(200, 150);

    addToken(CurrencyType.RED_TOKENS, tokens, "ruby.png");
    addToken(CurrencyType.RED_TOKENS, tokens, "emerald.png");
    addToken(CurrencyType.BLUE_TOKENS, tokens, "sapphire.png");
    addToken(CurrencyType.WHITE_TOKENS, tokens, "diamond.png");
    addToken(CurrencyType.BLACK_TOKENS, tokens, "onyx.png");
    addToken(CurrencyType.GOLD_TOKENS, tokens, "gold.png");

    StackPane mytokens = new StackPane();
    mytokens.getChildren().addAll(myRectangle, tokens);

    mytokens.setOnMouseEntered(e -> {
      myRectangle.setOpacity(0.7);
    });
    mytokens.setOnMouseExited(e -> {
          myRectangle.setOpacity(0.5);
        }
    );
    return FXGL.entityBuilder()
        .at(getAppWidth() - 280, 10)
        .view(mytokens)
        .onClick(e -> {
          OpenPrompt.openPrompt(PromptTypeInterface.PromptType.TOKEN_ACQUIRING);
        })
        .build();
  }

  private void addToken(CurrencyType currencyType, TilePane tokens,
                        String textureName) {
    // token (image)
    Texture token = FXGL.texture(textureName);
    token.setFitHeight(75);
    token.setFitWidth(75);
    // multiplicity (text)
    Text number = new Text();
    // Binds number to world property associated with the currency type in the bank and session.
    number.textProperty().bind(
        FXGL.getWorldProperties().intProperty(GameScreen.getSessionId() + currencyType.toString())
            .asString());

    number.setFont(CURSIVE_FONT_FACTORY.newFont(50));
    number.setFill(Paint.valueOf("#FFFFFF"));
    number.setStrokeWidth(2.);
    number.setStroke(Paint.valueOf("#000000"));
    number.setStyle("-fx-background-color: ffffff00; ");
    // pane
    StackPane myToken = new StackPane();
    StackPane.setAlignment(token, Pos.CENTER);
    StackPane.setAlignment(number, Pos.TOP_RIGHT);
    myToken.getChildren().addAll(token, number);
    tokens.getChildren().add(myToken);
  }

  /**
   * Adds the in-game menu button to the game board.
   *
   * @param data spawn data
   * @return icon entity
   */
  @Spawns("Setting")
  public Entity newSetting(SpawnData data) {
    StackPane stackPane = new StackPane();
    Texture texture = FXGL.texture("setting.png");
    stackPane.getChildren().add(texture);
    return FXGL.entityBuilder()
        .view(stackPane)
        .at(10, 10)
        .scale(0.1, 0.1)
        .onClick(e -> {
          OpenPrompt.openPrompt(PromptTypeInterface.PromptType.PAUSE);
        })
        .build();
  }

  /**
   * Adds a new noble tile to the game board.
   *
   * @param data spawn data
   * @return noble entity
   */
  @Spawns("Noble")
  public Entity newNoble(SpawnData data) {
    return FXGL.entityBuilder()
        .view(data.getData().get("texture") + ".png")
        .with(new NobleComponent())
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the game mat to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("Mat")
  public Entity buildMat(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX, matCoordsY)
        .view("mat.png")
        .scale(0.6, 0.6)
        .build();
  }

  /**
   * Adds the background to the game board.
   *
   * @param data spawn data
   * @return card entity
   */
  @Spawns("Background")
  public Entity buildBackground(SpawnData data) {
    return FXGL.entityBuilder()
        .at(0, 0)
        .view("background.png")
        .scale(1, 1)
        .build();
  }

  private enum CircleType {
    /**
     * Ruby circle type.
     */
    RUBY(Color.RED),
    /**
     * Emerald circle type.
     */
    EMERALD(Color.GREEN),
    /**
     * Sapphire circle type.
     */
    SAPPHIRE(Color.BLUE),
    /**
     * Diamond circle type.
     */
    DIAMOND(Color.WHITE),
    /**
     * Onyx circle type.
     */
    ONYX(Color.BLACK),
    /**
     * Gold circle type.
     */
    GOLD(Color.GOLD);

    private final Color color;

    CircleType(Color color) {
      this.color = color;
    }

    private Color getColor() {
      return this.color;
    }

    /**
     * Gets stroke color.
     *
     * @return the stroke color
     */
    public Color getStrokeColor() {
      if (this == ONYX) {
        return Color.WHITE;
      }
      return Color.BLACK;
    }
  }

}
