package hr.fer.zemris.optjava.dz3.models.neighborhoods;

import hr.fer.zemris.optjava.dz3.models.solutions.BitVectorSolution;

import java.util.Random;

/**
 * Created by Dominik on 24.10.2016..
 */
public class BitVectorNeighborhood implements INeighborhood<BitVectorSolution> {
    protected Random random;
    private int[] bits;

    public BitVectorNeighborhood(int[] bits, Random random) {
        this.random = random;
        this.bits = bits;
    }

    @Override
    public BitVectorSolution randomNeighbor(BitVectorSolution solution) {
        BitVectorSolution copy = solution.duplicate();

        int n = bits.length;
        for (int i = 0, pos = 0; i < n; i++) {
            int rand = random.nextInt(bits[i]);

            copy.value.flip(pos + rand);
            pos += bits[i];
        }

        return copy;
    }
}
