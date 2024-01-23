package oop.model;



public class DarwinMap extends AbstractWorldMap{
    public DarwinMap(int width, int height,int grassCount,int grassGrowth, int grassEnergy, int dailyEnergy, int minimumMutations, int maximumMutations){
        super(width, height, grassCount, grassGrowth, grassEnergy, dailyEnergy, false, minimumMutations, maximumMutations);
    }
    @Override
    public boolean isOccupied(Vector2d position){
        return false;
    }

}
