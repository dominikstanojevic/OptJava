package hr.fer.zemris.optjava.dz11.mutations;

import hr.fer.zemris.optjava.dz11.rng.IRNG;
import hr.fer.zemris.optjava.dz11.rng.RNG;
import hr.fer.zemris.optjava.dz11.solutions.GASolution;
import hr.fer.zemris.optjava.dz11.solutions.IntArrayGASolution;

/**
 * Created by Dominik on 22.1.2017..
 */
public class IntRandomMutation<T extends GASolution<int[]>> implements IMutationOperator<T> {
    public static final double DEFAULT_MUTATION_RATE = 0.1;
    private double mutationRate;

    public IntRandomMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public IntRandomMutation() {
        this(DEFAULT_MUTATION_RATE);
    }

    @Override
    public void mutate(T solution) {
        IntArrayGASolution s = (IntArrayGASolution) solution;
        int[] array = solution.getData();

        IRNG random = RNG.getRNG();
        for (int i = 0; i < array.length; i++) {
            if (random.nextDouble() <= mutationRate) {
                array[i] = random.nextInt(s.lowerBounds[i], s.upperBounds[i]);
            }
        }
    }
}
