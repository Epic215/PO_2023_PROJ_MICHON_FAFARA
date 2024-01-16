package oop.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import oop.Simulation;
import oop.SimulationEngine;
import oop.model.*;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class SimulationPresenter{
    @FXML public RadioButton worldConfigGlobe;
    @FXML public RadioButton worldConfigGlobeTides;
    @FXML private GridPane mapGrid;
    @FXML private Button startButton;
    @FXML private TextField animalCountInput;
    @FXML private TextField geneSizeInput;
    @FXML private TextField mapWidthInput;
    @FXML private TextField mapHeightInput;
    @FXML private TextField grassCountInput;
    @FXML private TextField grassGrowthInput;
    @FXML private TextField grassEnergyInput;
    @FXML private TextField dailyEnergyInput;
    @FXML private TextField initialEnergyInput;
    @FXML private TextField breedEnergyInput;
    @FXML private AnchorPane anchorPane;
    @FXML
    private void initialize(){
        animalCountInput.setAlignment(Pos.CENTER);
        GridPane.setHalignment(animalCountInput, HPos.CENTER);

    }

    public void onSimulationStartClicked() {
        startButton.setOnAction(event -> {
            int animalCount = Integer.parseInt(animalCountInput.getText());
            int geneSize = Integer.parseInt(geneSizeInput.getText());
            int mapWidth = Integer.parseInt(mapWidthInput.getText());
            int mapHeight = Integer.parseInt(mapHeightInput.getText());
            int grassCount = Integer.parseInt(grassCountInput.getText());
            int grassGrowth = Integer.parseInt(grassGrowthInput.getText());
            int grassEnergy = Integer.parseInt(grassEnergyInput.getText());
            int dailyEnergy = Integer.parseInt(dailyEnergyInput.getText());
            int initialEnergy = Integer.parseInt(initialEnergyInput.getText());
            int breedEnergy = Integer.parseInt(breedEnergyInput.getText());

            AbstractWorldMap abstractWorldMap;
            if(worldConfigGlobeTides.isSelected()){
                abstractWorldMap = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy);
            } else{
                abstractWorldMap = new DarwinMapWater(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy);
            }

//            DarwinMap darwinWorld = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy);
            Simulation simulation = new Simulation(animalCount,abstractWorldMap,geneSize,initialEnergy,breedEnergy);

            FXMLLoader newwindow= new FXMLLoader(getClass().getResource("/windowsimulation.fxml"));
            GridPane root = null;
            try {
                root = newwindow.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SimulationView newView = newwindow.getController();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Mapa: Teeeest");
            stage.setScene(new Scene(root));
            stage.show();

            abstractWorldMap.subscribe(newView);
            List<Simulation> simulationList = new ArrayList<>();
            simulationList.add(simulation);
            SimulationEngine simulationEngine = new SimulationEngine(simulationList);
            //run
            simulationEngine.runAsync();


//            fillGrid();
        });
    }
}
