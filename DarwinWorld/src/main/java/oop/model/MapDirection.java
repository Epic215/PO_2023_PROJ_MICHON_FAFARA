package oop.model;

public enum MapDirection {
    NORTH("N"),
    NORTH_EAST("NE"),
    EAST("E"),
    SOUTH_EAST("SE"),
    SOUTH("S"),
    SOUTH_WEST("SW"),
    WEST("W"),
    NORTH_WEST("NW");


    public final String direction;
    MapDirection(String direction){
        this.direction = direction;
    }

}