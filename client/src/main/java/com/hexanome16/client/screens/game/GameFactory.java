package com.hexanome16.client.screens.game;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.components.CardComponent;
import com.hexanome16.client.screens.game.components.NobleComponent;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.actualyUI.OpenPromt;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * FXGL factory for the game board.
 */
public class GameFactory implements EntityFactory {
  public static final int matCoordsX = 400;
  public static final int matCoordsY = 150;
  private final String levelOne = "level_one";
  private final String levelTwo = "level_two";
  private final String levelThree = "level_three";

  /**
   * Adds a level-one card from the level-one deck to the game board.

   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelOneCard")
  public Entity newLevelOneCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(levelOne + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.ONE, levelOne + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  /**
   * Adds a level-two card from the level-two deck to the game board.

   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelTwoCard")
  public Entity newLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(levelTwo + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.TWO, levelTwo + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  /**
   * Adds a level-three card from the level-three deck to the game board.

   * @param data spawn data
   * @return card entity
   */
  @Spawns("LevelThreeCard")
  public Entity newLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(levelThree + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.THREE, levelThree + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  /**
   * Adds a sacrifice card to the game board.

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
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.BUY_CARDS_BY_CARDS);
        })
        .build();
  }

  /**
   * Adds a noble-reserve card to the game board.

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
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.BUY_RESERVE_NOBLE_CARD);
        })
        .build();
  }

  /**
   * Adds a bag card to the game board.

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
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.BUY_BAG_CARD);
        })
        .build();
  }

  /**
   * Adds the level-three deck to the game board.

   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelThreeDeck")
  public Entity levelThree(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_three_quantity").asString());
    StackPane myStackPane = new StackPane();
    Texture level3deck = FXGL.texture("level_three.png");
    myStackPane.getChildren().addAll(level3deck, myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the level-two deck to the game board.

   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelTwoDeck")
  public Entity levelTwo(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_two_quantity").asString());
    StackPane myStackPane = new StackPane();
    Texture level2deck = FXGL.texture("level_two.png");
    myStackPane.getChildren().addAll(level2deck, myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the level-one deck to the game board.

   * @param data spawn data
   * @return deck entity
   */
  @Spawns("LevelOneDeck")
  public Entity levelOne(SpawnData data) {
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_one_quantity").asString());
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("level_one.png");
    myStackPane.getChildren().addAll(level1deck, myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the game bank to the game board.

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

    addToken(tokens, "ruby.png", 3);
    addToken(tokens, "emerald.png", 3);
    addToken(tokens, "sapphire.png", 2);
    addToken(tokens, "diamond.png", 3);
    addToken(tokens, "onyx.png", 4);
    addToken(tokens, "gold.png", 4);

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
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.TOKEN_ACQUIRING);
        })
        .build();
  }

  private void addToken(TilePane tokens, String textureName, int amount) {
    // token (image)
    Texture token = FXGL.texture(textureName);
    token.setFitHeight(75);
    token.setFitWidth(75);
    // multiplicity (text)
    Text number = new Text(Integer.toString(amount));
    number.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 50));
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
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.PAUSE);
        })
        .build();
  }

  /**
   * Adds a new noble tile to the game board.

   * @param data spawn data
   * @return noble entity
   */
  @Spawns("Noble")
  public Entity newNoble(SpawnData data) {
    return FXGL.entityBuilder()
        .view("noble" + data.getData().get("nobleIndex") + ".png")
        .with(new NobleComponent())
        .scale(0.15, 0.15)
        .build();
  }

  /**
   * Adds the game mat to the game board.

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
    RUBY,
    EMERALD,
    SAPPHIRE,
    DIAMOND,
    ONYX,
    GOLD;

    private Color getColor() {
      if (this == RUBY) {
        return Color.RED;
      } else if (this == EMERALD) {
        return Color.GREEN;
      } else if (this == SAPPHIRE) {
        return Color.BLUE;
      } else if (this == DIAMOND) {
        return Color.WHITE;
      } else if (this == ONYX) {
        return Color.BLACK;
      }
      return Color.GOLD;
    }

    public Color getStrokeColor() {
      if (this == ONYX) {
        return Color.WHITE;
      }
      return Color.BLACK;
    }
  }

}