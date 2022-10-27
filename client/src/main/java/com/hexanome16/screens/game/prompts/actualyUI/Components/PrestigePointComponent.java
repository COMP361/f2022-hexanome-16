package org.actualyUI.Components;

import com.almasb.fxgl.entity.component.Component;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PrestigePointComponent extends Component {
   int aPrestige;
   GemComponent aGemComponent;
  double aWidth;
  double aHeight;

  @Override
  public void onAdded() {

    aWidth= aGemComponent.aWidth;
    aHeight= aGemComponent.aHeight/5;

    aPrestige = ThreadLocalRandom.current().nextInt(0, 5 + 1);
    Node myNode = new Rectangle(aWidth,aHeight, Color.LIGHTGRAY);
    myNode.setOpacity(0.4);

    Text NumberPrestigePoints = new Text();
    NumberPrestigePoints.textProperty().set(String.valueOf(aPrestige));
    NumberPrestigePoints.setTranslateY(aHeight);
    NumberPrestigePoints.setTranslateX(10);
    NumberPrestigePoints.setFont(Font.font("ComicSans", FontWeight.EXTRA_BOLD,aHeight));

    getEntity().getViewComponent().addChild(myNode);
    if (aPrestige!=0) getEntity().getViewComponent().addChild(NumberPrestigePoints);
  }

  public double getHeight() {
    return aHeight;
  }

  public double getWidth() {
    return aWidth;
  }
}
