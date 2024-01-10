package oop;

import javafx.application.Application;
import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;

import java.util.Map;

import static java.lang.Thread.sleep;

public class World {
    public static void main(String[] args){
        int geneSize = 3;
        DarwinMap darwinMap = new DarwinMap(10, 10, 10, 1,2, 2);
//        Simulation simulation = new Simulation(10,darwinMap);
        //simulation.moveAnimals();
//        darwinMap.getMostPopularGeneType();
        guiLaunch();
//        for(int i=0; i<10; i++){
//            simulation.moveAnimals();
//            try {
//                sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
    public static void guiLaunch(){
        Application.launch(SimulationApp.class);
    }
}
