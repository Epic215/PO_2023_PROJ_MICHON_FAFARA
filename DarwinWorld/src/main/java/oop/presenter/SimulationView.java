package oop.presenter;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import oop.Simulation;
import oop.SimulationEngine;
import oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

public class SimulationView implements MapChangeListener{

    @FXML
    private Button stopButton;
    @FXML private Label infoLabel;
    @FXML private GridPane mapGridd;
    @FXML private GridPane container;
    @FXML private Label animalCount;
    @FXML private Label plantsCount;
    @FXML private Label emptyCount;
    @FXML private Label geneType;
    @FXML private Label averageEnergy;
    @FXML private Label averageLifespan;
    @FXML private Label childrenCount;
//    @FXML private TextField animalCountInput;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private VBox animalList;
    private AbstractWorldMap map;
    private Simulation engine;
    private UUID animalFollowedId;
    private int flag = 0;

    @Override
    public void mapChanged(AbstractWorldMap worldMap, String message) {
        //synchronized (System.out){
//        System.out.println(Thread.currentThread());
        setWorldMap(worldMap);
        Platform.runLater(() -> {
//            infoLabel.setText(message);
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

        animalCount.setText(String.valueOf(map.getAnimalCount()));
        plantsCount.setText(String.valueOf(map.getGrassCount()));
        emptyCount.setText(String.valueOf(map.getFreeFieldsCount()));
        geneType.setText(String.valueOf(map.getMostPopularGeneType()));
        averageEnergy.setText(String.valueOf(map.getAverageEnergy()));
        averageLifespan.setText(String.valueOf(map.getAverageDeadAge()));
        childrenCount.setText(String.valueOf(map.getAverageChildCount()));

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

//        for(int i=0; i < map.getCurrentBounds().upperRight().getX(); i++){
//            for(int j=0; j <  map.getCurrentBounds().upperRight().getY(); j++){
//                Label newLabel = (Label) this.mapGridd.getChildren().get(i*map.getCurrentBounds().upperRight().getX()+j);
//            }
//        }
//        for (int i = 0; i < map.getCurrentBounds().upperRight().getX() ; i++) {
//            for (int j = 0; j < map.getCurrentBounds().upperRight().getY() ; j++){
//                Label label1 = new Label();
//                label1.setMinWidth(width*0.85);
//                label1.setMinHeight(height*0.85);
//                label1.setAlignment(Pos.CENTER);
//                GridPane.setHalignment(label1, HPos.CENTER);
//                label1.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 150px");
//                mapGridd.add(label1,i,j);
//            }
//        }
//        mapGridd.setStyle("-fx-background-color: #ffffff");
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
        Vector2d position = animalAnimalWithId(animalFollowedId).getPosition();
        Animal followedAnimal = animalAnimalWithId(animalFollowedId);
        animalChanged(followedAnimal);
        for (Map.Entry<Vector2d,ArrayList<Animal>> animal : animals.entrySet()){
            label = new Label();
            label.setMinWidth(width*0.8);
            label.setMinHeight(height*0.8);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            label.setText(""+animal.getValue().size());
            if(animal.getKey().equals(position)){
                label.setStyle("-fx-background-color: #ff57a3; -fx-border-radius: 100px ");
            } else {
                label.setStyle("-fx-background-color: #d79839; -fx-border-radius: 100px ");
            }
            label.setOnMouseClicked(event -> {
                Label animalLabel = new Label();
                animalLabel.setText("Animals on position " + animal.getKey().toString());
                animalList.getChildren().clear();
                animalList.getChildren().add(animalLabel);
//                animalList.setClip(new Rectangle(400,277));
                animal.getValue().forEach(element -> {
                    Button animalButton = new Button();
                    animalButton.setId(String.valueOf(element.getAnimalId()));
                    animalButton.setText("Animal " + String.valueOf(element.getAnimalId()));
                    animalButton.setOnAction(action -> {
                        animalFollowedId = UUID.fromString(animalButton.getId());
                        animalList.getChildren().clear();
                        animalChanged(element);
                        mapChanged(map,"message");
                    });
                    animalList.getChildren().add(animalButton);
                });
            });
            mapGridd.add(label,animal.getKey().getX()- left,upper - animal.getKey().getY());
        }

        if(map.getIsWater()){
            drawWater(width, height, left, upper);
        }
    }

    private void animalChanged(Animal element) {
        if(!element.getPosition().equals(new Vector2d(-1,-1))){
            flag = 1;
            animalList.getChildren().clear();
            Label animalStats = new Label();
            Label animalGene = new Label();
            Label animalEnergy = new Label();
            Label animalEatenPlants = new Label();
            Label animalChildrenCount = new Label();
            Label animalAge = new Label();
            animalStats.setText("Followed animal stats:");
            animalGene.setText("Gene: " + String.valueOf(element.getGene()));
            animalEnergy.setText("Energy: " + String.valueOf(element.getEnergy()));
            animalEatenPlants.setText("Eaten grass: " + String.valueOf(element.getEatenGrass()));
            animalChildrenCount.setText("Children: " + String.valueOf(element.getChildrenCount()));
            animalAge.setText("Age: " + String.valueOf(element.getAge()));
            animalList.getChildren().add(animalStats);
            animalList.getChildren().add(animalGene);
            animalList.getChildren().add(animalEnergy);
            animalList.getChildren().add(animalEatenPlants);
            animalList.getChildren().add(animalChildrenCount);
            animalList.getChildren().add(animalAge);
        }
        else if(flag==1){
            flag = 2;
            animalList.getChildren().clear();
            Label animalDeathDate = new Label();
            animalDeathDate.setText("Selected animal died on: " + String.valueOf(engine.getSimulationDay()) + " day:(");
            animalList.getChildren().add(animalDeathDate);
        }
    }

    private void drawWater(double width, double height, int left, int upper) {
        Label label;
        Map<Vector2d, Water> waters = ((DarwinMapWater) map).getWaters();
        for (Map.Entry<Vector2d,Water> water : waters.entrySet()){
            label = new Label();
            label.setMinWidth(width *0.8);
            label.setMinHeight(height *0.8);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            label.setText(water.getValue().toString());
            label.setStyle("-fx-background-color: #3b36da; -fx-border-radius: 100px ");
            mapGridd.add(label,water.getKey().getX()- left, upper - water.getKey().getY());
        }
    }
    private void clearGrid() {
        mapGridd.getChildren().retainAll(mapGridd.getChildren().get(0)); // hack to retain visible grid lines
        mapGridd.getColumnConstraints().clear();
        mapGridd.getRowConstraints().clear();
    }
    @FXML
    public void viewPause()  {
        engine.pause();

    }
    @FXML
    public void viewResume() {
        engine.resume();

    }
    @FXML
    public void viewStop() {
        engine.stop();

    }

    public void set(Simulation simulationEngine) {
        engine=simulationEngine;
    }
    private Animal animalAnimalWithId(UUID uuid){
        ArrayList<Animal> animals = map.getAnimals();
        final Animal[] searchedAnimal = {new Animal(new Vector2d(-1,-1),MapDirection.ERR,0,0)};
        animals.forEach(animal -> {
            if(animal.getAnimalId().equals(uuid)){
                searchedAnimal[0] = animal;
            }
        });
        return searchedAnimal[0];
    }
}