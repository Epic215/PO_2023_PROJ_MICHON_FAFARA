package oop.model;

import java.util.List;

public class MutationContext {
    private MutationStategy mutationStategy;

    public void setMutationStategy(MutationStategy mutationStategy) {
        this.mutationStategy = mutationStategy;
    }
    public int[] executeMutationStrategy(int[] gene, int n){
        return mutationStategy.mutate(gene,n);
    }
}