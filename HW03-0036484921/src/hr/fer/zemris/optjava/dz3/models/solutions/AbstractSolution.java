package hr.fer.zemris.optjava.dz3.models.solutions;

/**
 * Created by Dominik on 18.10.2016..
 */
public abstract class AbstractSolution<T> implements Comparable<AbstractSolution<T>> {
    public double fitness;
    public T value;

    public AbstractSolution() {
    }

    protected AbstractSolution(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int compareTo(AbstractSolution<T> o) {
        if (this.fitness < o.fitness) {
            return -1;
        }
        if (this.fitness == o.fitness) {
            return 0;
        }
        return 1;
    }
}
