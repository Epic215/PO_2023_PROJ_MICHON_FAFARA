package oop.presenter;

import com.opencsv.CSVWriter;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class SimulationView implements MapChangeListener{

    @FXML private GridPane mapGridd;
    @FXML private GridPane container;
    @FXML private Label animalCount;
    @FXML private Label plantsCount;
    @FXML private Label emptyCount;
    @FXML private Text geneType;
    @FXML private Label averageEnergy;
    @FXML private Label averageLifespan;
    @FXML private Label childrenCount;
    @FXML private VBox animalList;
    private AbstractWorldMap map;
    private Simulation engine;
    private UUID animalFollowedId;
    private int flag = 0;
    private boolean showEquatorStatus = false;
    File statistictFile = new File(Paths.get(".\\").toAbsolutePath().getParent().toString() + "\\src\\main\\resources\\statistictFile.csv");

    @Override
    public void mapChanged(AbstractWorldMap worldMap, String message) {
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            try {
                drawMap();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
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
    public void drawMap() throws URISyntaxException {
        clearGrid();
        showEquatorStatus = false;
        newGrid();
    }
    private void newGrid() {
        int left = map.getCurrentBounds().bottomLeft().getX();
        int upper = map.getCurrentBounds().upperRight().getY()-1;
        double containerWidth = container.getWidth();
        double containerHeight = container.getHeight();
        double width = min(0.65*containerWidth/map.getCurrentBounds().upperRight().getX()*0.9, containerHeight/map.getCurrentBounds().upperRight().getY()*0.9);
        double height = min(0.65*containerWidth/map.getCurrentBounds().upperRight().getX()*0.9, containerHeight/map.getCurrentBounds().upperRight().getY()*0.9);
        geneType.setWrappingWidth(containerWidth/7);
        animalCount.setText(String.valueOf(map.getAnimalCount()));
        plantsCount.setText(String.valueOf(map.getGrassCount()));
        emptyCount.setText(String.valueOf(map.getFreeFieldsCount()));
        geneType.setText(map.getMostPopularGeneType());
        averageEnergy.setText(String.valueOf(map.getAverageEnergy()));
        averageLifespan.setText(String.valueOf(map.getAverageDeadAge()));
        childrenCount.setText(String.valueOf(map.getAverageChildCount()));
        if(engine.getSaveToCsv()){
            saveToCsv(map.getAnimalCount(),map.getGrassCount(),map.getFreeFieldsCount(),map.getMostPopularGeneType(), map.getAverageEnergy(), map.getAverageDeadAge(), map.getAverageChildCount());
        }

        Label label;

        for (int i = 0; i < map.getCurrentBounds().upperRight().getX() ; i++) {
            this.mapGridd.getColumnConstraints().add(new ColumnConstraints(width));

        }
        boolean[] equator=map.getEquator();
        for (int i = 0; i < map.getCurrentBounds().upperRight().getY() ; i++) {
            this.mapGridd.getRowConstraints().add(new RowConstraints(height));
            if (showEquatorStatus) {
                for (int j = 0; j < map.getCurrentBounds().upperRight().getY(); j++) {
                    label = new Label(" ");
                    label.setMinWidth(width*0.9);
                    label.setMinHeight(height*0.9);
                    if (equator[i]) {
                        label.setStyle("-fx-background-color: #d8e3ac; -fx-border-radius: 100px ");
                    } else {
                        label.setStyle("-fx-background-color: #efd7be; -fx-border-radius: 100px ");
                    }
                    label.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(label, HPos.CENTER);
                    this.mapGridd.add(label, j, i);
                }
            }
        }
        mapGridd.setGridLinesVisible(false);

        Map<Vector2d, ArrayList<Animal>> animals = map.getAnimals2();
        Map<Vector2d, Grass> grasses = map.getGrasses();
        for (Map.Entry<Vector2d,Grass> grass : grasses.entrySet()) {
            label = new Label();
            label.setMinWidth(width*0.8);
            label.setMinHeight(height*0.8);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            label.setText(grass.getValue().toString());
            label.setStyle("-fx-background-color: #77c44c; -fx-border-radius: 100px");

            mapGridd.add(label,  grass.getKey().getX() ,   upper-grass.getKey().getY());
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
                int animalEnergy = engine.resolveConflictFirstStrongest(animal.getValue()).getEnergy();
                int maxAnimalEnergy = engine.getInitialEnergy();
                String styles = null;
                if(animalEnergy>0 && animalEnergy<=maxAnimalEnergy*0.2){
                    styles = "-fx-background-color: #e5b678; -fx-border-radius: 100px ";
                }else if(animalEnergy>maxAnimalEnergy*0.2 && animalEnergy<=maxAnimalEnergy*0.4){
                    styles = "-fx-background-color: #cc9c5f; -fx-border-radius: 100px ";
                }else if(animalEnergy>maxAnimalEnergy*0.4 && animalEnergy<=maxAnimalEnergy*0.6){
                    styles = "-fx-background-color: #e08f4c; -fx-border-radius: 100px ";
                }else if(animalEnergy>maxAnimalEnergy*0.6 && animalEnergy<=maxAnimalEnergy*0.8){
                    styles = "-fx-background-color: #ea7232; -fx-border-radius: 100px ";
                }else if(animalEnergy>maxAnimalEnergy*0.8 && animalEnergy<=maxAnimalEnergy){
                    styles = "-fx-background-color: #ec6332; -fx-border-radius: 100px ";
                }
                label.setStyle(styles);
                final Boolean[] isMostPopularHere = {false};
                animal.getValue().forEach(value -> {
                    if(value.ifGeneMostPopular(map.getMostPopularGeneType())){
                        isMostPopularHere[0] =true;
                    }
                });
                if(isMostPopularHere[0]){
                    label.setStyle("-fx-background-color: #f1cc38; -fx-border-radius: 100px ");
                }
            }
            label.setOnMouseClicked(event -> {
                Label animalLabel = new Label();
                animalLabel.setText("Animals on position " + animal.getKey().toString());
                animalList.getChildren().clear();
                animalList.getChildren().add(animalLabel);
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
            mapGridd.add(label,animal.getKey().getX(), upper-animal.getKey().getY());
        }

        if(map.getIsWater()){
            drawWater(width, height, left, upper);
        }
    }

    private void saveToCsv(int animalCount,int grassCount,int freeFieldsCount,String mostPopularGeneType, int averageEnergy, int averageDeadAge, int averageChildCount){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(statistictFile,true));

            String[] header = { "animalCount", "grassCount", "freeFieldsCount", "mostPopularGeneType", "averageEnergy", "averageDeadAge", "averageChildCount"};
            writer.write(Arrays.toString(header) + "\n");

            String[] data = { String.valueOf(animalCount), String.valueOf(grassCount), String.valueOf(freeFieldsCount), mostPopularGeneType.replaceAll(",",""), String.valueOf(averageEnergy), String.valueOf(averageDeadAge), String.valueOf(averageChildCount)};
            writer.write(Arrays.toString(data) + "\n");

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
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
            mapGridd.add(label,water.getKey().getX(), upper - water.getKey().getY());
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
        animalList.getChildren().clear();
        Button showEquator = new Button();
        showEquator.setText("Show preferred grass fields");
        showEquator.setOnAction(value -> {
            if(!showEquatorStatus){
                showEquatorStatus = true;
                clearGrid();
                newGrid();
            } else {
                showEquatorStatus = false;
                clearGrid();
                newGrid();
            }
        });
        animalList.getChildren().add(showEquator);
    }
    @FXML
    public void viewResume() {
        animalList.getChildren().clear();
        engine.resume();
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