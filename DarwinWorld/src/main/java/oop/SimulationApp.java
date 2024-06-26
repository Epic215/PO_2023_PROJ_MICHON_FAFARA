package oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import oop.model.DarwinMap;
import oop.presenter.SimulationPresenter;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();   //creates new fxml loader
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml")); //path to fxml file
        GridPane viewRoot = loader.load();


        SimulationPresenter presenter = loader.getController();

        configureStage(primaryStage,viewRoot);

        presenter.onSimulationStartClicked();

        primaryStage.show();
        presenter.onWindowClose(primaryStage);
    }
    private void configureStage(Stage primaryStage, GridPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DarwinMap - settings");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}

