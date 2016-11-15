package hr.fer.zemris.optjava.dz5.models.algorithms;

import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 25.10.2016..
 */
public interface IAlgorithm<T extends AbstractSolution> {
    T run();
}
