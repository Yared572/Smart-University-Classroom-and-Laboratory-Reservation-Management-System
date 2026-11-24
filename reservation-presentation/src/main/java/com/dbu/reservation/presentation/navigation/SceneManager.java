package com.dbu.reservation.presentation.navigation;
import javafx.scene.Scene;import javafx.stage.Stage;
public final class SceneManager {private final Stage stage;public SceneManager(Stage stage){this.stage=stage;}public void show(Scene scene){stage.setScene(scene);stage.show();}}
