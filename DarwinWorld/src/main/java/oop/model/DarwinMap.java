package oop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DarwinMap {
    private Vector2d bottomLeft;
    private Vector2d upperRight;
    public int mapId;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    public DarwinMap(int width, int height, int mapId){
        this.mapId = mapId;
        this.upperRight = new Vector2d(width,height);
        this.bottomLeft = new Vector2d(0,0);
        //generateGrass
        //generateWater
    }
    public Boundary getCurrentBounds(){
        return new Boundary(bottomLeft,upperRight);
    }
    public int getMapId(){
        return this.mapId;
    }

}
