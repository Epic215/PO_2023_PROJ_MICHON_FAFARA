package oop.model;

public class Animal {
    private MapDirection facing;
    private Vector2d position;
    private Gene gene;
    public Animal(Vector2d position, MapDirection facing,Gene gene){
        this.facing = MapDirection.NORTH;
        this.position = new Vector2d();
        this.gene = gene;
    }
}
