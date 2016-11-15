package hr.fer.zemris.optjava.dz5.models.mutations;

import hr.fer.zemris.optjava.dz5.part2.QAPSolution;

import java.util.Random;

/**
 * Created by Dominik on 12.11.2016..
 */
public class QAPMutation implements IMutationOperator<QAPSolution> {
    private double rate;
    private int mutations;

    public QAPMutation(double rate, int mutations) {
        this.rate = rate;
        this.mutations = mutations;
    }

    @Override
    public void mutate(QAPSolution solution, Random random) {
        int[] chromosome = solution.chromosome;

        for (int i = 0; i < mutations; i++) {
            if (rate >= random.nextDouble()) {
                continue;
            }

            int firstPoint = random.nextInt(chromosome.length);
            int secondPoint = random.nextInt(chromosome.length);

            int temp = chromosome[secondPoint];
            chromosome[secondPoint] = chromosome[firstPoint];
            chromosome[firstPoint] = temp;
        }
    }
}
