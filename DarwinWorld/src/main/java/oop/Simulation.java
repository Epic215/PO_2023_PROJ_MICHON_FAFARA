package oop;

import oop.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation implements Runnable{
    private int animalCount;
    private int daysCount;
    private final int geneSize;
    private final int initialEnergy;
    private final AbstractWorldMap abstractWorldMap;
    public Simulation(int animalCount, AbstractWorldMap abstractWorldMap, int geneSize,int initialEnergy){
        this.daysCount = 0;
        this.animalCount = animalCount;
        this.abstractWorldMap = abstractWorldMap;
        this.geneSize = geneSize;
        this.initialEnergy = initialEnergy;
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
            abstractWorldMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(4),1,18));
        }
        abstractWorldMap.printAnimals();
        abstractWorldMap.printGrasses();
    }
    public Animal resolveConflictFirstStrongest(ArrayList<Animal> animals){
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
    public Animal resolveConflictSecondStrongest(ArrayList<Animal> animals,Animal firstStrongestAnimal){
        int oldestAnimalAge=0;
        int mostChildren=0;
        int mostEnergy=0;
        ArrayList<Animal> conflictAnimals=new ArrayList<>();
        for (Animal animal : animals){
            if (animal!=firstStrongestAnimal){
                mostEnergy=Math.max(mostEnergy,animal.getEnergyStatus());
            }

        }
        for (Animal animal : animals){
            if (mostEnergy==animal.getEnergyStatus()){
                if (animal!=firstStrongestAnimal){
                    conflictAnimals.add(animal);
                }
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
                Animal animal = resolveConflictFirstStrongest(animals.get(key));
                animal.eatGrass(abstractWorldMap.getGrassEnergy());
                //delete dead
            }
        });
    }
    private void breedAnimals(){
        Random randomPosition = new Random();
        Boundary mapBoundary = abstractWorldMap.getCurrentBounds();
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key,value) -> {
            if(value.size()>=2){
                Animal animal1 = resolveConflictFirstStrongest(value);
                Animal animal2 = resolveConflictSecondStrongest(value,animal1);

                Animal animal3 = new Animal(key,OptionsParser.change(Functions.randomNumberBetween(0,8)),geneSize, initialEnergy);
                animal3.createGene(animal1, animal2, animal2.getEnergy()/animal1.getEnergy()*geneSize);
                abstractWorldMap.place(animal3);
            }
        });
    }
    private void decrementEnergy(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key,value) -> {
            value.forEach(animal -> {
                animal.decreaseEnergy(abstractWorldMap.getDailyEnergy());
            });
        });
    }
    private void deleteDead(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key,value) -> {
            value.stream().filter(animal -> animal.getEnergy() <= 0).forEach(abstractWorldMap::deleteDead);
        });
        abstractWorldMap.mapChanged("fhdjsklafh");
    }
    public void run(){
        for(int i=0; i<10; i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            moveAnimals();
            decrementEnergy();
//            breedAnimals();
            deleteDead();
            this.daysCount += 1;
        }
    }
}
