package oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DarwinMapWater extends AbstractWorldMap{
    private final Map<Vector2d, Water> waters=new HashMap<>();
    private final ArrayList<Water> mainWaterPools=new ArrayList<>();
    private int phase=0;
    public DarwinMapWater(int width, int height,int grassCount,int grassGrowth){//  int energy, int grassGrowth, int animalCount)
        super(width, height, grassCount, grassGrowth);
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
    private boolean isWaterOnPlace(Vector2d waterTemporaryPlace,Vector2d mainWaterPoolPosition){
        return waterTemporaryPlace.follows(bottomLeft) && waterTemporaryPlace.precedes(upperRight) && !waterTemporaryPlace.equals(mainWaterPoolPosition) && waters.containsKey(waterTemporaryPlace);
    }
    private void waterChange(){
        phase+=1;

        if(phase<3)
        {
            for(Water water : mainWaterPools) {
                Vector2d mainWaterPoolPosition=water.getPosition();
                for (int i=mainWaterPoolPosition.getX()-phase;i<=mainWaterPoolPosition.getX()+phase;i++){
                    for (int j=mainWaterPoolPosition.getY()-phase;i<=mainWaterPoolPosition.getY()+phase;j++){
                        Vector2d waterTemporaryPlace=new Vector2d(i,j);
                        if (isWaterOnPlace(waterTemporaryPlace,mainWaterPoolPosition)){
                            waters.put(waterTemporaryPlace,new Water(waterTemporaryPlace));
                        }
                    }
                }
            }
        }
        if(phase<3)
        {
            for(Water water : mainWaterPools) {
                Vector2d mainWaterPoolPosition=water.getPosition();
                for (int i=mainWaterPoolPosition.getX()-phase;i<=mainWaterPoolPosition.getX()+phase;i++){
                    for (int j=mainWaterPoolPosition.getY()-phase;i<=mainWaterPoolPosition.getY()+phase;j++){
                        Vector2d waterTemporaryPlace=new Vector2d(i,j);
                        if (isWaterOnPlace(waterTemporaryPlace,mainWaterPoolPosition)){
                            waters.put(waterTemporaryPlace,new Water(waterTemporaryPlace));
                        }
                    }
                }
            }
        }
        for(Water water : mainWaterPools) {
            Vector2d mainWaterPoolPosition=water.getPosition();

        }
    }

    @Override
    public boolean isOccupied(Vector2d position){
        return false;
    }


}
