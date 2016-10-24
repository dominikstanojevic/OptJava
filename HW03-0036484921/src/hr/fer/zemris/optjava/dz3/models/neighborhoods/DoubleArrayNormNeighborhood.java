package hr.fer.zemris.optjava.dz3.models.neighborhoods;

import hr.fer.zemris.optjava.dz3.models.solutions.DoubleArraySolution;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Dominik on 19.10.2016..
 */
public class DoubleArrayNormNeighborhood extends AbstractDoubleArrayNeighborhood {
    public DoubleArrayNormNeighborhood(double[] deltas, Random random) {
        super(deltas, random);
    }

    @Override
    public DoubleArraySolution randomNeighbor(
            DoubleArraySolution solution) {
        Function<Double, Double> distribution = delta -> random.nextGaussian() * delta;

        return randomNeighbor(solution, distribution);
    }
}
