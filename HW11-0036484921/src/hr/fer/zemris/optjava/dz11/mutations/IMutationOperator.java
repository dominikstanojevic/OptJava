package hr.fer.zemris.optjava.dz11.mutations;

import hr.fer.zemris.optjava.dz11.solutions.GASolution;

/**
 * Created by Dominik on 22.1.2017..
 */
public interface IMutationOperator<T extends GASolution<?>> {
    void mutate(T solution);
}
