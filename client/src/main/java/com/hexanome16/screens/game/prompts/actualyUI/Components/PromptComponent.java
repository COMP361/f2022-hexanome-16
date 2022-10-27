package org.actualyUI.Components;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.actualyUI.PromptTypes.PromptTypeInterface;

public class PromptComponent extends Component {
  PromptTypeInterface aPromptType;
  double aWidth;
  double aHeight;

  double topleftX;
  double topleftY;


  public PromptComponent(PromptTypeInterface pPromptType) {
    aPromptType = pPromptType;
    aWidth = pPromptType.width();
    aHeight = pPromptType.height();
    topleftX = getAppWidth()/2 - (aWidth/2);
    topleftY = getAppHeight()/2- (aHeight/2);
  }

  @Override
  public void onAdded() {
    buildBox(10);
    aPromptType.populatePrompt(entity);
    buildButton(30,15,5);
  }

  private void buildBox(double pOutterSpace) {
    //initiate elements
    Rectangle myRectangle=new Rectangle(getAppWidth(),getAppHeight(),Color.GREY);
    Node myPromptOuter = new Rectangle(aWidth+pOutterSpace,aHeight+pOutterSpace, Color.rgb(249,161,89));
    Node myPrompt = new Rectangle(aWidth,aHeight, Color.rgb(78,147,180));

    //customizing Component/////////////////////////////////
    myRectangle.setOpacity(0.5);
    /////////
    myPromptOuter.setTranslateX(topleftX-(pOutterSpace/2));
    myPromptOuter.setTranslateY(topleftY-(pOutterSpace/2));
    /////////
    myPrompt.setTranslateX(topleftX);
    myPrompt.setTranslateY(topleftY);
    ////////////////////////////////////////////////////////

    //adding elements
    entity.getViewComponent().addChild(myRectangle);
    entity.getViewComponent().addChild(myPromptOuter);
    entity.getViewComponent().addChild(myPrompt);

  }

  private void buildButton(double pButtonWidth, double pFontSize, double pButtonAddedHeight) {
    //initiate elements
    Text mybutton = new Text("X");
    Node myBBox = new Rectangle(pButtonWidth,pFontSize+pButtonAddedHeight,Color.RED);


    //customizing Component : Text
    mybutton.setFont((new Font(pFontSize)));
    mybutton.setTextAlignment(TextAlignment.CENTER);
    mybutton.setWrappingWidth(pButtonWidth);
    mybutton.setTranslateX(topleftX+aWidth-pButtonWidth);
    mybutton.setTranslateY(topleftY+pFontSize);
    mybutton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
      closePrompt();
      e.consume();
    });

    //customizing Component : boundingbox
    myBBox.setOpacity(0);
    myBBox.setTranslateX(topleftX+aWidth-pButtonWidth);
    myBBox.setTranslateY(topleftY);
    myBBox.addEventHandler(MouseEvent.MOUSE_ENTERED,e->{
      myBBox.setOpacity(0.6);
    });
    myBBox.addEventHandler(MouseEvent.MOUSE_EXITED, e->{
      myBBox.setOpacity(0);
    });
    myBBox.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
      closePrompt();
      e.consume();
    });


    //adding elements
    entity.getViewComponent().addChild(mybutton);
    entity.getViewComponent().addChild(myBBox);
  }






  public static void closePrompt() {
    FXGL.getGameWorld().removeEntities(FXGL.getGameWorld().getEntitiesByComponent(PromptComponent.class));
  }
}
