package oop.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gene {
    private int[] gene;
    private int geneIndex;
    public Gene(int geneSize){
        this.geneIndex = 0;
        generateGene(geneSize);
    }
    private int generateNumber(int min, int max){
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }
    private void generateGene(int geneSize){
        gene = new int[geneSize];
        for(int i=0; i<geneSize; i++){
            gene[i] = (generateNumber(0,7));
//            System.out.println(gene[i]);
        }
    }
    public void moveGeneIndex(){
        geneIndex = (geneIndex+1)%gene.length;
    }
    public int getCurrentGene(){
        return gene[geneIndex];
    }
    public String toString(){
        return Arrays.toString(gene);
    }
}
