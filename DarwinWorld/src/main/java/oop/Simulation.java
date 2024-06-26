package oop;

import oop.model.*;

import java.util.*;

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
            height = randomPosition.nextInt(mapBoundary.upperRight().getY());
            width = randomPosition.nextInt(mapBoundary.upperRight().getX());
            direction = randomPosition.nextInt(8);
            abstractWorldMap.place(new Animal(new Vector2d(width,height),OptionsParser.change(direction),geneSize,initialEnergy));
        }
    }
    public Animal resolveConflictFirstStrongest(ArrayList<Animal> animals){

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

    }
    public Animal resolveConflictSecondStrongest(ArrayList<Animal> animals,Animal firstStrongestAnimal){
        ArrayList<Animal> conflictAnimals=new ArrayList<>();
        Comparator<Animal> animalComparatorConflict = Comparator.comparing(Animal::getEnergyStatus)
                .thenComparing(Animal::getAge)
                .thenComparing(Animal::getChildrenCount).reversed();
        animals.sort(animalComparatorConflict);
        Animal secondStrongestAnimal=animals.get(0);
        if (secondStrongestAnimal.getAnimalId()==firstStrongestAnimal.getAnimalId()){
            secondStrongestAnimal=animals.get(1);
        }
        for (Animal animal : animals){
            if (animal.getAnimalId()==firstStrongestAnimal.getAnimalId()){
                continue;
            }
            if (secondStrongestAnimal.equals(animal)){
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
    }
    public void moveAnimals(){
        Map<Vector2d, ArrayList<Animal>> animals = abstractWorldMap.getAnimals2();
        animals.forEach((key, value) -> {

            if(!value.isEmpty()){
                for(Animal animal : value){
                    if(animal != null){
                        animal.incrementAge();
                        abstractWorldMap.move(animal);

                    }
                }
            }
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
    public int getInitialEnergy(){
        return initialEnergy;
    }
    public void run(){
        while(running){
            try {
                Thread.sleep(300);
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
                abstractWorldMap.GrassGenerator(abstractWorldMap.getGrassGrowth());
                this.daysCount += 1;

            }

        }
    }
}
