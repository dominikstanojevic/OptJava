package hr.fer.zemris.optjava.dz9.models.crossovers;

import hr.fer.zemris.optjava.dz9.models.Pair;
import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public interface ICrossoverOperator<T extends AbstractSolution> {
    Pair<T, T> getChildren(Pair<T, T> parents, Random random);
}
