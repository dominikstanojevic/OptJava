package hr.fer.zemris.optjava.dz4.models.selections;

import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;
import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * Created by Dominik on 25.10.2016..
 */
public interface ISelection<T extends AbstractSolution> {
    T select(Population<T> population, Random random);
}