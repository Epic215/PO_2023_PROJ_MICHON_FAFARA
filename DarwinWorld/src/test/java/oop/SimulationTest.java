package oop;

import oop.model.Animal;
import oop.model.DarwinMap;
import oop.model.MapDirection;
import oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    @Test
    void energyMatterStrongestAnimal(){
        Simulation simulation = new Simulation(1,new DarwinMap(1,1,1,1,1,1,1,2),1,1,1,false);
        Animal animal1 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,1);
        Animal animal2 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,2);
        Animal animal3 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal4 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,4);
        Animal animal5 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,5);
        Animal animal6 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,6);

        ArrayList<Animal> animals = new ArrayList<>(List.of(animal1,animal2,animal3,animal4,animal5,animal6));

        Animal strongest=simulation.resolveConflictFirstStrongest(animals);
        assertEquals(animal6,strongest);
    }
    @Test
    void ageMatterStrongestAnimal(){
        Simulation simulation = new Simulation(1,new DarwinMap(1,1,1,1,1,1,1,2),1,1,1,false);
        Animal animal1 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal2 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal3 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal4 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal5 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal6 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);

        animal1.setAge(26);
        animal2.setAge(52);
        animal3.setAge(31);
        animal4.setAge(67);
        animal5.setAge(43);
        animal6.setAge(12);

        ArrayList<Animal> animals = new ArrayList<>(List.of(animal1,animal2,animal3,animal4,animal5,animal6));

        Animal strongest=simulation.resolveConflictFirstStrongest(animals);
        assertEquals(animal4,strongest);
    }
    @Test
    void childrenMatterStrongestAnimal(){
        Simulation simulation = new Simulation(1,new DarwinMap(1,1,1,1,1,1,1,2),1,1,1,false);
        Animal animal1 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal2 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal3 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal4 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal5 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal6 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);

        animal1.setChildrenCount(8);
        animal2.setChildrenCount(52);
        animal3.setChildrenCount(67);
        animal4.setChildrenCount(34);
        animal5.setChildrenCount(43);
        animal6.setChildrenCount(12);

        ArrayList<Animal> animals = new ArrayList<>(List.of(animal1,animal2,animal3,animal4,animal5,animal6));
        System.out.println(animals);
        Animal strongest=simulation.resolveConflictFirstStrongest(animals);
        System.out.println(animals);
        assertEquals(animal3,strongest);
    }
    @Test
    void randomStrongestAnimal(){
        Simulation simulation = new Simulation(1,new DarwinMap(1,1,1,1,1,1,1,2),1,1,1,false);
        Animal animal1 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal2 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal3 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal4 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal5 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);
        Animal animal6 = new Animal(new Vector2d(1,1), MapDirection.NORTH,5,3);

        animal1.setChildrenCount(2);
        animal2.setChildrenCount(2);
        animal3.setChildrenCount(2);
        animal4.setChildrenCount(4);
        animal5.setChildrenCount(4);
        animal6.setChildrenCount(4);

        ArrayList<Animal> animals = new ArrayList<>(List.of(animal1,animal2,animal3,animal4,animal5,animal6));
        Animal strongest=simulation.resolveConflictFirstStrongest(animals);
        assertEquals(animals.get(2), strongest);
        assertEquals(animals.get(1), strongest);
        assertEquals(animals.get(0), strongest);
        assertNotEquals(animals.get(3), strongest);
    }
}
