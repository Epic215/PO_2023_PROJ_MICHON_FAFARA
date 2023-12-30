package oop;

import oop.model.*;

import java.util.*;

public class Simulation{
    private int animalCount;
    private AbstractWorldMap abstractWorldMap;
    public Simulation(int animalCount, AbstractWorldMap abstractWorldMap){
        this.animalCount = animalCount;
        this.abstractWorldMap = abstractWorldMap;
        animalGenerator(animalCount);
    }
    private void animalGenerator(int n){
        Random randomPosition = new Random();
        Boundary mapBoundary = abstractWorldMap.getCurrentBounds();
        int height;
        int width;
        int direction;
        for (int i=0; i<n;i++){
            height=randomPosition.nextInt(mapBoundary.upperRight().getX());
            width = randomPosition.nextInt(mapBoundary.upperRight().getY());
            direction = randomPosition.nextInt(8);
            abstractWorldMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(direction),6));
        }
        abstractWorldMap.printAnimals();
        abstractWorldMap.printGrasses();
    }
    public void moveAnimals(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals();
//        darwinMap.printGrasses();
        animals.forEach((key, value) -> {
            abstractWorldMap.printAnimals();
//            System.out.println(value.toString());
            if(!value.isEmpty()){
                for(Animal animal : value){
                    if(animal != null){
                        System.out.println("przed move");
                        System.out.println(animal.toString());
                        abstractWorldMap.move(animal);
                        System.out.println("po move");
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
