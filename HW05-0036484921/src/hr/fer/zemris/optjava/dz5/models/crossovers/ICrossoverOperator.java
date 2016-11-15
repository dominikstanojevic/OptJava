package hr.fer.zemris.optjava.dz5.models.crossovers;

import hr.fer.zemris.optjava.dz5.models.Pair;
import hr.fer.zemris.optjava.dz5.models.solutions.AbstractSolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public interface ICrossoverOperator<T extends AbstractSolution> {
    T getChild(Pair<T, T> parents, Random random);
}
