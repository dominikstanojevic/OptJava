package hr.fer.zemris.optjava.dz11.solutions;

import hr.fer.zemris.optjava.dz11.rng.RNG;

/**
 * Created by Dominik on 20.1.2017..
 */
public abstract class GASolution<T> implements Comparable<GASolution<T>> {
    protected T data;
    public double fitness;

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();

    public abstract GASolution<T> newLikeThis();

    @Override
    public int compareTo(GASolution<T> o) {
        return -Double.compare(this.fitness, o.fitness);
    }

    public abstract void randomize();
}
