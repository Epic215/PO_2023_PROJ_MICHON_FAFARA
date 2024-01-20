package oop.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import oop.Simulation;
import oop.SimulationEngine;
import oop.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static java.lang.Math.abs;


public class SimulationPresenter{
    @FXML public RadioButton worldConfigGlobeTides;
    @FXML public RadioButton noConfig;
//    @FXML private GridPane mapGrid;
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
    @FXML private TextField minimumMutationsInput;
    @FXML private TextField maximumMutationsInput;
    private boolean saveToCsv;
    private Properties settings;
    String RESOURCENAME = "settings.properties"; // could also be a constant
//    @FXML private AnchorPane anchorPane;

    @FXML
    private void initialize(){
        animalCountInput.setAlignment(Pos.CENTER);
        GridPane.setHalignment(animalCountInput, HPos.CENTER);
        initializeSettings();
        saveToCsv = true;
    }
    private void initializeSettings(){
        settings= new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream(RESOURCENAME)) {
//            System.out.println(resourceStream);
            settings.load(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        worldConfigGlobeTides.setSelected(Boolean.parseBoolean("worldConfigGlobeTides"));

        animalCountInput.setText(settings.getProperty("geneSize"));
        geneSizeInput.setText(settings.getProperty("animalCount"));
        mapWidthInput.setText(settings.getProperty("mapWidth"));
        mapHeightInput.setText(settings.getProperty("mapHeight"));
        grassCountInput.setText(settings.getProperty("grassCount"));
        grassGrowthInput.setText(settings.getProperty("grassGrowth"));
        grassEnergyInput.setText(settings.getProperty("grassEnergy"));
        dailyEnergyInput.setText(settings.getProperty("dailyEnergy"));
        initialEnergyInput.setText(settings.getProperty("initialEnergy"));
        breedEnergyInput.setText(settings.getProperty("breedEnergy"));

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
            int minimumMutations = Integer.parseInt(minimumMutationsInput.getText());
            int maxiumEnergy = Integer.parseInt(maximumMutationsInput.getText());


            AbstractWorldMap abstractWorldMap;
            if(worldConfigGlobeTides.isSelected()){
                abstractWorldMap = new DarwinMapWater(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy, minimumMutations, maxiumEnergy);
            } else{
                abstractWorldMap = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy, minimumMutations, maxiumEnergy);
            }
            if(noConfig.isSelected()){
                saveToCsv = false;
            }

//            DarwinMap darwinWorld = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy);
            Simulation simulation = new Simulation(animalCount,abstractWorldMap,geneSize,initialEnergy,breedEnergy,saveToCsv);

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
            newView.set(simulation);
            //run
            simulationEngine.runAsync();
            stage.setOnCloseRequest(close ->{
                simulation.stop();
            });


//            fillGrid();
        });
    }

    public void onWindowClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(exit -> {
//            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            System.out.println(loader.getResourceAsStream(RESOURCENAME));
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//            System.out.println(new File(Objects.requireNonNull(classloader.getResource(RESOURCENAME)).getFile()));
//            try(FileOutputStream out = new FileOutputStream(String.valueOf(new File(Objects.requireNonNull(classloader.getResource(RESOURCENAME)).getFile())))) {
//
//            settings.setProperty("worldConfigGlobeTides", String.valueOf(worldConfigGlobeTides.isSelected()));
//            settings.setProperty("animalCount",animalCountInput.getText());
//            settings.setProperty("geneSize",geneSizeInput.getText());
//            settings.setProperty("mapWidth",mapWidthInput.getText());
//            settings.setProperty("mapHeight",mapHeightInput.getText());
//            settings.setProperty("grassCount",grassCountInput.getText());
//            settings.setProperty("grassGrowth",grassGrowthInput.getText());
//            settings.setProperty("grassEnergy",grassEnergyInput.getText());
//            settings.setProperty("dailyEnergy",dailyEnergyInput.getText());
//            settings.setProperty("initialEnergy",initialEnergyInput.getText());
//            settings.setProperty("breedEnergy",breedEnergyInput.getText());
//            settings.store(out,null);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
}
