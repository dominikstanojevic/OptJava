package hr.fer.zemris.optjava.dz5.models.solutions;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSolution<?> that = (AbstractSolution<?>) o;

        return chromosome.equals(that.chromosome);

    }

    @Override
    public int hashCode() {
        return chromosome.hashCode();
    }
}
