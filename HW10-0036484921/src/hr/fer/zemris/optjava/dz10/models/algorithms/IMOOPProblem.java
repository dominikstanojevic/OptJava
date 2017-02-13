package hr.fer.zemris.optjava.dz10.models.algorithms;

import hr.fer.zemris.optjava.dz10.models.IFunction;
import hr.fer.zemris.optjava.dz10.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 28.12.2016..
 */
public interface IMOOPProblem<T extends AbstractSolution>{
    int getNumberOfObjectives();
    double[] evaluateSolution(T solution);
    IFunction getFunction(int index);
}
