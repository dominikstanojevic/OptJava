package hr.fer.zemris.optjava.dz9.models.algorithms;

import hr.fer.zemris.optjava.dz9.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 28.12.2016..
 */
public interface IMOOPProblem<T extends AbstractSolution> {
    int getNumberOfObjectives();
    double[] evaluateSolution(T solution);
}
