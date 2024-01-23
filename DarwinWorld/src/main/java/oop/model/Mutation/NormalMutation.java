package oop.model.Mutation;

import oop.model.Functions;

public class NormalMutation implements MutationStrategy {

    @Override
    public void mutate(int[] gene, int n) {


        for (int i=0 ;i<n;i++){
            int position= Functions.randomNumberBetween(0,gene.length);
            int genotype;
            do{
                genotype=Functions.randomNumberBetween(0,8);
            }while(genotype==gene[position]);
            gene[position]=genotype;
        }
    }
}
