package oop.model.Mutation;

import oop.model.Functions;

public class ReplaceMutation implements MutationStrategy {
    @Override
    public void mutate(int[] gene, int n) {

        for (int i=0 ;i<n;i++){
            int position1= Functions.randomNumberBetween(0,gene.length);
            int position2;
            do{
                position2=Functions.randomNumberBetween(0,gene.length);
            }while(position2==position1);
            int genotypeTmp=gene[position1];
            gene[position1]=gene[position2];
            gene[position2]=genotypeTmp;
        }
    }
}
