package com.hexanome16;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


public class MainApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(1920);
        gameSettings.setHeight(1080);
        gameSettings.setTitle("Splendor");
    }
    // To Spawn Prompt, OpenPromt.openPrompt(PromptTypeInterface.PromptType.<AN_ENUM_ELEMENT_FROM_PROMPT_TYPE_INTERFACE>);
    // see all possible prompt types in the PromptTypeInterface, inside the inner enum in src/main/java/com/hexanome16/screens/game/prompts/actualyUI/Components/PromptTypes
    // Or look for PromptTypeInterface
    @Override
    protected void initGame() {
        backToMainScreen();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        super.initGameVars(vars);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void backToMainScreen() {
        getGameWorld().addEntityFactory(new StartupScreen());
        getGameWorld().addEntityFactory(new LoginScreen());
        spawn("mainscreen", 0, 0);
        spawn("diamond", 730, 360);
        spawn("message", 370, 985);

    }

}
