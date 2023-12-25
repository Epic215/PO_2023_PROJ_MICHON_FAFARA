package oop;

import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;

import java.util.Map;

public class World {

    public static void main(String[] args){
        DarwinMap darwinMap = new DarwinMap(10, 10, 10, 10);
        Animal animal1 = new Animal(new Vector2d(2,2), MapDirection.NORTH);
        Animal animal2 = new Animal(new Vector2d(2,2), MapDirection.SOUTH);
        Animal animal3 = new Animal(new Vector2d(10,2), MapDirection.SOUTH);
        darwinMap.place(animal1);
        darwinMap.place(animal2);
        darwinMap.place(animal3);

        darwinMap.move(animal3, MapDirection.EAST);
        //println
        darwinMap.printAnimals();
//        darwinMap.printGrasses();
    }
    public void guiLaunch(){
//        Application.launch(SimulationApp.class);
    }
}
