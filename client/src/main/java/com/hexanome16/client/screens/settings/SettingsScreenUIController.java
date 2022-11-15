package com.hexanome16.client.screens.settings;

import com.almasb.fxgl.ui.UIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

public class SettingsScreenUIController implements UIController {
  private final String[] languages = {"English", "French", "Spanish"};
  private final String[] resolutions =
      {"1280 x 1024", "1366 x 768", "1600 x 900", "1920 x 1080", "1920 x 1200", "2560 x 1440"};
  @FXML
  Button doneButton; //Package private to expose it to the SettingScreen
  @FXML
  private ChoiceBox<String> languageChoiceBox;
  @FXML
  private ChoiceBox<String> resolutionChoiceBox;
  @FXML
  private Slider musicSlider;
  @FXML
  private Slider sfxSlider;
  @FXML
  private Slider soundSlider;
  private int myMusic = 100, mySFX = 100, mySound = 100;

  @Override
  public void init() {
    initLanguageBox();
    initResolutionsBox();
    initSliders();
  }

  private void initLanguageBox() {
    languageChoiceBox.getItems().addAll(languages);
    languageChoiceBox.getSelectionModel().selectFirst();
  }

  private void initResolutionsBox() {
    resolutionChoiceBox.getItems().addAll(resolutions);
    resolutionChoiceBox.getSelectionModel().select(3);
  }

  private void initSliders() {
    soundSlider.adjustValue(mySound);
    musicSlider.adjustValue(myMusic);
    sfxSlider.adjustValue(mySFX);
    soundSlider.valueProperty()
        .addListener((observableValue, number, t1) -> mySound = (int) soundSlider.getValue());
    musicSlider.valueProperty()
        .addListener((observableValue, number, t1) -> myMusic = (int) musicSlider.getValue());
    sfxSlider.valueProperty()
        .addListener((observableValue, number, t1) -> mySFX = (int) sfxSlider.getValue());
  }
}
