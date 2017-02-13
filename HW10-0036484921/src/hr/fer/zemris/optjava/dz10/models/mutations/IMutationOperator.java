package hr.fer.zemris.optjava.dz10.models.mutations;

import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public interface IMutationOperator<T extends AbstractSolution> {
    void mutate(T solution, Random random);
}
