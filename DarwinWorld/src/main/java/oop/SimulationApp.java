package oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import oop.model.DarwinMap;
import oop.presenter.SimulationPresenter;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        var scene = new Scene(new Label("HELLO"));
//        primaryStage.setScene(scene);
//        primaryStage.show();
        FXMLLoader loader = new FXMLLoader();   //creates new fxml loader
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml")); //path to fxml file
        BorderPane viewRoot = loader.load();

        DarwinMap grassField = new DarwinMap(10, 10, 10, 1,2);

        SimulationPresenter presenter = loader.getController();
        presenter.setWorldMap(grassField);
        grassField.subscribe(presenter);

//        primaryStage.setScene(new Scene(viewRoot));
        configureStage(primaryStage,viewRoot);
        primaryStage.show();
    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        //properties
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}

