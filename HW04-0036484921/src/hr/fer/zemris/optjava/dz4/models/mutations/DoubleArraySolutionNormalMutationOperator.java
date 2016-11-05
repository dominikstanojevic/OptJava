package hr.fer.zemris.optjava.dz4.models.mutations;

import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public class DoubleArraySolutionNormalMutationOperator implements IMutationOperator<DoubleArraySolution> {
    private double sigma;
    private double mutationRate;

    public DoubleArraySolutionNormalMutationOperator(double sigma, double mutationRate) {
        this.sigma = sigma;
        this.mutationRate = mutationRate;
    }

    public DoubleArraySolutionNormalMutationOperator(double sigma) {
        this(sigma, 1);
    }

    @Override
    public void mutate(DoubleArraySolution solution, Random random) {
        double[] values = solution.chromosome;
        for (int i = 0; i < values.length; i++) {
            if (mutationRate != 1 && random.nextDouble() > mutationRate) {
                continue;
            }

            values[i] += random.nextGaussian() * sigma;
        }
    }
}
