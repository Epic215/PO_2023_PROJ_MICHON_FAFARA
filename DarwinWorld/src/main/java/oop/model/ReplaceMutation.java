package oop.model;

import java.util.List;
import java.util.Random;

public class ReplaceMutation implements MutationStategy{
    @Override
    public List<Integer> mutate(List<Integer> gene, int n) {
        Random randomPosition = new Random();

        for (int i=0 ;i<n;i++){
            int position1=randomPosition.nextInt(gene.size());
            int position2;
            do{
                position2=randomPosition.nextInt(gene.size());
            }while(position2==position1);
            int genotypeTmp=gene.get(position1);
            gene.set(position1,gene.get(position2));
            gene.set(position2,genotypeTmp);
        }
        return gene;
    }
}
