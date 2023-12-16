package oop.model;

public class Grass {
    private final Vector2d position;
    public Grass(Vector2d position){
        this.position=position;
    }
    public Vector2d getPosition() {
        return position;
    }
    public String toString(){
        return "*";
    }
}
