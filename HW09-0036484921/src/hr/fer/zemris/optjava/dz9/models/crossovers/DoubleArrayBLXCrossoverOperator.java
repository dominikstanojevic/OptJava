package hr.fer.zemris.optjava.dz9.models.crossovers;

import hr.fer.zemris.optjava.dz9.models.Pair;
import hr.fer.zemris.optjava.dz9.models.solutions.DoubleArraySolution;

import java.util.Random;

/**
 * Created by Dominik on 4.11.2016..
 */
public class DoubleArrayBLXCrossoverOperator implements ICrossoverOperator<DoubleArraySolution> {
    private double alpha;

    public DoubleArrayBLXCrossoverOperator(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Pair<DoubleArraySolution, DoubleArraySolution> getChildren(
            Pair<DoubleArraySolution, DoubleArraySolution> parents, Random random) {
        double[] firstParent = parents.first.chromosome;
        double[] secondParent = parents.second.chromosome;

        DoubleArraySolution firstChild = parents.first.newLikeThis();
        DoubleArraySolution secondChild = parents.second.newLikeThis();

        int size = firstChild.chromosome.length;

        for (int i = 0; i < size; i++) {
            double min = Math.min(firstParent[i], secondParent[i]);
            double max = Math.max(firstParent[i], secondParent[i]);

            double d = max - min;

            firstChild.chromosome[i] = random.nextDouble() * d * (1 + 2 * alpha) + (min - alpha * d);
            secondChild.chromosome[i] = random.nextDouble() * d * (1 + 2 * alpha) + (min - alpha * d);
        }

        return new Pair<>(firstChild, secondChild);
    }
}
