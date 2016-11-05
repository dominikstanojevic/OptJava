package hr.fer.zemris.optjava.dz4.models;

import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 25.10.2016..
 */
public class Pair<T extends AbstractSolution> {
    public T first;
    public T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
}
