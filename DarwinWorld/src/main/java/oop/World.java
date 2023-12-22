package oop;

import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;

public class World {

    public static void main(String[] args){
        DarwinMap darwinMap = new DarwinMap(10, 10, 0, 10, 10, 10, 10);
        Animal animal1 = new Animal(new Vector2d(2,2), MapDirection.NORTH, 10 ,10, 10);
        Animal animal2 = new Animal(new Vector2d(2,2), MapDirection.SOUTH, 10 ,10, 10);
        darwinMap.place(animal1);
        darwinMap.place(animal2);
        darwinMap.printAnimals();
    }
}
