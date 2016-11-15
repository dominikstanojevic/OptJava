package hr.fer.zemris.optjava.dz5.models.crossovers;

import hr.fer.zemris.optjava.dz5.models.Pair;
import hr.fer.zemris.optjava.dz5.part2.QAPSolution;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by Dominik on 14.11.2016..
 */
public class QAP2PointCrossover implements ICrossoverOperator<QAPSolution> {
    private Supplier<QAPSolution> supplier;

    public QAP2PointCrossover(Supplier<QAPSolution> supplier) {
        this.supplier = supplier;
    }

    @Override
    public QAPSolution getChild(
            Pair<QAPSolution, QAPSolution> parents, Random random) {
        QAPSolution child = supplier.get();
        int[] childChromosome = child.chromosome;

        int[] firstChromosome = parents.first.chromosome;
        int[] secondChromosome = parents.second.chromosome;

        int firstPoint = random.nextInt(childChromosome.length);
        int secondPoint = random.nextInt(childChromosome.length);

        int min = Math.min(firstPoint, secondPoint);
        int max = Math.max(firstPoint, secondPoint);

        int[] copy = new int[max - min];
        System.arraycopy(firstChromosome, min, copy, 0, max - min);

        for (int i = 0, pos = 0; i < childChromosome.length; i++) {
            if (i >= min && i < max) {
                childChromosome[i] = copy[i - min];
                continue;
            }

            while (contains(copy, secondChromosome[pos])) {
                pos++;
            }

            childChromosome[i] = secondChromosome[pos];
            pos++;
        }

        return child;
    }

    private boolean contains(int[] array, int value) {
        return Arrays.stream(array).anyMatch(i -> i == value);
    }
}
