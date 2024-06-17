package it.unicam.cs.followme.app;

import it.unicam.cs.followme.controller.Controller;
import it.unicam.cs.followme.controller.SimulationMapBuilder;
import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController implements Simulator {

    // servono per scorrere nel piano
    private double entropyX;
    private double entropyY;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Map<Label, ProgrammableEntity<Direction>> robotMap;
    private Map<Label,Shape> shapesMap;
    private int currentIteration; // variabile per la simulazione
    // parametri per la simulaizone automatica
    private final double dt_simulation = 0.5;
    private final double time_simulation = 10.0;

    private Timeline simulationTimeLine;
    Alert allert = new Alert(Alert.AlertType.NONE); // per la gestione/visualizzazione degli errori

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;

    @FXML
    private TextField tfCommandTime;

    @FXML
    private TextField tfSimulationTime;

    @FXML
    private Group cartesian;

    private final Controller<ProgrammableEntity<Direction>, Shape>
            controller =Controller.getController();

    public void initialize() {
        this.shapesMap = new HashMap<>();
        this.robotMap =new HashMap<>();
        this.entropyX=0;
        this.entropyY=0;
        cartesianPlane();
    }
    private void right(){
        double dist= xAxis.getUpperBound()-xAxis.getLowerBound();
        changeBounds(0.10*dist,0,0.10*dist,0);
        entropyX+=(0.10*dist);
        generateAll();
    }

    private void up(){
        double dist= yAxis.getUpperBound()-yAxis.getLowerBound();
        changeBounds(0,0.10*dist,0,0.10*dist);
        this.entropyY+=(0.10*dist);
        generateAll();
    }

    private void left(){
        double dist= xAxis.getUpperBound()-xAxis.getLowerBound();
        changeBounds(-0.10*dist,0,-0.10*dist,0);
        entropyX+=(-0.10*dist);
        generateAll();
    }

    private void down(){
        double dist= yAxis.getUpperBound()-yAxis.getLowerBound();
        changeBounds(0,-0.10*dist,0,-0.10*dist);
        this.entropyY+=(-0.10*dist);
        generateAll();
    }

    @FXML
    public void onNext(Event event){
        next();
    }

    @FXML
    public void onClear(Event event){
        clear();
    }

    @FXML
    public void onSettings(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FileSelection.fxml"));
        Parent parent = loader.load();
        FileChooserController cont = loader.getController();
        Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(parent,500,500);
        stage.setScene(scene);
        stage.setTitle("File Selection");
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    public void onDown(Event event){
        down();
    }
    @FXML
    public void onUp(Event event){
        up();
    }
    @FXML
    public void onLeft(Event event){
        left();
    }
    @FXML
    public void onRight(Event event){
        right();
    }
    @FXML
    public void onZoomIn(Event Event){
        if(this.xAxis.getUpperBound()-this.xAxis.getLowerBound()>20){
            changeBounds(+10,+10,-10,-10);
        }
        generateAll();
    }

    @FXML
    public void onZoomOut(Event Event){
        if(this.xAxis.getUpperBound()-this.xAxis.getLowerBound()<200){
            changeBounds(-10,-10,+10,+10);
        }
        generateAll();
    }

    @FXML
    public void onSimulation(ActionEvent event) {
            double commandTime = Double.parseDouble(tfCommandTime.getText());
            double simulationTime = Double.parseDouble(tfSimulationTime.getText());
            simulate(commandTime, simulationTime);
    }
    @FXML
    public void onDefaultSimulation(Event event) throws IOException{
        File env = new File ("src/main/resources/program/DefaultEnvironment");
        File prog = new File("src/main/resources/program/DefaultProgram");
        try{
            this.clear();
            this.setConfig(prog,env,5);
            generateAll();
            this.simulate(dt_simulation, time_simulation);
        }catch(FollowMeParserException e){
            alertError("PARSER EXCEPTION",e.getMessage());
        }catch(NumberFormatException e){
            alertError("ROBOT",e.getMessage());
        }
    }
    @Override
    public void simulate(double dt, double time) {
        // serve un cast perchè i passi da svolgere sono un numero intero
        int iteration = (int) (time / dt);
        // Crea un nuovo thread per eseguire la simulazione
        Thread simulationThread = new Thread(() -> {
            while (currentIteration < iteration) {
                Platform.runLater(this::next);
                currentIteration++;
                try {
                    Thread.sleep((long) (dt * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        simulationThread.start();
    }

    private void changeBounds(double lx,double ly,double ux,double uy){
        this.xAxis.setUpperBound(xAxis.getUpperBound()+ux);
        this.yAxis.setUpperBound(yAxis.getUpperBound()+uy);
        this.yAxis.setLowerBound(yAxis.getLowerBound()+ly);
        this.xAxis.setLowerBound(xAxis.getLowerBound()+lx);
    }

    public void setConfig(File prog, File env, int n) throws FollowMeParserException, IOException{
        SimulationMapBuilder builder=new SimulationMapBuilder();
        controller.createController(env,prog,builder.getMap(n));
        generateAll();
    }
    private void next(){
        this.controller.nextCommand();
        generateRobot();
    }

    /**
     * Genera il piano cartesiano
     */
    private void cartesianPlane(){
        xAxis=new NumberAxis(-10, 10, 1);
        xAxis.setSide(Side.BOTTOM);
        xAxis.setMinorTickVisible(false);
        xAxis.setPrefWidth(WIDTH);//larghezza dell'asse
        xAxis.setLayoutY((HEIGHT-100 )/ 2.0);
        yAxis = new NumberAxis(-10, 10, 1);
        yAxis.setSide(Side.LEFT);
        yAxis.setMinorTickVisible(false);
        yAxis.setPrefHeight(HEIGHT-100);// diminuisce l'altezza per via delle due Hbox
        yAxis.layoutXProperty().bind(
                Bindings.subtract(
                        ((WIDTH) / 2) + 1,
                        yAxis.widthProperty()
                )
        );
        cartesian.getChildren().addAll(xAxis,yAxis);
    }

    private void generateAll(){
        generateRobot();
        generateShapes();
    }
    private void generateRobot(){
        if(!this.robotMap.isEmpty())
            this.cartesian.getChildren().removeAll(this.robotMap.keySet());
        robotMap = this.controller.getProgrammableEntity().entrySet().stream().
                collect(Collectors.toMap(e->{Label l =new Label( e.getKey().getLabel(),
                                new Circle(5, Color.RED)
                        );
                            l.setLayoutX(mapX(e.getValue().getX()));
                            l.setLayoutY(mapY(e.getValue().getY()));
                            return l;
                        },
                        Map.Entry::getKey
                ));
        this.robotMap.keySet().forEach(e->{
            if(isInX(e.getLayoutX())&&isInY(e.getLayoutY()))
                this.cartesian.getChildren().add(e);
        });
    }

    private void generateShapes(){
        if(!this.shapesMap.isEmpty())
            this.cartesian.getChildren().removeAll(this.shapesMap.keySet());
        this.shapesMap =new HashMap<>();
        List<Map.Entry<Shape,Coordinate>> figures = this.controller.getShapes().entrySet().stream().toList();
        if(!figures.isEmpty())
            figures.forEach(e->{
                if(e.getKey().getType()==ShapeEnum.CIRCLE)
                    this.addCircle(e);
                else
                    this.addRectangle(e);
            });
    }
    private void addCircle(Map.Entry<Shape, Coordinate>e){
        it.unicam.cs.followme.model.Circle circle=(it.unicam.cs.followme.model.Circle)e.getKey();
        Circle c = new Circle
                (mapX(circle.getRadius())-mapX(0),Color.RED);
        c.setOpacity(0.5);
        Label label = new Label(circle.getLabel(),c);
        label.setLayoutX(mapX(e.getValue().getX())-c.getRadius());
        label.setLayoutY(mapY(e.getValue().getY())-c.getRadius()/1.35);
        this.shapesMap.put(label,circle);
        if(isInX(label.getLayoutX()-c.getRadius())&&isInX(c.getRadius()+label.getLayoutX())
                && isInY(label.getLayoutY()-c.getRadius())&&isInY(c.getRadius()+label.getLayoutY()))
            this.cartesian.getChildren().add(label);
    }

    private void addRectangle(Map.Entry<Shape,Coordinate> e){
        Rectangle rectangle=(Rectangle)e.getKey();
        javafx.scene.shape.Rectangle rectangle1 = new javafx.scene.shape.Rectangle
                (mapX(rectangle.getBase())-mapX(0),-mapY(rectangle.getHeight())+mapY(0),Color.GREEN);
        rectangle1.setOpacity(0.5);

        Label label = new Label(rectangle.getLabel(),rectangle1);
        label.setLayoutX(mapX(e.getValue().getX())-rectangle1.getWidth()/2);
        label.setLayoutY(mapY(e.getValue().getY())-rectangle1.getHeight()/2/1.35);
        this.shapesMap.put(label,rectangle);
        if(isInX(label.getLayoutX()-rectangle1.getWidth()/2)&&isInX(label.getLayoutX()+rectangle1.getWidth()/2)&&
                isInY(label.getLayoutY()-rectangle1.getHeight()/2)&&isInY(label.getLayoutY()+rectangle1.getHeight()/2))
            this.cartesian.getChildren().add(label);

    }
    private double mapX(double x) {
        double lx = xAxis.getPrefWidth() / 2;// prendo la larghezza e la divido così sono nel punto 0
        double tx = xAxis.getPrefWidth() /
                (xAxis.getUpperBound() -
                        xAxis.getLowerBound());
        // ritorno la x - l'entropia della x cioe' il fattore di spostamento orizzontale
        // lo moltiplico per quanto vale ogni tick + dove si trova l'origine
        return (x-entropyX )* tx + lx;
    }
    private double mapY(double y) {
        // prendo la larghezza e la divido così sono nel punto 0
        double ly = yAxis.getPrefHeight() / 2;
        double ty = yAxis.getPrefHeight() /
                (yAxis.getUpperBound() -
                        yAxis.getLowerBound());
        // ritorno la t - l'entropia della y cioe' il fattore di spostamento verticale
        // lo moltiplico per quanto vale ogni tick + dove si trova l'origine
        return -(y -this.entropyY )* ty + ly-7;
    }

    /**
     * Controlla se un punto si trova nel'ascissa
     * @param x la x del punto
     */
    private boolean isInX(double x){
        return x<mapX(xAxis.getUpperBound())
                && x>mapX(xAxis.getLowerBound());
    }

    /**
     * controlla se un punto si trova nell'asse Y
     * @param y la y del punto
     */
    private boolean isInY(double y){
        return y>mapY(yAxis.getUpperBound())+1
                && y<mapY(yAxis.getLowerBound())-10;
    }

    private void clear(){
        this.controller.clear();
        this.cartesian.getChildren().removeAll(this.cartesian.getChildren());
        currentIteration = 0;
        initialize();

    }

    /**
     * Mostra a video gli errori
     */
    private void alertError(String title,String message){
        allert.setAlertType(Alert.AlertType.ERROR);
        allert.setTitle(title);
        allert.setContentText(message);
        allert.show();
    }

}