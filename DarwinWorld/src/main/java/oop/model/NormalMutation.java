package oop.model;

import java.util.List;
import java.util.Random;

public class NormalMutation implements MutationStategy{

    @Override
    public List<Integer> mutate(List<Integer> gene, int n) {
        Random randomPosition = new Random();
        int[] genotypes ={0,1,2,3,4,5,6,7};
        for (int i=0 ;i<n;i++){
            int position=randomPosition.nextInt(gene.size());
            int genotype;
            do{
                genotype=randomPosition.nextInt(8);
            }while(genotype==gene.get(position));
            gene.set(position,genotype);
        }
        return gene;
    }
}
