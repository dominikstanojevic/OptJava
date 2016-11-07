package hr.fer.zemris.optjava.dz4.models.crossovers;

import hr.fer.zemris.optjava.dz4.models.Pair;
import hr.fer.zemris.optjava.dz4.models.solutions.DoubleArraySolution;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Dominik on 4.11.2016..
 */
public class DoubleArrayBLXCrossoverOperator implements ICrossoverOperator<DoubleArraySolution> {
    private double alpha;
    private Supplier<DoubleArraySolution> supplier;

    public DoubleArrayBLXCrossoverOperator(Supplier<DoubleArraySolution> supplier, double alpha) {
        this.supplier = supplier;
        this.alpha = alpha;
    }

    @Override
    public Pair<DoubleArraySolution, DoubleArraySolution> getChildren(
            Pair<DoubleArraySolution, DoubleArraySolution> parents, Random random) {
        double[] firstParent = parents.first.chromosome;
        double[] secondParent = parents.second.chromosome;

        DoubleArraySolution firstChild = supplier.get();
        DoubleArraySolution secondChild = supplier.get();

        int size = firstChild.chromosome.length;

        for (int i = 0; i < size; i++) {
            double min = Math.min(firstParent[i], secondParent[i]);
            double max = Math.max(firstParent[i], secondParent[i]);

            double d = max - min;

            firstChild.chromosome[i] = random.nextDouble() * (max - min + 2 * alpha * d) + (min - alpha * d);
            secondChild.chromosome[i] = random.nextDouble() * (max - min + 2 * alpha * d) + (min - alpha * d);
        }

        return new Pair<>(firstChild, secondChild);
    }
}
