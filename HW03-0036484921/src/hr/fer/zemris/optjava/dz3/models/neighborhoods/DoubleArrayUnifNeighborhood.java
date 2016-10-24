package hr.fer.zemris.optjava.dz3.models.neighborhoods;

import hr.fer.zemris.optjava.dz3.models.solutions.DoubleArraySolution;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Dominik on 19.10.2016..
 */
public class DoubleArrayUnifNeighborhood extends AbstractDoubleArrayNeighborhood {
    public DoubleArrayUnifNeighborhood(double[] deltas, Random random) {
        super(deltas, random);
    }

    @Override
    public DoubleArraySolution randomNeighbor(
            DoubleArraySolution solution) {
        Function<Double, Double> distribution =
                delta -> random.nextDouble() * 2 * delta - delta;
        return randomNeighbor(solution, distribution);
    }
}
