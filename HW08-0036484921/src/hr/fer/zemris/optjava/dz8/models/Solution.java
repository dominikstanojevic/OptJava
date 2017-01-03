package hr.fer.zemris.optjava.dz8.models;

import hr.fer.zemris.optjava.dz8.models.neural.Utils;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dominik on 18.12.2016..
 */
public class Solution implements Comparable<Solution> {
    public RealVector values;
    public double fitness;

    public Solution(RealVector values) {
        this.values = values;
    }

    public static Solution randomize(int size) {
        RealVector values = Utils.createRandomVector(size, -1, 1);
        return new Solution(values);
    }

    @Override
    public int compareTo(Solution o) {
        return Double.compare(this.fitness, o.fitness);
    }

    @Override
    public String toString() {
        return Double.toString(fitness);
    }
}
