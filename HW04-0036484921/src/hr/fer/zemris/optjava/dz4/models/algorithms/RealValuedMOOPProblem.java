package hr.fer.zemris.optjava.dz4.models.algorithms;

import hr.fer.zemris.optjava.dz9.models.IFunction;
import hr.fer.zemris.optjava.dz9.models.solutions.DoubleArraySolution;

/**
 * Created by Dominik on 28.12.2016..
 */
public class RealValuedMOOPProblem implements IMOOPProblem<DoubleArraySolution> {
    private IFunction[] functions;

    public RealValuedMOOPProblem(IFunction... functions) {
        this.functions = functions;
    }

    @Override
    public int getNumberOfObjectives() {
        return functions.length;
    }

    @Override
    public double[] evaluateSolution(DoubleArraySolution solution) {
        double[] results = new double[functions.length];

        for (int i = 0; i < functions.length; i++) {
            results[i] = functions[i].valueAt(solution.chromosome);
        }

        return results;
    }
}
