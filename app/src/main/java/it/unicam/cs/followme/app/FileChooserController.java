package it.unicam.cs.followme.app;

import it.unicam.cs.followme.controller.Controller;
import it.unicam.cs.followme.controller.SimulationMapBuilder;
import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FileChooserController{
    /**
     * il controller
     */
    private final Controller<ProgrammableEntity<Direction>, Shape> controller
            = Controller.getController();
    /**
     * il costruttore della mappa dei robot
     */
    private final SimulationMapBuilder mapBuilder = new SimulationMapBuilder();

    Alert a = new Alert(Alert.AlertType.NONE);

    private File programFile;

    private File environmentFile;

    @FXML
    private Button progButton;
    @FXML
    private Button envButton;
    @FXML
    private Button startButton;
    @FXML
    private Button openFile;
    @FXML
    private TextField nBotText;
    @FXML
    private Text textProg;
    @FXML
    private Text textEnv;


    @FXML
    public void selectProgFile(Event e){
        this.programFile=openFileChooser("Choose program file",e);
    }

    @FXML
    public void selectEnvFile(Event e){
        this.environmentFile=openFileChooser("Choose environment file", e);
    }

    @FXML
    public void startApplication(Event event) throws IOException {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent parent = loader.load();
            MainController cont = loader.getController();
            cont.setConfig(this.programFile,this.environmentFile,Integer.parseInt(this.nBotText.getText()));
            Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
            Scene scene = new Scene(parent,600,700);
            stage.setScene(scene);
            stage.setTitle("Follow Me");
            stage.setResizable(false);
            stage.show();
        }catch(FollowMeParserException e){
            alertError("PARSER EXCEPTION",e.getMessage());
        }catch(NumberFormatException e){
            alertError("ROBOT",e.getMessage());
        }
    }

    public void onBack(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent parent = loader.load();
        Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(parent,600,700);
        stage.setScene(scene);
        stage.setTitle("Follow Me");
        stage.setResizable(false);
        stage.show();

    }

    private File openFileChooser(String call,Event e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(call);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Txt Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());
    }

    private void alertError(String title,String message){
        a.setAlertType(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(message);
        a.show();
    }
}
