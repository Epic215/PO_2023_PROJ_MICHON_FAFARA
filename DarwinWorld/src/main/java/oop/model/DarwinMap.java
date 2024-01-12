package oop.model;

import java.util.*;

import static java.lang.Math.*;

public class DarwinMap extends AbstractWorldMap{
    public DarwinMap(int width, int height,int grassCount,int grassGrowth, int grassEnergy, int dailyEnergy, int breedEnergy){//  int energy, int grassGrowth, int animalCount)
        super(width, height, grassCount, grassGrowth, grassEnergy, dailyEnergy, breedEnergy);
    }
    @Override
    public boolean isOccupied(Vector2d position){
        return false;
    }

}
