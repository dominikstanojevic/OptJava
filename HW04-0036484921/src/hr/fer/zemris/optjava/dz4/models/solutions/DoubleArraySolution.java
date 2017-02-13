package hr.fer.zemris.optjava.dz4.models.solutions;

import java.util.Random;
import java.util.StringJoiner;

/**
 * Created by Dominik on 25.10.2016..
 */
public class DoubleArraySolution extends AbstractSolution<double[]> {
    protected double[] min;
    protected double[] max;

    public DoubleArraySolution(int size, double[] min, double[] max) {
        this(new double[size], min, max);
    }

    private DoubleArraySolution(double[] chromosome, double[] min, double[] max) {
        super(chromosome);

        this.min = min;
        this.max = max;
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(chromosome.length, min, max);
    }

    public void randomize(Random random) {
        for (int i = 0; i < chromosome.length; i++) {
            chromosome[i] = random.nextDouble() * (max[i] - min[i]) + min[i];
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (double n : chromosome) {
            sj.add(Double.toString(n));
        }

        return sj.toString();
    }

    @Override
    public boolean satisfiesConstraints() {
        for(int i = 0; i < chromosome.length; i++) {
            if (chromosome[i] < min[i] || chromosome[i] > max[i]) {
                return false;
            }
        }

        return true;
    }
}
