package hr.fer.zemris.optjava.dz11.rng;

import java.util.Random;

/**
 * Created by Dominik on 20.1.2017..
 */
public class RNGRandomImpl implements IRNG{
    private Random random;

    public RNGRandomImpl() {
        this.random = new Random();
    }

    public RNGRandomImpl(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public double nextDouble() {
       return random.nextDouble();
    }

    @Override
    public double nextDouble(double min, double max) {
        if(min > max) {
            throw new IllegalArgumentException("Invalid range. Minimum cannot be higher than maximum.");
        }
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public float nextFloat(float min, float max) {
        if(min > max) {
            throw new IllegalArgumentException("Invalid range. Minimum cannot be higher than maximum.");
        }
        return random.nextFloat() * (max - min) + min;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
        if(min > max) {
            throw new IllegalArgumentException("Invalid range. Minimum cannot be higher than maximum.");
        }
        return random.nextInt(max - min) + min;
    }

    @Override
    public boolean nextBoolean() {
        return  random.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return random.nextGaussian();
    }
}
