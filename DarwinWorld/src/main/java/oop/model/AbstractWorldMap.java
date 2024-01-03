package oop.model;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public abstract class AbstractWorldMap{
    protected final boolean[] equator;
    protected Vector2d bottomLeft;
    protected Vector2d upperRight;
    protected UUID mapId = UUID.randomUUID();
    protected int grassCount;
    protected int grassGrowth;
    protected int grassEnergy;
    protected int animalCount;
    protected final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected final ArrayList<Vector2d> canPlaceGrassEquator = new ArrayList<>();
    protected final ArrayList<Vector2d> canPlaceGrassSteppes = new ArrayList<>();
    public AbstractWorldMap(int width, int height,int grassCount,int grassGrowth){
        this.upperRight = new Vector2d(width,height);
        this.bottomLeft = new Vector2d(0,0);
        this.grassCount = grassCount;
        this.grassGrowth = grassGrowth;
        this.equator = new boolean[height];
        initializeMapEquator(height,width);
        GrassGenerator(grassCount);
    }
    private void initializeMapEquator(int height,int width){
        int equatorHeigth=0;
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
        pow-=1;
        int i=1;
        while (pow>0){
            equator[middle + i] = true;
            pow-=1;
            if (pow>0){
                equator[middle-i]=true;
                pow-=1;
            }
        }
        for (int j=0;j<height;j++){
            for (int k=0;k<width;k++){
                if (equator[j]){
                    canPlaceGrassEquator.add(new Vector2d(j,k));
                }
                else {
                    canPlaceGrassSteppes.add(new Vector2d(j,k));
                }
            }
        }
    }
    private void GrassGenerator(int grassCount){
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
    public void printAnimals(){
        animals.forEach((key, value) -> {
            System.out.println("Key=" + key + ", Value=" + value.toString());
        });
    }
    public void printGrasses(){
        grasses.forEach((key, value) -> {
            System.out.println("Key=" + key + ", Value=" + value.toString());
        });
    }
    public void move(Animal animal){
        MapDirection mapDirection = animal.getCurrentGene();
        Vector2d supposedPosition = animal.getPosition().add(mapDirection.toUnitVector());

        if(canMoveTo(supposedPosition)){
            animals.get(animal.getPosition()).remove(animal);
//            if(animals.get(animal.getPosition()).size() == 0) {
//                animals.remove(animal.getPosition());
//            }
            if(supposedPosition.getX() > upperRight.getX()){
                animal.crossEarth(bottomLeft.getX(), supposedPosition.getY(), mapDirection);
            } else if (supposedPosition.getX() < bottomLeft.getX()) {
                animal.crossEarth(upperRight.getX(), supposedPosition.getY(), mapDirection);
            } else {
                animal.move(mapDirection);
            }
//            place(animal);
        } else {
            animal.bounce(mapDirection);
        }
        animal.moveGeneIndex();
        place(animal);
    }
    public boolean canMoveTo(Vector2d vector2d){
        return !(vector2d.getY() < bottomLeft.getX() || vector2d.getY() > upperRight.getY() || isOccupied(vector2d));
    }


    abstract boolean isOccupied(Vector2d vector2d);


    public Map<Vector2d, ArrayList<Animal>> getAnimals(){
        Map<Vector2d, ArrayList<Animal>> newAnimals = new HashMap<>();
        animals.forEach((key,value) -> {
            newAnimals.put(key,new ArrayList<>());
            value.forEach(animal -> {
                newAnimals.get(key).add(animal);
            });
        });
        return newAnimals;
    }
    public Boundary getCurrentBounds(){
        return new Boundary(bottomLeft,upperRight);
    }
    public UUID getMapId(){
        return this.mapId;
    }
}
