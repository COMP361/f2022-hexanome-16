package com.hexanome16.screens.game.prompts.actualyUI.Components;

import com.almasb.fxgl.entity.component.Component;
import com.hexanome16.screens.game.prompts.actualyUI.GemEnum;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GemComponent extends Component {
  GemEnum aGem;
  double aHeight;
  double aWidth;


  public GemComponent(Object pGem, double pWidth,  double pHeight){

    aGem = (GemEnum) pGem;
    aHeight =pHeight;
    aWidth = pWidth;
  }

  @Override
  public void onAdded() {
    Node myNode = new Rectangle(aWidth,aHeight,aGem.getColor());
    myNode.setOpacity(0.8);
    getEntity().getViewComponent().addChild(myNode);
  }




}
