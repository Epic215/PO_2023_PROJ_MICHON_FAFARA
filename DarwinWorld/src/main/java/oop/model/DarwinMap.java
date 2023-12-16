package oop.model;

public class DarwinMap {
    private Vector2d bottomLeft;
    private Vector2d upperRight;
    public int mapId;
    public DarwinMap(int width, int height, int mapId){
        this.mapId = mapId;
        this.upperRight = new Vector2d();
        this.bottomLeft = new Vector2d();
        //generateGrass
        //generateWater if()
    }
}
