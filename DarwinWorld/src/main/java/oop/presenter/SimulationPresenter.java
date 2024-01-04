package oop.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import oop.Simulation;
import oop.SimulationEngine;
import oop.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class SimulationPresenter implements MapChangeListener{
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button startButton;
    private AbstractWorldMap map;
    public void setWorldMap(AbstractWorldMap map){
        this.map = map;
//        ConsoleMapDisplay listener = new ConsoleMapDisplay();
//        map.subscribe(listener);
    }
    @FXML
    private void initialize(){
        startButton.setOnAction(event -> {
            Simulation simulation = new Simulation(3,this.map);

            map.subscribe(this);
            List<Simulation> simulationList = new ArrayList<>();
            simulationList.add(simulation);
            SimulationEngine simulationEngine = new SimulationEngine(simulationList);
            //run
            simulationEngine.runAsync();
            mapGrid.setMinSize(400, 400);
            mapChanged(map,"message");
//            fillGrid();
        });
    }
    private void fillGrid(){
        Boundary mapBoundary = map.getCurrentBounds();
        int upperX = mapBoundary.upperRight().getX();
        int upperY = mapBoundary.upperRight().getY();
        System.out.println(upperX);
        System.out.println(upperY);
//        mapGrid.getColumnConstraints().add(new ColumnConstraints(mapX));
//        mapGrid.getRowConstraints().add(new RowConstraints(mapY));
        Map<Vector2d, ArrayList<Animal>> animals = map.getAnimals2();
        Map<Vector2d, Grass> grasses = map.getGrasses();

        for(int col=0; col < upperX; col++){
            for(int row=0; row < upperY; row++){
                Label label = new Label();
                label.setMinWidth(50);
                label.setMinHeight(50);
                label.setAlignment(Pos.CENTER);
                Vector2d tempVector = new Vector2d(col,row);
                if(animals.get(tempVector) != null){
//                    int finalCol = col;
//                    int finalRow = row;
                    label.setStyle("-fx-border-radius: 100px; -fx-background-color: #3577a2");
//                    mapGrid.add(label, col, row);
                }else if(grasses.get(tempVector)!=null){
                    label.setText("*");
                    label.setStyle("-fx-background-color: #71b641; -fx-border-radius: 100px");
//                    mapGrid.add(label, col, row);
                }
                mapGrid.add(label, col, row);
                }
            }
        }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    public void drawMap(){
        clearGrid();
        fillGrid();
    }
    @Override
    public void mapChanged(AbstractWorldMap worldMap, String message) {
        setWorldMap(worldMap);
        Platform.runLater(() -> {
            drawMap();
        });
    }
}
