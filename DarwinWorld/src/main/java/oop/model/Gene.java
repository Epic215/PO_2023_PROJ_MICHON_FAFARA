package oop.model;

import java.util.ArrayList;
import java.util.List;

public class Gene {
    private List<Integer> gene = new ArrayList<>();
    private int geneMove;
    public Gene(int N){
        this.geneMove = 0;
        generateGene(N);
    }
    private int generateNumber(int min, int max){
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
    private void generateGene(int N){
        for(int i=0; i<N; i++){
            gene.add(generateNumber(0,7));
        }
    }
    public String toString(){
        return gene.toString();
    }

}
