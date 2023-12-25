package oop;

import oop.model.*;

import java.util.*;

public class Simulation{
    private int animalCount;
    private DarwinMap darwinMap;
    public Simulation(int animalCount, DarwinMap darwinMap){
        this.animalCount = animalCount;
        this.darwinMap = darwinMap;
        animalGenerator(animalCount);
    }
    private void animalGenerator(int n){
        Random randomPosition = new Random();
        Boundary mapBoundary = darwinMap.getCurrentBounds();
        int height;
        int width;
        int direction;
        for (int i=0; i<n;i++){
            height=randomPosition.nextInt(mapBoundary.upperRight().getX());
            width = randomPosition.nextInt(mapBoundary.upperRight().getY());
            direction = randomPosition.nextInt(8);
            darwinMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(direction),6));
        }
        darwinMap.printAnimals();
        darwinMap.printGrasses();
    }
    public void moveAnimals(){
        Map<Vector2d, ArrayList<Animal>> animals = darwinMap.getAnimals();
//        darwinMap.printGrasses();
        animals.forEach((key, value) -> {
            darwinMap.printAnimals();
//            System.out.println(value.toString());
            if(value.size() != 0){
                for(Animal animal : value){
                    if(animal != null){
                        System.out.println("przed move");
                        System.out.println(animal.toString());
                        darwinMap.move(animal);
                        System.out.println("przed move");
                        System.out.println(animal.toString());
                    }
                }
            }
        });
    }
    private void eatGrass(){

    }
    private void breedAnimals(){

    }
    private void deleteDead(){

    }
    public void run(){

    }
}
