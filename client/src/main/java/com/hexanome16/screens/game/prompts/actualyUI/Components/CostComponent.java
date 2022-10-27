package org.actualyUI.Components;

import com.almasb.fxgl.entity.component.Component;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;

import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.actualyUI.GemEnum;

public class CostComponent extends Component {
  Map<GemEnum,Integer>  aCost = new HashMap<>();

  @Override
  public void onAdded() {
    init();
    VBox myBox = new VBox();
    Text myNodePrice;
    Node myNodeGem;
    HBox myHBox;

    for (GemEnum e: GemEnum.values()) {

      if (aCost.get(e)>0){
      myNodePrice= new Text(aCost.get(e).toString());
      myNodePrice.setTranslateX(5);
        myNodePrice.setTranslateY(-5);
      myNodePrice.setFont(Font.font("monospace", FontWeight.EXTRA_BOLD,10));
      myNodeGem = new Circle(10,10,5,e.getColor());
      myNodeGem.setTranslateX(10);
      myNodeGem.setTranslateY(-5);
      myHBox = new HBox(myNodePrice,myNodeGem);



      myBox.getChildren().add(myHBox);
      }

    }

    Rectangle myRectangle = (Rectangle) getEntity().getViewComponent().getChildren().get(0);

    Rectangle myPrestigeRectangle = (Rectangle) getEntity().getViewComponent().getChildren().get(1);
    double width = myRectangle.getWidth();
    double height = myRectangle.getHeight();
    double OtherHeight= getEntity().getComponent(PrestigePointComponent.class).aHeight;
    myBox.setPrefHeight(height-OtherHeight);
//    myBox.setMinHeight(height-OtherHeight);


    myBox.setPrefWidth(width);
//    myBox.setMinWidth(width);

    myBox.setAlignment(Pos.BOTTOM_LEFT);
    myBox.setTranslateY(getEntity().getComponent(PrestigePointComponent.class).aHeight);
    BackgroundFill myBGF =new BackgroundFill(Color.rgb(0,0,5,0.2).darker(),new CornerRadii(5),new Insets(0));
    Background myBG = new Background(myBGF);
    myBox.setBackground(myBG);
    myBox.setFillWidth(true);
    getEntity().getViewComponent().addChild(myBox);

  }

  public void init(){
    int i=1;
    for (GemEnum e: GemEnum.values()){
      aCost.put(e, i);
      i++;
    }
  }
}
