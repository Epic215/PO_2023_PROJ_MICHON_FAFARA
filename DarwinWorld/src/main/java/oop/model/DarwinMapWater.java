package oop.model;

public class DarwinMapWater extends AbstractWorldMap{
    public DarwinMapWater(int width, int height,int grassCount,int grassGrowth){//  int energy, int grassGrowth, int animalCount)
        super(width, height, grassCount, grassGrowth);
//        this.animalCount = animalCount;
        //generateWater
    }
    public void move(Animal animal){
        MapDirection mapDirection = animal.getCurrentGene();
        Vector2d supposedPosition = animal.getPosition().add(mapDirection.toUnitVector());

        if(canMoveTo(supposedPosition)){
            animals.get(animal.getPosition()).remove(animal);
//            if(animals.get(animal.getPosition()).size() == 0) {
//                animals.remove(animal.getPosition());
//            }
            if(supposedPosition.getX() > upperRight.getX()){
                animal.crossEarth(bottomLeft.getX(), supposedPosition.getY(), mapDirection);
            } else if (supposedPosition.getX() < bottomLeft.getX()) {
                animal.crossEarth(upperRight.getX(), supposedPosition.getY(), mapDirection);
            } else {
                animal.move(mapDirection);
            }
//            place(animal);
        } else {
            animal.bounce(mapDirection);
        }
        animal.moveGeneIndex();
        place(animal);
    }
    private boolean isOccupied(Vector2d position){
        return false;
    }
    public boolean canMoveTo(Vector2d vector2d){
        return !(vector2d.getY() < bottomLeft.getX() || vector2d.getY() > upperRight.getY() || isOccupied(vector2d));
    }
}
