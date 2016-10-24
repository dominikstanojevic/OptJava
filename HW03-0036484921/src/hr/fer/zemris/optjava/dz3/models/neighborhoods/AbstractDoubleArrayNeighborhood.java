package hr.fer.zemris.optjava.dz3.models.neighborhoods;

import hr.fer.zemris.optjava.dz3.models.solutions.DoubleArraySolution;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 19.10.2016..
 */
public abstract class AbstractDoubleArrayNeighborhood implements INeighborhood<DoubleArraySolution> {
    protected double[] deltas;
    protected Random random;

    public AbstractDoubleArrayNeighborhood(double[] deltas, Random random) {
        this.deltas = deltas;
        this.random = random;
    }

    protected DoubleArraySolution randomNeighbor(DoubleArraySolution solution,
            Function<Double, Double>
            distribution) {
        DoubleArraySolution duplicate = solution.duplicate();
        double[] values = duplicate.getValue();

        for(int i = 0; i < values.length; i++) {
            values[i] += distribution.apply(deltas[i]);
        }

        return duplicate;
    }
}
