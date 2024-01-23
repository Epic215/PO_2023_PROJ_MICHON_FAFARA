package oop.model;

public class IncorrectDataException extends RuntimeException{
    public IncorrectDataException(String message){
        super("Incorrect data for " + message);
    }
}
