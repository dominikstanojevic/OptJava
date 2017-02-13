package hr.fer.zemris.optjava.dz11.solutions;

import hr.fer.zemris.optjava.dz11.rng.IRNG;
import hr.fer.zemris.optjava.dz11.rng.RNG;

import java.util.Arrays;

/**
 * Created by Dominik on 22.1.2017..
 */
public class IntArrayGASolution extends GASolution<int[]> {
    public int[] lowerBounds;
    public int[] upperBounds;

    public IntArrayGASolution(int size, int[] lowerBounds, int[] upperBounds) {
        if(size < 1) {
            throw new IllegalArgumentException("Array size cannot be less than 1. Given: " + size + ".");
        }

        data = new int[size];
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    public IntArrayGASolution(int size) {
        this(size, null, null);
    }

    private IntArrayGASolution(int[] data, int[] lowerBounds, int[] upperBounds) {
        this.data = data;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    @Override
    public GASolution<int[]> duplicate() {
        int[] copy = Arrays.copyOf(this.data, this.data.length);
        return new IntArrayGASolution(copy, lowerBounds, upperBounds);
    }

    @Override
    public GASolution<int[]> newLikeThis() {
       return new IntArrayGASolution(this.data.length, lowerBounds, upperBounds);
    }

    @Override
    public void randomize() {
        IRNG random = RNG.getRNG();
        for(int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(lowerBounds[i], upperBounds[i]);
        }
    }
}
