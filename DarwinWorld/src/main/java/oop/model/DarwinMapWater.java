package oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DarwinMapWater extends AbstractWorldMap{
    private final Map<Vector2d, Water> waters=new HashMap<>();
    private final ArrayList<Water> mainWaterPools=new ArrayList<>();
    private int phase=0;
    public DarwinMapWater(int width, int height,int grassCount,int grassGrowth, int grassEnergy){//  int energy, int grassGrowth, int animalCount)
        super(width, height, grassCount, grassGrowth, grassEnergy);
//        this.animalCount = animalCount;
        generateWater(width,height);
    }
    private void generateWater(int width,int height){

        for (int i=0;i<2;i++){
            Vector2d waterVector=new Vector2d(Functions.randomNumberBetween(0,width),Functions.randomNumberBetween(0,height));
            waters.put(waterVector,new Water(waterVector));
            mainWaterPools.add(new Water(waterVector));
        }
    }
    private void waterChange(){
        phase+=1;
        for(Water water : mainWaterPools) {
            Vector2d mainWaterPoolPosition=water.getPosition();

        }
    }

    @Override
    public boolean isOccupied(Vector2d position){
        return false;
    }


}
