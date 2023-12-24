package oop.model;

import java.util.List;

public interface MutationStategy {
    List<Integer> mutate(List<Integer> gene,int n);
}
