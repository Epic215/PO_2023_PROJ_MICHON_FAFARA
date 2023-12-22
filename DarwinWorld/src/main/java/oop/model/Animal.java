package oop.model;

public class Animal {
    private MapDirection facing;
    private Vector2d position;
    private Gene gene;
    public Animal(Vector2d position, MapDirection facing, int N){
        this.facing = MapDirection.NORTH;
        this.position = new Vector2d(1,1);
        this.gene = new Gene(N);
        System.out.println(facing);
        System.out.println(position.toString());
        System.out.println(gene);
    }
}
