package oop.model;

public class NormalMutation implements MutationStrategy {

    @Override
    public int[] mutate(int[] gene, int n) {


        for (int i=0 ;i<n;i++){
            int position=Functions.randomNumberBetween(0,gene.length);
            int genotype;
            do{
                genotype=Functions.randomNumberBetween(0,8);
            }while(genotype==gene[position]);
            gene[position]=genotype;
        }
        return gene;
    }
}
