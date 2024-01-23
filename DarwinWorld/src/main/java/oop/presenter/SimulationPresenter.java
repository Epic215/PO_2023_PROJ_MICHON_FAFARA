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
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Alert missingDataError = new Alert(Alert.AlertType.ERROR);
    private Alert incorrectDataError = new Alert(Alert.AlertType.ERROR);
    String RESOURCENAME = "settings.properties"; // could also be a constant
//    @FXML private AnchorPane anchorPane;

    @FXML
    private void initialize(){
        missingDataError.setContentText("Missing input data");
        incorrectDataError.setContentText("Incorrect data for minimum or maximum mutations");
        animalCountInput.setAlignment(Pos.CENTER);
        GridPane.setHalignment(animalCountInput, HPos.CENTER);
        initializeSettings();
        saveToCsv = true;
    }
    private void initializeSettings(){
        settings= new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream("settings.properties")) {
//            System.out.println(resourceStream);
            settings.load(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Boolean.parseBoolean(settings.getProperty("worldConfigGlobeTides")));
        worldConfigGlobeTides.setSelected(Boolean.parseBoolean(settings.getProperty("worldConfigGlobeTides")));
        animalCountInput.setText(settings.getProperty("animalCount"));
        geneSizeInput.setText(settings.getProperty("geneSize"));
        mapWidthInput.setText(settings.getProperty("mapWidth"));
        mapHeightInput.setText(settings.getProperty("mapHeight"));
        grassCountInput.setText(settings.getProperty("grassCount"));
        grassGrowthInput.setText(settings.getProperty("grassGrowth"));
        grassEnergyInput.setText(settings.getProperty("grassEnergy"));
        dailyEnergyInput.setText(settings.getProperty("dailyEnergy"));
        initialEnergyInput.setText(settings.getProperty("initialEnergy"));
        breedEnergyInput.setText(settings.getProperty("breedEnergy"));
        minimumMutationsInput.setText(settings.getProperty("minimumMutations"));
        maximumMutationsInput.setText(settings.getProperty("maximumMutations"));

    }

    private void createMap (int mapWidth, int mapHeight, int grassCount, int grassGrowth, int grassEnergy, int dailyEnergy, int minimumMutations, int maxiumMutations, int animalCount, int geneSize, int initialEnergy, int breedEnergy) throws IncorrectDataException{
        if(minimumMutations<0 || minimumMutations>maxiumMutations){
            incorrectDataError.show();
            throw new IncorrectDataException("Minimum mutations");
        }
        if(maxiumMutations<0){
            incorrectDataError.show();
            throw new IncorrectDataException("Maximum mutations");
        }
        AbstractWorldMap abstractWorldMap;
        if(worldConfigGlobeTides.isSelected()){
            abstractWorldMap = new DarwinMapWater(mapWidth, mapHeight, grassCount, grassGrowth, grassEnergy, dailyEnergy, minimumMutations, maxiumMutations);
        } else{
            abstractWorldMap = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth, grassEnergy, dailyEnergy, minimumMutations, maxiumMutations);
        }
        if(noConfig.isSelected()){
            saveToCsv = false;
        }

//            DarwinMap darwinWorld = new DarwinMap(mapWidth, mapHeight, grassCount, grassGrowth,grassEnergy, dailyEnergy);
        Simulation simulation = new Simulation(animalCount,abstractWorldMap, geneSize, initialEnergy, breedEnergy,saveToCsv);

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
    }


    public void onSimulationStartClicked() throws NumberFormatException {
        startButton.setOnAction(event -> {
            if(animalCountInput.getText().isEmpty() || geneSizeInput.getText().isEmpty() || mapWidthInput.getText().isEmpty() ||
                mapHeightInput.getText().isEmpty() || grassCountInput.getText().isEmpty() || grassGrowthInput.getText().isEmpty() ||
                dailyEnergyInput.getText().isEmpty() || initialEnergyInput.getText().isEmpty() || breedEnergyInput.getText().isEmpty() ||
                minimumMutationsInput.getText().isEmpty() || mapHeightInput.getText().isEmpty()){
                missingDataError.show();
                throw new NumberFormatException();
            }
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
            int maxiumMutations = Integer.parseInt(maximumMutationsInput.getText());
            //java.lang.NumberFormatException

            createMap(mapWidth, mapHeight, grassCount, grassGrowth, grassEnergy, dailyEnergy, minimumMutations, maxiumMutations, animalCount, geneSize, initialEnergy, breedEnergy);
//            fillGrid();
        });
    }

    public void onWindowClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(exit -> {

            PropertiesConfiguration conf;
            try {
                saveSettings();
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }


        });
    }

    private void saveSettings() throws ConfigurationException {
        PropertiesConfiguration conf;
        conf = new PropertiesConfiguration(Paths.get(".\\").toAbsolutePath().getParent().toString() + "\\src\\main\\resources\\settings.properties");
        conf.setProperty("worldConfigGlobeTides",worldConfigGlobeTides.isSelected());
        conf.setProperty("animalCount",animalCountInput.getText());
        conf.setProperty("geneSize", geneSizeInput.getText());
        conf.setProperty("mapWidth", mapWidthInput.getText());
        conf.setProperty("mapHeight", mapHeightInput.getText());
        conf.setProperty("grassCount", grassCountInput.getText());
        conf.setProperty("grassGrowth", grassGrowthInput.getText());
        conf.setProperty("grassEnergy", grassEnergyInput.getText());
        conf.setProperty("dailyEnergy", dailyEnergyInput.getText());
        conf.setProperty("initialEnergy", initialEnergyInput.getText());
        conf.setProperty("breedEnergy", breedEnergyInput.getText());
        conf.setProperty("minimumMutations", minimumMutationsInput.getText());
        conf.setProperty("maximumMutations", maximumMutationsInput.getText());


        conf.save();
    }
}
