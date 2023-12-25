package oop;

import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;

import java.util.Map;

public class World {

    public static void main(String[] args){
        int geneSize = 3;
        DarwinMap darwinMap = new DarwinMap(10, 10, 10, 1);
        Animal animal1 = new Animal(new Vector2d(2,2), MapDirection.NORTH, geneSize);
        Animal animal2 = new Animal(new Vector2d(2,2), MapDirection.SOUTH, geneSize);
        Animal animal3 = new Animal(new Vector2d(10,2), MapDirection.SOUTH, geneSize);
        animal1.printGene();
        animal2.printGene();
        animal3.printGene();
        darwinMap.place(animal1);
        darwinMap.place(animal2);
        darwinMap.place(animal3);

        Simulation simulation = new Simulation(3,darwinMap);
        simulation.moveAnimals();
//        darwinMap.move(animal3);
        //println
//        for (int i=0; i<geneSize; i++){
//            darwinMap.move(animal1);
//            darwinMap.printAnimals();
//        }
//        darwinMap.printAnimals();
//        darwinMap.printGrasses();
    }
    public void guiLaunch(){
//        Application.launch(SimulationApp.class);
    }
}
