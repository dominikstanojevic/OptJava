package hr.fer.zemris.optjava.dz3.models.solutions;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by Dominik on 18.10.2016..
 */
public class DoubleArraySolution extends AbstractSolution<double[]> {

    public DoubleArraySolution(int size) {
        this.value = new double[size];
    }

    private DoubleArraySolution(double[] values) {
        super(values);
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(value.length);
    }

    public DoubleArraySolution duplicate() {
        double[] copy = Arrays.copyOf(value, value.length);
        return new DoubleArraySolution(copy);
    }

    public void randomize(Random random, double[] min, double[] max) {
        for (int i = 0; i < value.length; i++) {
            value[i] = random.nextDouble() * (max[i] - min[i]) + min[i];
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (double v : value) {
            sj.add(Double.toString(v));
        }

        return sj.toString();
    }
}
