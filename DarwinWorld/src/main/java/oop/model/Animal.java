package oop.model;

import java.util.*;

import static java.lang.Math.min;
import static oop.model.MapDirection.*;

public class Animal implements WorldElement{
    private int childrenCount;
    private int age;
    private MapDirection facing;
    private Vector2d position;
    private UUID animalId;
    private Gene gene;
    private int energy;
    private int breedEnergy;
    private int eatenGrass;
    public Animal(Vector2d position, MapDirection facing, int geneSize,int energy){ //int geneSize, int energy, int breedEnergy){
        this.facing = facing;
        this.position = position;
        this.gene = new Gene(geneSize);
        this.energy=energy;
        this.age = 0;
        this.childrenCount = 0;
        this.eatenGrass = 0;
        this.animalId = UUID.randomUUID();
//        this.energy = energy;
//        this.breedEnergy = breedEnergy;
    }
    public void decreaseEnergy(int lostEnergy){
        energy = energy - lostEnergy;
    }
    public void move(MapDirection mapDirection){
        this.facing = this.facing.turn(mapDirection.direction);
        this.position = this.position.add(this.facing.toUnitVector());
    }
    public void printGene(){
        System.out.println(gene.toString());
    }
    public void crossEarth(int x, int y, MapDirection mapDirection){
        this.facing = this.facing.turn(mapDirection.direction);
        this.position = new Vector2d(x,y);
    }
    public int getAge(){
        return age;
    }
    public int getChildrenCount(){
        return childrenCount;
    }
    public int getEnergyStatus(){
        return energy;
    }
    public MapDirection getCurrentGene(){
        return switch(gene.getCurrentGene()){
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SOUTH_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> ERR;
        };
    }
    public void moveGeneIndex(){
        gene.moveGeneIndex();
    }
    public void bounce(MapDirection direction){
        this.facing = this.facing.turn(4);
    }
    public String toString(){
        return position.toString() + " " + facing.direction + " " + gene.toString();
    }
    public void eatGrass(int grassEnergy, int initialEnergy){
        this.eatenGrass += 1;
        this.energy = min(initialEnergy ,this.energy + grassEnergy);
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
    public UUID getAnimalId(){
        return animalId;
    }
    public int getEnergy(){
        return this.energy;
    }
    public Gene getGene(){
        return gene;
    }
    public MapDirection getFacing(){
        return facing;
    }
    public void createGene(Animal animal1, Animal animal2, int div){
        childrenCount+=1;
        gene.createGene(animal1, animal2, div);
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
    public int getEatenGrass(){
        return eatenGrass;
    }
    public void incrementAge(){
        age+=1;
    }
    public void incrementChildCount(){
        childrenCount+=1;
    }
    public void mutateGene(int min, int max){
        int numOfMutations = Functions.randomNumberBetween(min,max);
        gene.mutateGene(numOfMutations);
    }
    public boolean ifGeneMostPopular(String gene){
//        System.out.println(gene);
//        System.out.println(Arrays.toString(this.getGene().getGene()));
        return gene.equals(Arrays.toString(this.getGene().getGene()));
    }
    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Animal that))
            return false;
        return (that.animalId==this.animalId);
    }
}
