package oop.model;

import java.util.Map;

public class Animal implements WorldElement{
    private MapDirection facing;
    private Vector2d position;
    private Gene gene;
    private int energy;
    private int breedEnergy;
    public Animal(Vector2d position, MapDirection facing){ //int geneSize, int energy, int breedEnergy){
        this.facing = facing;
        this.position = position;
//        this.gene = new Gene(geneSize);
//        this.energy = energy;
//        this.breedEnergy = breedEnergy;
    }
    public void decreaseEnergy(int lostEnergy){
        energy = energy - lostEnergy;
    }
    public void move(MapDirection mapDirection){
        this.position = this.position.add(mapDirection.toUnitVector());
    }
    public void crossEarth(int x, int y){
        this.position = new Vector2d(x,y);
    }
    public String toString(){
        return position.toString() + " " + facing.direction;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
}
