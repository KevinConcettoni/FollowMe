package it.unicam.cs.followme.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(App.class.getResource("/fxml/Main.fxml")));
        primaryStage.setTitle("Follow Me");
        primaryStage.setScene(new Scene(root,600,700));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("images/Robot-icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
