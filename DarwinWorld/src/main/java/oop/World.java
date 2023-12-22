package oop;

import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;

public class World {

    public static void main(String[] args){
//        Animal animal = new Animal(new Vector2d(1,1), MapDirection.NORTH, 7);
        DarwinMap darwinMap = new DarwinMap(10,10,10,1);

        darwinMap.place(new Animal(new Vector2d(1,1),MapDirection.NORTH));
        darwinMap.place(new Animal(new Vector2d(1,1),MapDirection.NORTH));
        darwinMap.place(new Animal(new Vector2d(1,2),MapDirection.NORTH));
        darwinMap.printAnimals();
        darwinMap.printGrasses();

    }
}
