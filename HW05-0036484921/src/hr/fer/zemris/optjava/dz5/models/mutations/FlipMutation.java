package hr.fer.zemris.optjava.dz5.models.mutations;

import hr.fer.zemris.optjava.dz5.models.solutions.BitVectorSolution;

import java.util.Random;

/**
 * Created by Dominik on 11.11.2016..
 */
public class FlipMutation implements IMutationOperator<BitVectorSolution> {
    private double rate;

    public FlipMutation(double rate) {
        this.rate = rate;
    }

    public FlipMutation(int average, int n) {
        rate = ((double) average) / n;
    }

    @Override
    public void mutate(BitVectorSolution solution, Random random) {
        boolean[] chromosome = solution.chromosome;
        for(int i = 0; i < chromosome.length; i++) {
            if (rate > random.nextDouble()) {
                chromosome[i] = !chromosome[i];
            }
        }
    }
}
