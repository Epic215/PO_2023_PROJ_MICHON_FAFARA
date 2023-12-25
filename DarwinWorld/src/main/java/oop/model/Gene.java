package oop.model;

import java.util.ArrayList;
import java.util.List;

public class Gene {
    private final List<Integer> gene = new ArrayList<>();
    private int geneIndex;
    public Gene(int geneSize){
        this.geneIndex = 0;
        generateGene(geneSize);
    }
    private int generateNumber(int min, int max){
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
    private void generateGene(int geneSize){
        for(int i=0; i<geneSize; i++){
            gene.add(generateNumber(0,7));
        }
    }

    public List<Integer> getGene(){
        return gene;
    }
    public void moveGeneIndex(){
        geneIndex = (geneIndex+1)%gene.size();
    }
    public String toString(){
        return gene.toString();
    }
}
