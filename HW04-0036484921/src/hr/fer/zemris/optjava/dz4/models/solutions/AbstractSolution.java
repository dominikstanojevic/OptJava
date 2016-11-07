package hr.fer.zemris.optjava.dz4.models.solutions;

import hr.fer.zemris.optjava.dz4.part2.models.Stick;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Dominik on 25.10.2016..
 */
public abstract class AbstractSolution<T> implements Comparable<AbstractSolution> {
    public double fitness;
    public T chromosome;
    protected AbstractSolution(T chromosome) {
        this.chromosome = chromosome;
    }

    public abstract void randomize(Random random);

    @Override
    public int compareTo(AbstractSolution o) {
        Objects.requireNonNull(o, "Cannot compare to null solution.");

        return Double.compare(this.fitness, o.fitness);
    }

    @Override
    public abstract String toString();
}
