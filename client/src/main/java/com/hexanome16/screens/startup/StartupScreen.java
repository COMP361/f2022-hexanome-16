package com.hexanome16.screens.startup;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;


public class StartupScreen implements EntityFactory {
    @Spawns("mainscreen")
    public Entity mainScreen(SpawnData data) {
        var main_screen = FXGL.texture("splendor_main_screen.jpg");
        return FXGL.entityBuilder(data)
                .view(main_screen)
                .type(EntityType.STARTUP)
                .build();
    }

    @Spawns("diamond")
    public Entity diamond(SpawnData data) {
        FXGLButton button = createButton();
        return FXGL.entityBuilder(data)
                .view(button)
                .type(EntityType.STARTUP)
                .build();
    }

    @Spawns("message")
    public Entity text(SpawnData data) {
        Text message = createMessage("Click diamond to enter the game!");
        return FXGL.entityBuilder(data)
                .view(message)
                .type(EntityType.STARTUP)
                .type(EntityType.MESSAGE)
                .build();
    }

    public static void backToMainScreen() {
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.LOGIN));
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.STARTUP));
        spawn("mainscreen", 0, 0);
        spawn("diamond", 730, 360);
        spawn("message", 370, 985);
    }

    private FXGLButton createButton() {
        FXGLButton button = new FXGLButton();
        button.setOnMouseClicked(e -> {
            spawnLoginScreen();
        });
        button.setPrefSize(500, 260);
        button.setStyle("-fx-background-color: ffffff00;" );
        return button;
    }

    private Text createMessage(String text) {
        Text message = new Text(text);
        message.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 115));
        message.setFill(Paint.valueOf("#FCD828"));
        message.setStrokeWidth(2.);
        message.setStroke(Paint.valueOf("#936D35"));
        message.setStyle("-fx-background-color: ffffff00; ");
        return message;
    }

    private void spawnLoginScreen() {
        getGameWorld().removeEntities(getGameWorld().getEntitiesByType(EntityType.MESSAGE));
        spawn("loginscreen", 630, 320);
        spawn("login", 880, 420);

        spawn("usertext", 830, 525);
        spawn("username", 1000, 505);
        spawn("passtext", 830, 575);
        spawn("password", 1000, 550);

        spawn("loginbutton", 1020, 635);
        spawn("cancelbutton", 820, 635);
    }

}