package hr.fer.zemris.optjava.dz3.models.solutions;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by Dominik on 18.10.2016..
 */
public class BitVectorSolution extends AbstractSolution<BitSet> {
    private int size;

    public BitVectorSolution(int size) {
        this.size = size;
        value = new BitSet(size);

        value.set(0, size, false);
    }

    private BitVectorSolution(BitSet bits, int size) {
        super(bits);
        this.size = size;
    }

    public BitVectorSolution newLikeThis() {
        return new BitVectorSolution(size);
    }

    public BitVectorSolution duplicate() {
        BitSet newBitSet = new BitSet(size);

        for(int i = 0; i < size; i++) {
            newBitSet.set(i, value.get(i));
        }

        return new BitVectorSolution(newBitSet, size);
    }

    public void randomize(Random random) {
        for (int i = 0; i < size; i++) {
            value.set(i, random.nextBoolean());
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        long[] array = value.toLongArray();
        for (long b : array) {
            sb.append(Long.toBinaryString(b));
        }

        return sb.toString();
    }
}
