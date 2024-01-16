package oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.min;


public class DarwinMapWater extends AbstractWorldMap{
    private final Map<Vector2d, Water> waters=new HashMap<>();
    private final Map<Vector2d,Water> mainWaterPools=new HashMap<>();
    private final Map<Integer,ArrayList<Water>> phases=new HashMap<>();
    private int phaseNumber=0;
    private static final int PHASES=3; // ilosc wzrosniec
    public DarwinMapWater(int width, int height,int grassCount,int grassGrowth, int grassEnergy, int dailyEnergy){//  int energy, int grassGrowth, int animalCount)
        super(width, height, grassCount, grassGrowth, grassEnergy, dailyEnergy, true);
//        this.animalCount = animalCount;
        generateWater(width,height);
        generateWaterPhases();
    }
    private void generateWater(int width,int height){
        int randomNumberWater=Functions.randomNumberBetween(1,min(width,height)*2);
        for (int i=0;i<randomNumberWater;i++){
            Vector2d waterVector=new Vector2d(Functions.randomNumberBetween(0,width),Functions.randomNumberBetween(0,height));
            Water newWater=new Water(waterVector);
            waters.put(waterVector,newWater);
            mainWaterPools.put(waterVector,newWater);
        }
    }

    public void waterChange(){
        phaseNumber++;
        if (phaseNumber<PHASES){
            waterAddToMap();
        }
        else{
            waterRemoveFromMap();
        }
    }

    private void waterAddToMap() {
        ArrayList<Water> waterToAdd= phases.get(phaseNumber);
        for (Water water : waterToAdd){
            waters.put(water.getPosition(),water);
            if(animals.get(water.getPosition())!=null) {
                for(Animal animal : animals.get(water.getPosition())){
                    animal.decreaseEnergy(animal.getEnergy());
                }
            }
        }
    }
    private void waterRemoveFromMap() {
        ArrayList<Water> waterToAdd= phases.get((2*PHASES-1)-phaseNumber);
        for (Water water : waterToAdd){
            waters.remove(water.getPosition(),water);

        }
        if((2*(PHASES-1))==phaseNumber){
            phaseNumber=0;
        }
    }

    private void generateWaterPhases(){
        Map<Vector2d,Water> usedPositions=new HashMap<>();
        ArrayList<Water> phase = new ArrayList<>();
        for(Map.Entry<Vector2d,Water> water : mainWaterPools.entrySet()) {
            usedPositions.put(water.getKey(),water.getValue());
            phase.add(water.getValue());
        }
        phases.put(0,phase);
        for (int p=1; p<PHASES;p++){
            phase= new ArrayList<>();
            for(Map.Entry<Vector2d,Water> water : mainWaterPools.entrySet()) {
                Vector2d mainWaterPoolPosition=water.getKey();

                int bottom=mainWaterPoolPosition.getY()-p;
                int top=mainWaterPoolPosition.getY()+p;
                for (int i=mainWaterPoolPosition.getX()-p;i<=mainWaterPoolPosition.getX()+p;i++){
                    Vector2d waterTemporaryPlace=new Vector2d(i,top);
                    if (usedPositions.get(waterTemporaryPlace)==null && isWaterStillInMap(waterTemporaryPlace)){
                        Water newWater= new Water(waterTemporaryPlace);
                        usedPositions.put(waterTemporaryPlace,newWater);
                        phase.add(newWater);
                    }
                    waterTemporaryPlace=new Vector2d(i,bottom);
                    if (usedPositions.get(waterTemporaryPlace)==null && isWaterStillInMap(waterTemporaryPlace)) {
                        Water newWater= new Water(waterTemporaryPlace);
                        usedPositions.put(waterTemporaryPlace,newWater);
                        phase.add(newWater);
                    }
                }
                bottom=mainWaterPoolPosition.getX()-p;
                top=mainWaterPoolPosition.getX()+p;
                for (int i=mainWaterPoolPosition.getY()-p;i<=mainWaterPoolPosition.getY()+p;i++){
                    Vector2d waterTemporaryPlace=new Vector2d(bottom,i);
                    if (usedPositions.get(waterTemporaryPlace)==null && isWaterStillInMap(waterTemporaryPlace)){
                        Water newWater= new Water(waterTemporaryPlace);
                        usedPositions.put(waterTemporaryPlace,newWater);
                        phase.add(newWater);
                    }
                    waterTemporaryPlace=new Vector2d(top,i);
                    if (usedPositions.get(waterTemporaryPlace)==null && isWaterStillInMap(waterTemporaryPlace)) {
                        Water newWater= new Water(waterTemporaryPlace);
                        usedPositions.put(waterTemporaryPlace,newWater);
                        phase.add(newWater);
                    }
                }
                phases.put(p,phase);
            }

        }
    }
    private boolean isWaterStillInMap(Vector2d waterTemporaryPlace){
        return (waterTemporaryPlace.follows(bottomLeft) && waterTemporaryPlace.precedes(upperRight.add(new Vector2d(-1,-1))));
    }

    @Override
    public boolean isOccupied(Vector2d position){
        return  waters.get(position)!=null;
    }
    public Map<Vector2d, Water> getWaters(){
        return waters;
    }
    public boolean canMoveTo(Vector2d vector2d){
        return !(vector2d.getX() < bottomLeft.getX() || vector2d.getX() >= upperRight.getX() || vector2d.getY() < bottomLeft.getY() || vector2d.getY() >= upperRight.getY() || isOccupied(vector2d));
    }


}
