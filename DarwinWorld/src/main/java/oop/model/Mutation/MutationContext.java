package oop.model.Mutation;



public class MutationContext {
    private MutationStrategy mutationStategy;

    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStategy = mutationStrategy;
    }
    public void executeMutationStrategy(int[] gene, int n){
        mutationStategy.mutate(gene, n);
    }
}
