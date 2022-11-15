package com.hexanome16.client.screens.game.players;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import com.hexanome16.client.screens.game.prompts.actualyUI.Components.PromptTypeInterface;
import com.hexanome16.client.screens.game.prompts.actualyUI.OpenPromt;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DeckFactory implements EntityFactory {

  // (helper) return a pane with a card and a label
  private StackPane getCard(int multiplicity, String cardName) {
    // current card
    Texture card = FXGL.texture(cardName);
    // current multiplicity
    Text number = new Text(Integer.toString(multiplicity));
    number.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 300));
    number.setFill(Paint.valueOf("#FCD828"));
    number.setStrokeWidth(2.);
    number.setStroke(Paint.valueOf("#936D35"));
    number.setStyle("-fx-background-color: ffffff00; ");
    // pane
    StackPane pane = new StackPane();
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setAlignment(number, Pos.TOP_RIGHT);
    pane.getChildren().addAll(card, number);
    // return it
    return pane;
  }

    @Spawns("Player")
  public Entity player(SpawnData data) {
    // player icon
    Texture icon = FXGL.texture("playericon.png");
    // current player's name TODO make this a variable
    Text playerName = new Text("Placeholder");
    playerName.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 100));
    playerName.setFill(Paint.valueOf("#FFFFFF"));
    playerName.setStrokeWidth(2.);
    playerName.setStroke(Paint.valueOf("#000000"));
    playerName.setStyle("-fx-background-color: ffffff00; ");
    // current player's prestige points TODO make this a variable
    Text prestigePoints = new Text("10");
    prestigePoints.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 100));
    prestigePoints.setFill(Paint.valueOf("#FFFFFF"));
    prestigePoints.setStrokeWidth(2.);
    prestigePoints.setStroke(Paint.valueOf("#000000"));
    prestigePoints.setStyle("-fx-background-color: ffffff00; ");
    // pane
    BorderPane pane = new BorderPane();
    BorderPane.setAlignment(prestigePoints, Pos.TOP_RIGHT);
    BorderPane.setAlignment(playerName, Pos.CENTER);
    pane.setTop(prestigePoints);
    pane.setBottom(playerName);
    pane.setCenter(icon);
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.2, 0.2)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("RedCard")
  public Entity redCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "redcard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("GreenCard")
  public Entity greenCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "greencard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("BlueCard")
  public Entity blueCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "bluecard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("WhiteCard")
  public Entity whiteCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "whitecard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("BlackCard")
  public Entity blackCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(2, "blackcard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("GoldCard")
  public Entity goldCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "goldcard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.25, 0.25)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    @Spawns("NobleCard")
  public Entity nobleCard(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "noblecard.png");
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.2, 0.2)
        .onClick(e -> {
          OpenPromt.openPrompt(PromptTypeInterface.PromptType.SEE_CARDS);
        })
        .build();
  }

    // this code is reused from peini's part
  @Spawns("PlayerTokens")
  public Entity playerTokens(SpawnData data) {
    StackPane mytokens = new StackPane();
    Rectangle myRectangle = new Rectangle(110, 160, Color.GREY);
    myRectangle.setOpacity(0.5);
    TilePane tokens = new TilePane();

    tokens.setHgap(5);
    tokens.setVgap(5);
    tokens.setAlignment(Pos.CENTER);
    tokens.setPrefColumns(2);
    tokens.setPrefRows(3);
    tokens.setPrefSize(110, 160);

    addToken(tokens, "ruby.png", 1);
    addToken(tokens, "emerald.png", 0);
    addToken(tokens, "sapphire.png", 0);
    addToken(tokens, "diamond.png", 2);
    addToken(tokens, "onyx.png", 0);
    addToken(tokens, "gold.png", 0);

    mytokens.getChildren().addAll(myRectangle, tokens);

    mytokens.setOnMouseEntered(e -> {
      myRectangle.setOpacity(0.7);
    });
    mytokens.setOnMouseExited(e -> {
          myRectangle.setOpacity(0.5);
        }
    );//.at(getAppWidth()- 210, 10 )
    return FXGL.entityBuilder(data)
        .view(mytokens)
        .build();
  }

  private void addToken(TilePane tokens, String textureName, int amount) {
    // token (image)
    Texture token = FXGL.texture(textureName);
    token.setFitHeight(45);
    token.setFitWidth(45);
    // multiplicity (text)
    Text number = new Text(Integer.toString(amount));
    number.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 28));
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


    @Spawns("ReservedNobles")
  public Entity reservedNobles(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "noblecard.png");
    pane.setOpacity(0.5);
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.1, 0.1)
        .build();
  }

    @Spawns("ReservedCards")
  public Entity reservedCards(SpawnData data) {
    // get a pane for this card
    StackPane pane = getCard(0, "card.png");
    pane.setOpacity(0.5);
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.07, 0.07)
        .build();
  }

    @Spawns("PlayersTurn")
  public Entity playersTurn(SpawnData data) {
    // current player's name TODO make this a variable
    Text text = new Text("Placeholder \n is playing");
    text.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 100));
    text.setFill(Paint.valueOf("#FFFFFF"));
    text.setStrokeWidth(2.);
    text.setStroke(Paint.valueOf("#000000"));
    text.setStyle("-fx-background-color: ffffff00; ");
    // pane
    StackPane pane = new StackPane();
    pane.getChildren().addAll(text);
    // build the entity
    return FXGL.entityBuilder(data)
        .view(pane)
        .scale(0.5, 0.5)
        .build();
  }

}
