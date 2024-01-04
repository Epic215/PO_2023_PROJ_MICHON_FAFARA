package oop;

import oop.model.*;

import java.util.*;

public class Simulation implements Runnable{
    private int animalCount;
    private final AbstractWorldMap abstractWorldMap;
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
            height = randomPosition.nextInt(mapBoundary.upperRight().getX());
            width = randomPosition.nextInt(mapBoundary.upperRight().getY());
            direction = randomPosition.nextInt(8);
            abstractWorldMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(direction),1,0,0,0));
        }
        abstractWorldMap.printAnimals();
        abstractWorldMap.printGrasses();
    }
    public Animal resolveConflict(ArrayList<Animal> animals){
        int oldestAnimalAge=0;
        int mostChildren=0;
        int mostEnergy=0;
        ArrayList<Animal> conflictAnimals=new ArrayList<>();
        for (Animal animal : animals){
            mostEnergy=Math.max(mostEnergy,animal.getEnergyStatus());
        }
        for (Animal animal : animals){
            if (mostEnergy==animal.getEnergyStatus()){
                conflictAnimals.add(animal);
            }
        }
        if (conflictAnimals.size()!=1){
            for (Animal animal : conflictAnimals){
                oldestAnimalAge=Math.max(oldestAnimalAge,animal.getAge());
            }
            for (Animal animal :  new ArrayList<>(conflictAnimals)){
                if (oldestAnimalAge!=animal.getAge()){
                    conflictAnimals.remove(animal);
                }
            }
            if (conflictAnimals.size()!=1){
                for (Animal animal : conflictAnimals){
                    mostChildren=Math.max(mostChildren,animal.getChildrenCount());
                }
                for (Animal animal : new ArrayList<>(conflictAnimals)){
                    if (mostChildren!=animal.getChildrenCount()){
                        conflictAnimals.remove(animal);
                    }
                }
                if (conflictAnimals.size()!=1){
                    int randomNumber=Functions.randomNumberBetween(0,conflictAnimals.size());
                    return conflictAnimals.get(randomNumber);
                }
            }
        }
        return conflictAnimals.get(0);
    }
    public void moveAnimals(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key, value) -> {
            abstractWorldMap.printAnimals();
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
        abstractWorldMap.mapChanged("fhdjsklafh");
    }
    public void moveAnimals2(){
        ArrayList<Animal> animals = abstractWorldMap.getAnimals();
        animals.forEach(animal -> {
            abstractWorldMap.printAnimals();

                System.out.println("przed move");
                System.out.println(animal.toString());
                abstractWorldMap.move(animal);
                System.out.println("po move");
                System.out.println(animal.toString());

        });
        abstractWorldMap.mapChanged("fhdjsklafh");
    }
    private void eatGrass(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        Map<Vector2d, Grass> grasses = abstractWorldMap.getGrasses();
        grasses.forEach((key,value) -> {
            if(animals.containsKey(key)){
                Animal animal = resolveConflict(animals.get(key));
                animal.eatGrass(abstractWorldMap.getGrassEnergy());
                //delete dead
            }
        });
    }
    private void breedAnimals(){

    }
    private void deleteDead(){

    }
    public void run(){
        for(int i=0; i<10; i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            moveAnimals();
        }
    }
}
