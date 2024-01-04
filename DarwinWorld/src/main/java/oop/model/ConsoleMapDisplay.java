package oop.model;

public class ConsoleMapDisplay implements MapChangeListener{
    private int operationCount = 0;
    @Override
    public synchronized void mapChanged(AbstractWorldMap worldMap, String message) {
        System.out.println(message);
        System.out.println(worldMap);
        operationCount += 1;
    }
    public int getOperationCounter(){
        return operationCount;
    }
}