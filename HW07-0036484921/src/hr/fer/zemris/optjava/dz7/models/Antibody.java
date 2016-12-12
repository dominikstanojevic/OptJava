package hr.fer.zemris.optjava.dz7.models;

import hr.fer.zemris.optjava.dz7.Utils;
import hr.fer.zemris.optjava.dz7.models.neural.Weights;

import java.util.Arrays;

/**
 * Created by Dominik on 12.12.2016..
 */
public class Antibody implements Weights, Comparable<Antibody> {
    public double[] values;
    public double affinity;

    public Antibody(int size) {
        values = new double[size];
    }

    private Antibody(double[] values, double affinity) {
        this.values = Arrays.copyOf(values, values.length);
        this.affinity = affinity;
    }

    public void randomize(double[] lowerBounds, double[] upperBounds) {
        for(int i = 0; i < values.length; i++) {
            values[i] = Utils.RANDOM.nextDouble() * (upperBounds[i] - lowerBounds[i]) + lowerBounds[i];
        }
    }

    public Antibody cloneAntibody() {
        return new Antibody(values, affinity);
    }

    @Override
    public String toString() {
        return Double.toString(1. / affinity);
    }

    @Override
    public int numberOfWeights() {
        return values.length;
    }

    @Override
    public double[] getWeights() {
        return values;
    }

    @Override
    public int compareTo(Antibody o) {
        return Double.compare(this.affinity, o.affinity);
    }
}
