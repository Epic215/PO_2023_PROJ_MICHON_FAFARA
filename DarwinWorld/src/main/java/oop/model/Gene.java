package oop.model;

import oop.model.Mutation.MutationContext;
import oop.model.Mutation.NormalMutation;
import oop.model.Mutation.ReplaceMutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
            gene[i] = (Functions.randomNumberBetween(0,8));
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
    public int[] getGene(){
        return gene;
    }
    public void createGene(Animal animal1, Animal animal2, int div){
        for (int i=0; i<div; i++){
            gene[i] = animal1.getGene().getGene()[i];
        }
        for (int i=div; i<animal1.getGene().getGene().length; i++){
            gene[i] = animal2.getGene().getGene()[i];
        }
    }
    public void mutateGene(int numOfMutations){
        if(Functions.randomNumberBetween(0,2)==1){
            MutationContext mutation= new MutationContext();
            mutation.setMutationStategy(new ReplaceMutation());
            mutation.executeMutationStrategy(gene,numOfMutations);
        }
        else {
            MutationContext mutation= new MutationContext();
            mutation.setMutationStategy(new NormalMutation());
            mutation.executeMutationStrategy(gene,numOfMutations);
        }
    }
    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Gene that))
            return false;
        return (Arrays.equals(this.gene, that.gene));
    }
    @Override
    public int hashCode(){return Objects.hash(gene);}
}
