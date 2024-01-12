package oop.presenter;

import oop.Simulation;
import oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationView implements MapChangeListener{

    @FXML
    private Label infoLabel;
    @FXML
    private GridPane mapGridd;
    @FXML
    private GridPane container;
    private AbstractWorldMap map;



    @Override
    public void mapChanged(AbstractWorldMap worldMap, String message) {
        //synchronized (System.out){
//        System.out.println(Thread.currentThread());
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            infoLabel.setText(message);
            drawMap();
        });
    }
    public void setWorldMap(AbstractWorldMap map){
        this.map=map;
    }

    @FXML
    private void initialize() {

        mapGridd.setAlignment(Pos.CENTER);
    }

    @FXML
    public void drawMap() {
        clearGrid();
        newGrid();
    }

    private void newGrid(){
        int left = map.getCurrentBounds().bottomLeft().getX();
        int upper = map.getCurrentBounds().upperRight().getY()-1;
        double containerWidth = container.getWidth();
        double width = 0.65*containerWidth/map.getCurrentBounds().upperRight().getX()*0.9;
        double height = 0.65*containerWidth/map.getCurrentBounds().upperRight().getX()*0.9;

        Label label;

        for (int i = 0; i < map.getCurrentBounds().upperRight().getX() ; i++) {
            this.mapGridd.getColumnConstraints().add(new ColumnConstraints(width));
            label = new Label();
            GridPane.setHalignment(label, HPos.CENTER);
            this.mapGridd.add(label, i, 0);
        }
        for (int i = 0; i < map.getCurrentBounds().upperRight().getY() ; i++) {
            this.mapGridd.getRowConstraints().add(new RowConstraints(height));
            label = new Label();
            GridPane.setHalignment(label, HPos.CENTER);
            this.mapGridd.add(label, 0, i);
        }

        Map<Vector2d, ArrayList<Animal>> animals = map.getAnimals2();
        Map<Vector2d, Grass> grasses = map.getGrasses();
//        System.out.println(elements);
//        System.out.println(animals);
//        System.out.println(grasses);
        for (Map.Entry<Vector2d,Grass> grass : grasses.entrySet()) {
            label = new Label();
            label.setMinWidth(width*0.8);
            label.setMinHeight(height*0.8);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            label.setText(grass.getValue().toString());
            label.setStyle("-fx-background-color: #77c44c; -fx-border-radius: 100px");
            mapGridd.add(label,  grass.getKey().getX() - left,  upper - grass.getKey().getY());

        }
        for (Map.Entry<Vector2d,ArrayList<Animal>> animal : animals.entrySet()){
            label = new Label();
            label.setMinWidth(width*0.8);
            label.setMinHeight(height*0.8);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            label.setText(""+animal.getValue().size());
            label.setStyle("-fx-background-color: #d79839; -fx-border-radius: 100px ");
            mapGridd.add(label,animal.getKey().getX()- left,upper - animal.getKey().getY());
        }

    }

    private void clearGrid() {
        mapGridd.getChildren().retainAll(mapGridd.getChildren().get(0)); // hack to retain visible grid lines
        mapGridd.getColumnConstraints().clear();
        mapGridd.getRowConstraints().clear();
    }


}