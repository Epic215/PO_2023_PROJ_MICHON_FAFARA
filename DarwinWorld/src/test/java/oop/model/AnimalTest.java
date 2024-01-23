package oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest {
    @Test
    void move(){
        Vector2d initialPosition = new Vector2d(5,5);
        Vector2d supposedPosition1 = new Vector2d(5,4);
        Vector2d supposedPosition2 = new Vector2d(6,5);
        Animal animal1 = new Animal(initialPosition,MapDirection.NORTH, 5,20);
        animal1.move(MapDirection.SOUTH);
        assertTrue(animal1.getPosition().equals(supposedPosition1));
        animal1.move(MapDirection.SOUTH);
        assertTrue(animal1.getPosition().equals(initialPosition));
        animal1.move(MapDirection.EAST);
        assertTrue(animal1.getPosition().equals(supposedPosition2));
        animal1.move(MapDirection.SOUTH);
        assertTrue(animal1.getPosition().equals(initialPosition));
    }
}
