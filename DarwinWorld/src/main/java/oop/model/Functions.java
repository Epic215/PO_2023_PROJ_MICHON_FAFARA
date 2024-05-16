package oop.model;

import java.util.Random;

public class Functions {
    public static int randomNumberBetween(int low, int high){
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }

}
