package oop.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.*;

public abstract class AbstractWorldMap{
    protected final boolean[] equator;
    protected Vector2d bottomLeft;
    protected Vector2d upperRight;
    protected int grassCount;
    protected int grassGrowth;
    protected int grassEnergy;
    protected int dailyEnergy;
    protected int overallAge;
    protected int deadCount;
    protected boolean isWater;
    protected int minimumMutations;
    protected int maximumMutations;
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final ArrayList<Vector2d> canPlaceGrassEquator = new ArrayList<>();
    protected final ArrayList<Vector2d> canPlaceGrassSteppes = new ArrayList<>();
    private final List<MapChangeListener> listeners = new ArrayList<>();
    private Map<String, Integer> popularGene = new HashMap<>();
    public AbstractWorldMap(int width, int height,int grassCount,int grassGrowth, int grassEnergy, int dailyEnergy, boolean isWater, int minimumMutations, int maximumMutations){
        this.upperRight = new Vector2d(width,height);
        this.bottomLeft = new Vector2d(0,0);
        this.grassCount = grassCount;
        this.grassGrowth = grassGrowth;
        this.grassEnergy = grassEnergy;
        this.equator = new boolean[height];
        this.dailyEnergy = dailyEnergy;
        this.overallAge = 0;
        this.deadCount = 0;
        this.isWater = isWater;
        this.minimumMutations = minimumMutations;
        this.maximumMutations = maximumMutations;
        initializeMapEquator(height,width);
        GrassGenerator(grassCount);
    }
    private void initializeMapEquator(int height,int width){
        int equatorHeigth;
        int pow= (int) (height*0.2);
        int middle=(height-1)/2;
        if (min(abs(0.2- (double) pow /height),abs(0.2- (double) (pow+1) /height))==abs(0.2- (double) pow /height))
        {
            equatorHeigth=pow;
        }
        else
        {
            equatorHeigth=pow+1;
        }
        equator[middle]=true;
        equatorHeigth-=1;
        int i=1;
        while (equatorHeigth>0){
            equator[middle + i] = true;
            equatorHeigth-=1;
            if (equatorHeigth>0){
                equator[middle-i]=true;
                equatorHeigth-=1;
            }
            i++;
        }
        for (int j=0;j<height;j++){
            for (int k=0;k<width;k++){
                if (equator[j]){
                    canPlaceGrassEquator.add(new Vector2d(k,j));
                }
                else {
                    canPlaceGrassSteppes.add(new Vector2d(k,j));
                }
            }
        }
    }
    public void GrassGenerator(int grassCount){
        int grassEquator=(int) (grassCount*0.8);
        int grassSteppes=grassCount-grassEquator;
        Random rand = new Random();
        for (int i=0;i<grassEquator;i++){
            if (canPlaceGrassEquator.isEmpty()){
                grassSteppes=grassCount-i;
                break;
            }
            int randomNumber=rand.nextInt(canPlaceGrassEquator.size());
            grasses.put(canPlaceGrassEquator.get(randomNumber),new Grass(canPlaceGrassEquator.get(randomNumber)));
            canPlaceGrassEquator.remove(randomNumber);
        }
        for (int i=0;i<grassSteppes;i++){
            if (canPlaceGrassSteppes.isEmpty()){
                break;
            }
            int randomNumber=rand.nextInt(canPlaceGrassSteppes.size());
            grasses.put(canPlaceGrassSteppes.get(randomNumber),new Grass(canPlaceGrassSteppes.get(randomNumber)));
            canPlaceGrassSteppes.remove(randomNumber);
        }


    }
    public void deleteGrass(Grass grass){
        if (equator[grass.getPosition().getY()]){
            canPlaceGrassEquator.add(grass.getPosition());
        }
        else {
            canPlaceGrassSteppes.add(grass.getPosition());
        }
        grasses.remove(grass.getPosition());
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

    public void move(Animal animal){
        MapDirection mapDirection = animal.getCurrentGene();
        Vector2d supposedPosition = animal.getPosition().add(animal.getFacing().turn(mapDirection.direction).toUnitVector());

        if(canMoveTo(supposedPosition)){
            animals.get(animal.getPosition()).remove(animal);
            if(animals.get(animal.getPosition()).isEmpty()) {
                animals.remove(animal.getPosition());
            }
            if(supposedPosition.getX() >= upperRight.getX()){
                animal.crossEarth(bottomLeft.getX(), supposedPosition.getY(), mapDirection);
            } else if (supposedPosition.getX() < bottomLeft.getX()) {
                animal.crossEarth(upperRight.getX()-1, supposedPosition.getY(), mapDirection);
            } else {
                animal.move(mapDirection);
            }

        } else {
            animal.bounce();
            animals.get(animal.getPosition()).remove(animal);
        }
        animal.moveGeneIndex();
        place(animal);
    }
    public void deleteDead(Animal animal){
        deadCount += 1;
        overallAge += animal.getAge();
        animals.get(animal.getPosition()).remove(animal);
        if(animals.get(animal.getPosition()).isEmpty()){
            animals.remove(animal.getPosition());
        }
    }
    public boolean canMoveTo(Vector2d vector2d){
        return !(vector2d.getY() < bottomLeft.getY() || vector2d.getY() >= upperRight.getY() || isOccupied(vector2d));
    }
    abstract boolean isOccupied(Vector2d vector2d);
    public Map<Vector2d, ArrayList<Animal>> getAnimals2(){
        Map<Vector2d, ArrayList<Animal>> newAnimals = new HashMap<>();
        animals.forEach((key,value) -> {
            newAnimals.put(key,new ArrayList<>());
            value.forEach(animal -> {
                newAnimals.get(key).add(animal);
            });
        });
        return newAnimals;
    }
    public ArrayList<Animal>  getAnimals(){
        ArrayList<Animal> newAnimals = new ArrayList<>();
        animals.forEach((key,value) -> {
            if(!value.isEmpty()){
                newAnimals.addAll(value);
            }
        });
        return newAnimals;
    }
    public Map<Vector2d, Grass> getGrasses(){
        Map<Vector2d, Grass> newGrasses = new HashMap<>();
        newGrasses.putAll(grasses);
        return newGrasses;
    }
    public Boundary getCurrentBounds(){
        return new Boundary(bottomLeft,upperRight);
    }

    public int getAnimalCount(){
        return this.getAnimals().size();
    }
    public int getGrassCount(){
        return grasses.size();
    }
    public int getFreeFieldsCount(){
        int count = 0;
        for(int i=0; i<getCurrentBounds().upperRight().getY(); i++){
            for(int j=0; j<getCurrentBounds().upperRight().getX(); j++){
                if(animals.get(new Vector2d(i,j))!=null && grasses.get(new Vector2d(i,j))!=null){
                    count+=1;
                }
            }
        }
        return count;
    }
    public String getMostPopularGeneType(){
        popularGene.clear();
        animals.forEach((key,value) -> {
            if(!value.isEmpty()){
                for (Animal animal : value){
                    String gene = animal.getGene().toString();
                    popularGene.merge(gene, 1, Integer::sum);
                }
            }
        });
        AtomicInteger maxGene = new AtomicInteger(0);
        AtomicReference<String> maxGeneArray = new AtomicReference<>();
        popularGene.forEach((key,value) -> {
            if(maxGene.get() < value){
                maxGene.set(value);
                maxGeneArray.set(key);
            }
        });

        return maxGeneArray.toString();
    }
    public int getAverageEnergy(){
        final int[] energySum = {0};
        animals.forEach((key,value) -> {
            value.forEach(animal -> {
                energySum[0] += animal.getEnergy();
            });
        });
        if(getAnimalCount()!=0){
            return energySum[0]/getAnimalCount();
        }
        return 0;
    }
    public void subscribe(MapChangeListener listener){
        listeners.add(listener);
    }
    public void mapChanged(String message){
        for (MapChangeListener listener : listeners){
            listener.mapChanged(this, message);
        }
    }
    public int getGrassEnergy(){
        return grassEnergy;
    }
    public int getDailyEnergy(){return dailyEnergy;}
    public int getAverageDeadAge(){
        if(deadCount!=0){
            return overallAge/deadCount;
        }
        return 0;
    }
    public int getAverageChildCount(){
        final int[] childCount = {0};
        final int[] animalCount = {0};
        animals.forEach((key,value) -> {
            value.forEach(animal -> {
                childCount[0] += animal.getChildrenCount();
                animalCount[0] += 1;
            });
        });
        if (animalCount[0]==0) return 0;
        return childCount[0]/animalCount[0];
    }
    public boolean getIsWater(){
        return isWater;
    }
    public int getGrassGrowth() {
        return grassGrowth;
    }
    public int getMinimumMutations(){
        return minimumMutations;
    }
    public int getMaximumMutations(){
        return maximumMutations;
    }

    public boolean[] getEquator() {
        return equator;
    }

}
