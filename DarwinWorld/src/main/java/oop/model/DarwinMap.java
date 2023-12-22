package oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DarwinMap {
    private Vector2d bottomLeft;
    private Vector2d upperRight;
    private int mapId;
    private int grassCount;
    private int grassGrowth;
    private int animalCount;
    private final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    public DarwinMap(int width, int height, int mapId, int grassCount, int energy, int grassGrowth, int animalCount){
        this.mapId = mapId;
        this.upperRight = new Vector2d(width,height);
        this.bottomLeft = new Vector2d(0,0);
        this.grassCount = grassCount;
        this.grassGrowth = grassGrowth;
        this.animalCount = animalCount;
        //generateGrass
        //generateWater
    }
    public void printAnimals(){
        animals.forEach((key, value) -> {
            System.out.println("Key=" + key + ", Value=" + value.toString());
        });
    }
    private int generateNumber(int min, int max){
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
    public void place(Animal animal) {
        if (animals.get(animal.getPosition())==null){
            ArrayList<Animal> field = new ArrayList<>();
            field.add(animal);
            animals.put(animal.getPosition(),field);
        }
        else
        {
            animals.get(animal.getPosition()).add(animal);
        }

    }
    public Boundary getCurrentBounds(){
        return new Boundary(bottomLeft,upperRight);
    }
    public int getMapId(){
        return this.mapId;
    }
}
