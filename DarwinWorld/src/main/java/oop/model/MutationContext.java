package oop.model;

public class MutationContext {
    private MutationStrategy mutationStategy;

    public void setMutationStategy(MutationStrategy mutationStategy) {
        this.mutationStategy = mutationStategy;
    }
    public int[] executeMutationStrategy(int[] gene, int n){
        return mutationStategy.mutate(gene,n);
    }
}
