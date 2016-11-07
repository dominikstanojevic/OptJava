package hr.fer.zemris.optjava.dz4.models;

import hr.fer.zemris.optjava.dz4.models.solutions.AbstractSolution;

/**
 * Created by Dominik on 25.10.2016..
 */
public class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
}
