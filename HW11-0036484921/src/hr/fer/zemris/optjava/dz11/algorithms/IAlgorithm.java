package hr.fer.zemris.optjava.dz11.algorithms;

import hr.fer.zemris.optjava.dz11.solutions.GASolution;

/**
 * Created by Dominik on 22.1.2017..
 */
public interface IAlgorithm<T extends GASolution<?>> {
    T run();
}
