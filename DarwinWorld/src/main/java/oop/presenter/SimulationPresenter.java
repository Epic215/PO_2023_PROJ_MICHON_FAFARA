package oop.presenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import oop.Simulation;
import oop.SimulationEngine;
import oop.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class SimulationPresenter{
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button startButton;

    @FXML
    private void initialize(){

    }

    public void onSimulationStartClicked() {
        startButton.setOnAction(event -> {
            DarwinMap darwinWorld = new DarwinMap(10, 10, 10, 1,2, 2);

            Simulation simulation = new Simulation(3,darwinWorld);

            FXMLLoader newwindow= new FXMLLoader(getClass().getResource("/windowsimulation.fxml"));
            BorderPane root = null;
            try {
                root = newwindow.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SimulationView newView = newwindow.getController();
            Stage stage = new Stage();
            stage.setTitle("Mapa: Teeeest");
            stage.setScene(new Scene(root));
            stage.show();

            darwinWorld.subscribe(newView);
            List<Simulation> simulationList = new ArrayList<>();
            simulationList.add(simulation);
            SimulationEngine simulationEngine = new SimulationEngine(simulationList);
            //run
            simulationEngine.runAsync();


//            fillGrid();
        });
    }
}
