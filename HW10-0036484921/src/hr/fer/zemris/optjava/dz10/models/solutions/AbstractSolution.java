package hr.fer.zemris.optjava.dz10.models.solutions;

import java.util.Objects;
import java.util.Random;

/**
 * Created by Dominik on 25.10.2016..
 */
public abstract class AbstractSolution<T> implements Comparable<AbstractSolution> {
    public double fitness;
    public T chromosome;
    public double distance;
    public double[] values;

    protected AbstractSolution(T chromosome) {
        this.chromosome = chromosome;
    }

    public abstract void randomize(Random random);

    @Override
    public int compareTo(AbstractSolution o) {
        Objects.requireNonNull(o, "Cannot compare to null solution.");

        if(this.fitness < o.fitness) {
            return 1;
        } else if (this.fitness == o.fitness) {
            if (this.distance > o.distance) {
                return 1;
            } else if (this.distance == o.distance) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public abstract String toString();

    public abstract boolean satisfiesConstraints();
}
