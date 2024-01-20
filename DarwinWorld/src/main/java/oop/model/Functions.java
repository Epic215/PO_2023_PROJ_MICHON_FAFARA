package oop.model;

import java.util.Random;

public class Functions {
    public static int randomNumberBetween(int low, int high){
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }
    public static boolean[] randomNNumbersBetween(int n, int low, int high){
        boolean[] numbers = new boolean[high];
        int numberCount = 0;
        int randomNumber;
        for(int i=0; i< numbers.length; i++){
            numbers[i] = false;
        }
        while(numberCount<n){
            randomNumber = randomNumberBetween(low,high);
            if(numbers[randomNumber]){
                continue;
            } else {
                numbers[randomNumber] = true;
                numberCount+=1;
            }
        }
        return numbers;
    }
}
