package com.hexanome16.screens.game.prompts.actualyUI.Components.PromptTypes;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.hexanome16.screens.game.prompts.actualyUI.Components.PromptComponent;
import java.util.ArrayList;
import java.util.EnumMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AssociateBagCard implements PromptTypeInterface {
  public static enum BonusType{
    RED,GREEN,BLUE,WHITE,BLACK;

    public static EnumMap<BonusType,Color> m = new EnumMap<BonusType, Color>(BonusType.class);

    static {
      m.put(RED, Color.RED.darker());
      m.put(GREEN, Color.GREEN.darker());
      m.put(BLUE, Color.BLUE.darker());
      m.put(WHITE, Color.WHITE.darker());
      m.put(BLACK, Color.BLACK);
    }
    public Paint getColor(){
      return m.get(this);
    }
    public Paint getStrokeColor(){
      if (this == BLACK) return Color.GREY;
      return Color.BLACK;
    }
  }

  double aWidth = getAppWidth()/2;
  double aHeight = getAppHeight()/2;
  double aBonusWidth = aWidth/8;
  double aBonusHeight = aBonusWidth*1.39;
  double topleftX = (getAppWidth()/2)-(aWidth/2);
  double topleftY = (getAppHeight()/2) - (aHeight/2);
  ArrayList<Node> myNodes = new ArrayList<>();


  @Override
  public double width() {
    return aWidth;
  }

  @Override
  public double height() {
    return aHeight;
  }

  @Override
  public void populatePrompt(Entity entity) {
    double ConfirmButtonRadii = aHeight/10;

    //initiate BorderPane
    BorderPane myPrompt = new BorderPane();
    initiatePane(myPrompt);


    //initiate elements
    Text promptMessage = new Text();            //Top
    HBox bonuses = new HBox();                  //Nobles
    Circle confirmationButton = new Circle();   //Button
    Font RobotoBoldPrompt = FXGL.getAssetLoader().loadFont("Roboto-Bold.ttf").newFont(aHeight/6);
    Font RobotoBoldConfirm = FXGL.getAssetLoader().loadFont("Roboto-Bold.ttf").newFont(aHeight/20);

    // prompt message//
    HBox myhBox = new HBox();
    promptMessage.setText("Choose A Bonus Type");
    promptMessage.setFont(RobotoBoldPrompt);
    promptMessage.setTextAlignment(TextAlignment.CENTER);
    promptMessage.setWrappingWidth(aWidth);
    myhBox.setAlignment(Pos.CENTER);
    myhBox.getChildren().add(promptMessage);
    myhBox.setPrefSize(aWidth,aHeight/4);
    myPrompt.setTop(myhBox);

    // Confirm button//
    confirmationButton.setRadius(ConfirmButtonRadii);
    confirmationButton.setFill(Color.rgb(249,161,89));
    confirmationButton.setOpacity(0.5);

    /*Centering circle*/
    Text confirm = new Text("Confirm");
    confirm.setFont(RobotoBoldConfirm);
    StackPane button = new StackPane(confirmationButton,confirm);
    button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      if(confirmationButton.getOpacity() == 1) {
        myNodes = new ArrayList<>();
        PromptComponent.closePrompts();
        ////////Handle new Noble AGAIN///////////
      }
    } );
    ////////////////////////////////////////////////////


    HBox inter1=new HBox(button);
    inter1.setAlignment(Pos.CENTER);
    inter1.setPrefSize(ConfirmButtonRadii*4,aHeight);
    inter1.setSpacing(5);
    /*Circle in inter1*/
    myPrompt.setRight(inter1);
    ////////////////////////////////////////////////////////////////////////////////////////////////


    // Bonuses /////////////////////////////////////////////////////////////////////////////////////
    bonuses.setAlignment(Pos.CENTER);
    bonuses.setSpacing(aBonusWidth/4);
    addBonusType(bonuses,confirmationButton,BonusType.RED);
    addBonusType(bonuses,confirmationButton,BonusType.GREEN);
    addBonusType(bonuses,confirmationButton,BonusType.WHITE);
    addBonusType(bonuses,confirmationButton,BonusType.BLACK);


    myPrompt.setCenter(bonuses);
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //add elements to view
    entity.getViewComponent().addChild(myPrompt);
  }

  private void addBonusType(HBox pBonuses, Circle confirmationButton, BonusType pBonusType) {
    StackPane myBonus = new StackPane();
    Rectangle Bonus = new Rectangle(aBonusWidth,aBonusHeight,pBonusType.getColor());
    Bonus.setStrokeWidth(aHeight/100);
    Bonus.setStroke(pBonusType.getStrokeColor());
    Rectangle SelectionRectangle = new Rectangle(aBonusWidth*1.1,aBonusHeight*1.1,Color.WHITE);
    SelectionRectangle.setOpacity(0.5);
    myNodes.add(SelectionRectangle);


    myBonus.getChildren().addAll(SelectionRectangle,Bonus);

    myBonus.setOnMouseEntered(e -> {
      if (SelectionRectangle.getOpacity()!=1){
        SelectionRectangle.setOpacity(0.7);
      }
    });

    myBonus.setOnMouseExited(e -> {
      if (SelectionRectangle.getOpacity()!=1){
        SelectionRectangle.setOpacity(0.5);
      }
    });

    myBonus.setOnMouseClicked(e -> {
      for (Node n : myNodes){
        n.setOpacity(0.5);
      }
      SelectionRectangle.setOpacity(1);
      confirmationButton.setOpacity(1);
    });

    pBonuses.getChildren().add(myBonus);
  }

  private void initiatePane(Pane myPrompt) {
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    myPrompt.setPrefWidth(aWidth);
    myPrompt.setMaxWidth(aWidth);
    myPrompt.setPrefHeight(aHeight);
    myPrompt.setMaxHeight(aHeight);
  }
}
