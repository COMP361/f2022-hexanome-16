package com.hexanome16.screens.game;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getip;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.box2d.dynamics.contacts.EdgeAndCircleContact;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.screens.game.components.CardComponent;
import com.hexanome16.screens.game.components.NobleComponent;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import com.hexanome16.screens.game.prompts.actualyUI.OpenPromt;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameFactory implements EntityFactory
{
  private String levelOne = "level_one";
  private String levelTwo = "level_two";
  private String levelThree = "level_three";

  public static final int matCoordsX = 400;

  public static final int matCoordsY = 150;

  @Spawns("LevelOneCard")
  public Entity newLevelOneCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(levelOne + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.ONE, levelOne + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  @Spawns("LevelTwoCard")
  public Entity newLevelTwoCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(levelTwo + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.TWO, levelTwo + data.getData().get("cardIndex") + ".png"))
        .build();
  }

  @Spawns("LevelThreeCard")
  public Entity newLevelThreeCard(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(levelThree + data.getData().get("cardIndex") + ".png")
        .scale(0.15, 0.15)
        .with(new CardComponent(Level.THREE, levelThree + data.getData().get("cardIndex") + ".png"))
        .build();
  }

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

  @Spawns("LevelThreeDeck")
  public Entity levelThree(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level3deck = FXGL.texture("level_three.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_three_quantity").asString());
    myStackPane.getChildren().addAll(level3deck,myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 155)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelTwoDeck")
  public Entity levelTwo(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level2deck = FXGL.texture("level_two.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_two_quantity").asString());

    myStackPane.getChildren().addAll(level2deck,myNumber);

    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 360)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("LevelOneDeck")
  public Entity levelOne(SpawnData data) {
    StackPane myStackPane = new StackPane();
    Texture level1deck = FXGL.texture("level_one.png");
    Text myNumber = new Text();
    myNumber.setFill(Color.WHITE);
    myNumber.setFont(Font.font(500));
    myNumber.textProperty().bind(getip("level_one_quantity").asString());
    myStackPane.getChildren().addAll(level1deck,myNumber);
    return FXGL.entityBuilder()
        .at(matCoordsX + 10, matCoordsY + 565)
        .view(myStackPane)
        .scale(0.15, 0.15)
        .build();
  }


  private enum CircleType{
    RUBY, EMERALD, SAPPHIRE, DIAMOND, ONYX, GOLD;
    private Color getColor(){
      if (this == RUBY){return Color.RED;}
      else if (this == EMERALD){ return Color.GREEN;}
      else if (this == SAPPHIRE) { return  Color.BLUE;}
      else if (this == DIAMOND) {return Color.WHITE;}
      else if (this == ONYX) {return Color.BLACK;}
      return Color.GOLD;
    }
    public Color getStrokeColor(){
      if (this == ONYX) return Color.WHITE;
      return Color.BLACK;
    }
  }
  @Spawns("TokenBank")
  public Entity tokenBank(SpawnData data) {
    StackPane mytokens = new StackPane();
    Rectangle myRectangle = new Rectangle(200,150,Color.GREY);
    myRectangle.setOpacity(0.5);
    TilePane tokens = new TilePane();

    tokens.setHgap(5);
    tokens.setVgap(5);
    tokens.setAlignment(Pos.CENTER);
    tokens.setPrefColumns(3);
    tokens.setPrefRows(2);
    tokens.setPrefSize(200,150);

    addCircle(tokens, CircleType.RUBY, 3);
    addCircle(tokens, CircleType.EMERALD, 3);
    addCircle(tokens, CircleType.SAPPHIRE, 2);
    addCircle(tokens, CircleType.DIAMOND, 3);
    addCircle(tokens, CircleType.ONYX, 4);
    addCircle(tokens, CircleType.GOLD, 4);


    mytokens.getChildren().addAll(myRectangle,tokens);
    mytokens.setOnMouseEntered(e -> {
      myRectangle.setOpacity(0.7);
    });
    mytokens.setOnMouseExited(e -> {
      myRectangle.setOpacity(0.5);
        }
    );
    return FXGL.entityBuilder()
        .at(getAppWidth()- 210, 10 )
        .view(mytokens)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.TOKEN_ACQUIRING);
        })
        .build();
  }

  private void addCircle(TilePane tokens, CircleType circletype, int amount) {
    StackPane myToken = new StackPane();
    Circle circle = new Circle(25);
    circle.setFill(circletype.getColor());
    circle.setStrokeWidth(5);
    circle.setStroke(circletype.getStrokeColor());
    Text number = new Text(Integer.toString(amount));
    number.setFont(Font.font(35));
    number.setFill(circletype.getStrokeColor());
    myToken.getChildren().addAll(circle,number);
    tokens.getChildren().add(myToken);
  }

  @Spawns("Setting")
  public Entity newSetting(SpawnData data) {
    StackPane stackPane = new StackPane();
    Texture texture = FXGL.texture("setting.png");
    stackPane.getChildren().add(texture);
    return FXGL.entityBuilder()
        .view(stackPane)
        .at(10,10)
        .scale(0.1, 0.1)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.PAUSE);
        })
        .build();
  }

  @Spawns("Noble")
  public Entity newNoble(SpawnData data) {
    return FXGL.entityBuilder()
        .view("noble" + data.getData().get("nobleIndex") + ".png")
        .with(new NobleComponent())
        .scale(0.15, 0.15)
        .build();
  }

  @Spawns("Mat")
  public Entity buildMat(SpawnData data) {
    return FXGL.entityBuilder()
        .at(matCoordsX, matCoordsY)
        .view("mat.png")
        .scale(0.6, 0.6)
        .build();
  }

  @Spawns("Background")
  public Entity buildBackground(SpawnData data) {
    return FXGL.entityBuilder()
        .at(0, 0)
        .view("background.png")
        .scale(1, 1)
        .build();
  }

}
