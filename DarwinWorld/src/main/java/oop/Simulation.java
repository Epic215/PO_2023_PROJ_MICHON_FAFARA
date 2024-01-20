package oop;

import oop.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation implements Runnable{
    private int animalCount;
    private int daysCount;
    private final int geneSize;
    private final int initialEnergy;
    private final int breedEnergy;
    private final AbstractWorldMap abstractWorldMap;
    private boolean pause=false;
    private boolean running=true;
    private int simulationDay;
    private boolean saveToCsv;
    public Simulation(int animalCount, AbstractWorldMap abstractWorldMap, int geneSize,int initialEnergy, int breedEnergy, boolean saveToCsv){
        this.daysCount = 0;
        this.simulationDay = 0;
        this.animalCount = animalCount;
        this.abstractWorldMap = abstractWorldMap;
        this.geneSize = geneSize;
        this.initialEnergy = initialEnergy;
        this.breedEnergy = breedEnergy;
        this.saveToCsv = saveToCsv;
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
            abstractWorldMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(direction),geneSize,initialEnergy));
        }
//        abstractWorldMap.printAnimals();
//        abstractWorldMap.printGrasses();
    }
    public Animal resolveConflictFirstStrongest(ArrayList<Animal> animals){
//        int oldestAnimalAge=0;
//        int mostChildren=0;
//        int mostEnergy=0;
        ArrayList<Animal> conflictAnimals=new ArrayList<>();
        Comparator<Animal> animalComparatorConflict = Comparator.comparing(Animal::getEnergyStatus)
                .thenComparing(Animal::getAge)
                .thenComparing(Animal::getChildrenCount).reversed();
        animals.sort(animalComparatorConflict);
        Animal strongestAnimal=animals.get(0);
        for (Animal animal : animals){
            if (strongestAnimal.equals(animal)){
                conflictAnimals.add(animal);
            }
            else {
                break;
            }
        }
        if (conflictAnimals.size()>1){
            int randomNumber=Functions.randomNumberBetween(0,conflictAnimals.size());
            return conflictAnimals.get(randomNumber);
        }
        return conflictAnimals.get(0);

//        for (Animal animal : animals){
//            mostEnergy=Math.max(mostEnergy,animal.getEnergyStatus());
//        }
//        for (Animal animal : animals){
//            if (mostEnergy==animal.getEnergyStatus()){
//                conflictAnimals.add(animal);
//            }
//        }
//        if (conflictAnimals.size()!=1){
//            for (Animal animal : conflictAnimals){
//                oldestAnimalAge=Math.max(oldestAnimalAge,animal.getAge());
//            }
//            for (Animal animal :  new ArrayList<>(conflictAnimals)){
//                if (oldestAnimalAge!=animal.getAge()){
//                    conflictAnimals.remove(animal);
//                }
//            }
//            if (conflictAnimals.size()!=1){
//                for (Animal animal : conflictAnimals){
//                    mostChildren=Math.max(mostChildren,animal.getChildrenCount());
//                }
//                for (Animal animal : new ArrayList<>(conflictAnimals)){
//                    if (mostChildren!=animal.getChildrenCount()){
//                        conflictAnimals.remove(animal);
//                    }
//                }
//                if (conflictAnimals.size()!=1){
//                    int randomNumber=Functions.randomNumberBetween(0,conflictAnimals.size());
//                    return conflictAnimals.get(randomNumber);
//                }
//            }
//        }
//        return conflictAnimals.get(0);
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
//                    System.out.println(conflictAnimals.size());
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
//            abstractWorldMap.printAnimals();
            if(!value.isEmpty()){
                for(Animal animal : value){
                    if(animal != null){
                        animal.incrementAge();
//                        System.out.println("przed move");
//                        System.out.println(animal.toString());
                        abstractWorldMap.move(animal);
//                        System.out.println("po move");
//                        System.out.println(animal.toString());
                    }
                }
            }
        });
        abstractWorldMap.mapChanged("fhdjsklafh");
    }
    public void moveAnimals2(){
        ArrayList<Animal> animals = abstractWorldMap.getAnimals();
        animals.forEach(animal -> {
//            abstractWorldMap.printAnimals();
//                System.out.println("przed move");
//                System.out.println(animal.toString());
                abstractWorldMap.move(animal);
//                System.out.println("po move");
//                System.out.println(animal.toString());

        });
        abstractWorldMap.mapChanged("fhdjsklafh");
    }
    private void eatGrass(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        Map<Vector2d, Grass> grasses = abstractWorldMap.getGrasses();
        grasses.forEach((key,value) -> {
            if(animals.get(key)!=null){
                Animal animal = resolveConflictFirstStrongest(animals.get(key));
                if(animal.getEnergy()>0){
                    animal.eatGrass(abstractWorldMap.getGrassEnergy(), initialEnergy);
                    abstractWorldMap.deleteGrass(value);
                }
            }
        });
    }
    private void breedAnimals(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key,value) -> {
            if(value.size() >= 2){
                Animal animal1 = resolveConflictFirstStrongest(value);
                Animal animal2 = resolveConflictSecondStrongest(value,animal1);
                if(animal1.getEnergy()>=breedEnergy && animal2.getEnergy()>=breedEnergy){
                    Animal animal3 = new Animal(key,OptionsParser.change(Functions.randomNumberBetween(0,8)),geneSize, 2*breedEnergy);
//                    System.out.println(animal3.toString());
                    animal3.createGene(animal1, animal2, animal1.getEnergy()/(animal1.getEnergy()+animal2.getEnergy())*geneSize);
                    animal3.mutateGene(abstractWorldMap.getMinimumMutations(), abstractWorldMap.getMaximumMutations());
                    abstractWorldMap.place(animal3);
                    animal1.decreaseEnergy(breedEnergy);
                    animal2.decreaseEnergy(breedEnergy);
                    animal1.incrementChildCount();
                    animal2.incrementChildCount();
                }
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
    public void pause(){
        pause=true;
    }
    public void resume(){
        pause=false;
    }
    public void stop(){
        running=false;
    }
    public int getSimulationDay(){
        return simulationDay;
    }
    public boolean getSaveToCsv(){
        return saveToCsv;
    }
    public void run(){
        while(running){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!pause){


                if (daysCount%10==0 && daysCount!=0 && abstractWorldMap.getIsWater()) {((DarwinMapWater)abstractWorldMap).waterChange();}
                simulationDay+=1;
                deleteDead();
                if(abstractWorldMap.getAnimalCount()==0){
                    break;
                }
                moveAnimals();
                decrementEnergy();
                eatGrass();
                breedAnimals();
//            deleteDead();
                abstractWorldMap.GrassGenerator(abstractWorldMap.getGrassGrowth());
                this.daysCount += 1;

            }

        }
    }
}
