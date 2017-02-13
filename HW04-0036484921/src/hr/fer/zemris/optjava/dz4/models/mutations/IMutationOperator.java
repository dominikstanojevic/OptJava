package hr.fer.zemris.optjava.dz4.models.mutations;

import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public interface IMutationOperator<T extends AbstractSolution> {
    void mutate(T solution, Random random);
}
