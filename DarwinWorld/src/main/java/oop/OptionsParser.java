package oop;

import oop.model.MapDirection;

public class OptionsParser {
    public static MapDirection change(int arg){

        switch(arg) {
            case 0 -> {
                return MapDirection.NORTH;
            }
            case 1 -> {
                return MapDirection.NORTH_EAST;
            }
            case 2 -> {
                return MapDirection.EAST;
            }
            case 3 -> {
                return MapDirection.SOUTH_EAST;
            }
            case 4 -> {
                return MapDirection.SOUTH;
            }
            case 5 -> {
                return MapDirection.SOUTH_WEST;
            }
            case 6 -> {
                return MapDirection.WEST;
            }
            case 7 -> {
                return MapDirection.NORTH_WEST;
            }
            default -> throw new IllegalArgumentException(arg + " is not legal move specification");

        }
    }


}