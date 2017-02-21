package hr.fer.zemris.optjava.dz4.models.selections;

import hr.fer.zemris.optjava.dz4.models.Population;
import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 25.10.2016..
 */
public interface ISelection<T extends AbstractSolution> {
    T selectBest(Population<T> population, Random random);
    T selectWorst(Population<T> population, Random random);
}
